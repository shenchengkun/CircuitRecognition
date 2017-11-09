package org.tensorflow.demo.ui;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.util.AttributeSet;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import org.tensorflow.demo.R;
import org.tensorflow.demo.circuit.CircuitFactory;
import org.tensorflow.demo.circuit.Gate;
import org.tensorflow.demo.circuit.Sink;
import org.tensorflow.demo.circuit.Source;
import org.tensorflow.demo.database.DatabaseElement;
import org.tensorflow.demo.database.GateDatabase;
import org.tensorflow.demo.database.GateElement;
import org.tensorflow.demo.inference.Recognition;
import org.tensorflow.demo.utils.Constants;
import org.tensorflow.demo.inference.TensorFlowImageClassifier;
import org.tensorflow.demo.visitors.Listeners;
import org.tensorflow.demo.visitors.ViewListeners;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
* This is the "main" file of this application. This class handles the drawing
* of gates, and the passing of these images to the classifier. Also this handles
* The creation of new gates and adding them to the main view.
*/
public class DrawingView extends android.widget.RelativeLayout {
	private Button DB;
	private int i=0;
	// Canvas and drawing
	private Canvas drawCanvas;
	private Bitmap canvasBitmap;
	private Path drawPath;
	private Paint drawPaint, canvasPaint;
	private boolean lineStarted;

	// Constants for user visuals
	private CircuitFactory gf;
	private int[] EMPTY; // Used for erasing the canvas bitmap

	private GateDatabase db;

	// Inputs to TensorFlowImageClassifier
	private TensorFlowImageClassifier cl;
	private int min_X, min_Y, max_X, max_Y; // Define the bounding box of the
											// current image on the canvas

	// Handle auto classification
	private Handler handler = new Handler(Looper.getMainLooper());
	private Runnable classify = new Runnable() {
		@Override
		public void run() {
			save();
		}
	};

	public DrawingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setupDrawing();
	}

	public void initialize(TensorFlowImageClassifier cl, GateDatabase db, Activity act) {
		this.cl = cl;
		this.db = db;
		this.act = act; // shouldn't need act after the DB is working correctly.
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		/*
		* Initialize everything that requires information on the size of the view.
		*/

		canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		drawCanvas = new Canvas(canvasBitmap);

		EMPTY = new int[canvasBitmap.getWidth() * canvasBitmap.getHeight()];

		Context context = getContext();
		ViewListeners ls = new ViewListeners(context, this, null);
		gf = new CircuitFactory(context, getResources(), ls);
		ls.setFactory(gf);

		Sink sink = initializeSink();
		ls.setSink(sink);
		DB= (Button) findViewById(R.id.DB);

		resetScaling();

	}

	/**
	* Place a new sink at the right end of the screen, in the middle of the right side. 
	* @Return the new sink.
	*/
	private Sink initializeSink() {
		Sink sink = gf.makeSink();

		LayoutParams para = new LayoutParams(Constants.GATE_WIDTH, Constants.GATE_HEIGHT);
		para.leftMargin = super.getWidth() - Constants.GATE_WIDTH;
		para.topMargin = (super.getHeight() - Constants.GATE_HEIGHT) / 2;

		this.addView(sink, para);

		return sink;
	}

	/**
	* Reset the drawing box to being empty.
	*/
	private void resetScaling() {
		min_X = canvasBitmap.getWidth() - 1;
		min_Y = canvasBitmap.getHeight() - 1;

		max_X = 0;
		max_Y = 0;

		handler.removeCallbacks(classify); // Stop the timer from running the previous classification
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
		canvas.drawPath(drawPath, drawPaint);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float touchX = event.getX();
		float touchY = event.getY();

		if (lineStarted) {
			setImageBox(touchX, touchY);
		}

		handler.removeCallbacks(classify); // stop the timer from classifing the last image box

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			drawPath.moveTo(touchX, touchY);
			setImageBox(touchX, touchY);
			lineStarted = true;

			break;
		case MotionEvent.ACTION_MOVE:
			if (lineStarted) {
				drawPath.lineTo(touchX, touchY);
				setImageBox(touchX, touchY);
			}
			break;
		case MotionEvent.ACTION_UP:
			if (lineStarted) {
				drawCanvas.drawPath(drawPath, drawPaint);

				// Start the Timer to classify the drawn object in 1 second
				handler.postDelayed(classify, 1000);
			}

			lineStarted = false;
			drawPath.reset();
			break;
		default:
			return false;
		}

		invalidate();
		return true;
	}

	/**
	* Adjust the current drawing box to contain the new point.
	*/
	private void setImageBox(float touchX, float touchY) {
		min_X = Math.min(min_X, Math.max((int) touchX - Constants.DRAW_WIDTH, 0));
		min_Y = Math.min(min_Y, Math.max((int) touchY - Constants.DRAW_WIDTH, 0));

		max_X = Math.max(max_X, Math.min((int) touchX + Constants.DRAW_WIDTH, canvasBitmap.getWidth() - 1));
		max_Y = Math.max(max_Y, Math.min((int) touchY + Constants.DRAW_WIDTH, canvasBitmap.getHeight() - 1));
	}

	/**
	* Add a new Gate to the specified location according to the specified recognition.
	* @Return the gate database element.
	*/
	private DatabaseElement addGate(int x, int y, Recognition rec) {
		Date now = Calendar.getInstance().getTime();
		DateFormat df = new SimpleDateFormat(Constants.TIME_FORMAT);
		String currentTime = df.format(now);

		GateElement databaseElement = new GateElement(rec.getImage(), rec.getLabel(), rec.getLabel(), currentTime);

		Gate gate = gf.makeGate(databaseElement, rec.getRecognitionType());

		LayoutParams para = new LayoutParams(Constants.GATE_WIDTH, Constants.GATE_HEIGHT);
		para.leftMargin = x - Constants.GATE_WIDTH / 2;
		para.topMargin = y - Constants.GATE_HEIGHT / 2;

		this.addView(gate, para);

		invalidate();

		return databaseElement;
	}

	/**
	* Remove all drawing from the canvas.
	*/
	private void eraseCanvas() {
		canvasBitmap.setPixels(EMPTY, 0, canvasBitmap.getWidth(), 0, 0, drawCanvas.getWidth(), drawCanvas.getHeight());

		invalidate();
		resetScaling();
	}

	/**
	* Set the canvus up for drawing lines.
	*/
	private void setupDrawing() {
		drawPath = new Path();
		drawPaint = new Paint();
		drawPaint.setColor(Color.BLACK);

		drawPaint.setAntiAlias(true);
		drawPaint.setStrokeWidth(Constants.DRAW_WIDTH);
		drawPaint.setStyle(Paint.Style.STROKE);
		drawPaint.setStrokeJoin(Paint.Join.ROUND);
		drawPaint.setStrokeCap(Paint.Cap.ROUND);
		canvasPaint = new Paint(Paint.DITHER_FLAG);
	}

	/**
	* Run inference on the image that has been drawn inside of the current Image Box.
	*/
	public Recognition classifyCurrentImage() {
        	Recognition rec = null;

		// If the Image Box is large enough, run the inference.
		if (min_X + Constants.MIN_SIZE < max_X && min_Y + Constants.MIN_SIZE < max_Y) {
			Bitmap toScale = Bitmap.createBitmap(canvasBitmap, min_X, min_Y, max_X - min_X, max_Y - min_Y);
			Bitmap scaled = Bitmap.createScaledBitmap(toScale, Constants.INPUT_SIZE, Constants.INPUT_SIZE, false);
            		rec = cl.recognizeImage(scaled);
		} // If the Image Box is small enough, add a new source to the location
		else if (max_X - min_X < Constants.SOURCE_SIZE && max_Y - min_Y < Constants.SOURCE_SIZE){
			addSource((min_X + max_X) / 2, (min_Y + max_Y) / 2);
		}

        	eraseCanvas();
		return rec;
	}

	/**
	* Add a new Source at the specified location.
	*/
	private void addSource(int x, int y) {
		Source source = gf.makeSource();

		LayoutParams para = new LayoutParams(Constants.GATE_WIDTH, Constants.GATE_HEIGHT);
		para.leftMargin = x - Constants.GATE_WIDTH / 2;
		para.topMargin = y - Constants.GATE_HEIGHT / 2;

		this.addView(source, para);

		invalidate();

	}

	/**
	* Classify the current Image Box's image and add the result to the view.
	*/
	public DatabaseElement classify() {
		int mid_X = (max_X + min_X) / 2;
		int mid_Y = (max_Y + min_Y) / 2;

		Recognition rec = classifyCurrentImage();

		if (rec == null) {
			return null;
		}

		return addGate(mid_X, mid_Y, rec);
	}

	// Should remove after DB works.

	private Activity act;
	private int counter = new Random().nextInt();

	/**
	* Classify and save the image drawn in the Image Box.
	*/
	public void save() {
		DatabaseElement databaseElement = classify();

		if (databaseElement == null) {
			return;
		}

		db.insertData(databaseElement); // TODO make the db work...
		Bitmap temp=databaseElement.getDrawnImage();
		Date today = Calendar.getInstance().getTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String reportDate = df.format(today);

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		temp.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] byteArray = stream.toByteArray();

		DrawGates.sqLiteHelper.insertData(reportDate,databaseElement.getPredictedGate(), byteArray);

		// Once the DB works, everything below can be removed. For now this is a basic way
		// to save files.
		/*verifyStoragePermissions();

		String path = Environment.getExternalStorageDirectory() + "/Pictures/Database";
		File file = new File(path, ((GateElement)databaseElement).getPredictedGate() + counter++ + ".png");

		Bitmap pictureBitmap = ((GateElement)databaseElement).getDrawnImage(); // obtaining the Bitmap

		OutputStream fOut = null;
		try {
			file.delete();
			fOut = new FileOutputStream(file);
			pictureBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
		} catch (IOException arg0) {
			arg0.printStackTrace();
		} finally {
			try {
				if (fOut != null) {
					fOut.flush();
					fOut.close();
				}
			} catch (IOException arg0) {
				arg0.printStackTrace();
			}
		}*/
	}

	// Should remove after DB works.
	
	private final int REQUEST_EXTERNAL_STORAGE = 1;
	private final String[] PERMISSIONS_STORAGE = { Manifest.permission.READ_EXTERNAL_STORAGE,
			Manifest.permission.WRITE_EXTERNAL_STORAGE };

	// Should remove after DB works.
	public void verifyStoragePermissions() {
		// Check if we have write permission
		int permission = ActivityCompat.checkSelfPermission(act, Manifest.permission.WRITE_EXTERNAL_STORAGE);

		if (permission != PackageManager.PERMISSION_GRANTED) {
			// We don't have permission so prompt the user
			ActivityCompat.requestPermissions(act, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
		}
	}
}
