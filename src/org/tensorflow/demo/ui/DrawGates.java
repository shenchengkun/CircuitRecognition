package org.tensorflow.demo.ui;


import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;

import org.tensorflow.demo.R;
import org.tensorflow.demo.inference.TensorFlowImageClassifier;
import org.tensorflow.demo.utils.Constants;

import org.tensorflow.demo.database.GateDatabase;

import java.io.ByteArrayOutputStream;

/**
* Main activity for the drawing project.
*/
public class DrawGates extends Activity {
	TextView show;
	Button DB;
	GateDatabase db;
	public static SQLiteHelper sqLiteHelper;
	@Override
	/**
	* Initialize all classes that require context and activity.
	* Pass them into DrawingView
	*/

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_draw_gates);

		DB = (Button) findViewById(R.id.DB);
		show= (TextView) findViewById(R.id.add);
		sqLiteHelper = new SQLiteHelper(this, "FoodDB.sqlite", null, 1);
		sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS FOOD(Id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, price VARCHAR, image BLOB)");

		AssetManager assetManager = this.getApplicationContext().getAssets();
		TensorFlowImageClassifier tf = null;
		try {
			tf = TensorFlowImageClassifier.create(assetManager, Constants.MODEL_PATH, Constants.LABELS_PATH, Constants.NUM_CLASSES, Constants.INPUT_SIZE, 0,
					1f, Constants.INPUT_NAME, Constants.OUTPUT_NAME);
		} catch (Exception arg1) {
			arg1.printStackTrace();
		}


		db = new GateDatabase(this, "GateDB.sqlite", Constants.GATE_TABLE_NAME, Constants.GATE_KEY_NAME, null, 1);
		db.queryData(Constants.CREATE_GATE_TABLE);

		DrawingView drawer = (DrawingView) this.findViewById(R.id.drawing);
		drawer.initialize(tf, db, this); // TODO remove passing in this when the DB is working correctly.



		DB.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//Intent intent = new Intent(DrawGates.this, FoodList.class);
				//startActivity(intent);
				//Toast.makeText(getApplicationContext(), "You don't have permission to access database!", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(DrawGates.this, listAll1.class);
				startActivity(intent);
				//Integer var=Integer.valueOf(show.getText().toString());
				//var+=1;
				//show.setText("1");

			}
		});

	}
	public static byte[] imageViewToByte(ImageView image) {
		Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] byteArray = stream.toByteArray();
		return byteArray;
	}
}
