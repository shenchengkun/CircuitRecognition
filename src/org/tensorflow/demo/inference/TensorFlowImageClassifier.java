package org.tensorflow.demo.inference;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Trace;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

/** 
* A classifier made to classify images into logic gates using TensorFlow. 
*/
public class TensorFlowImageClassifier {
	/*
	* Taken from the Tensorflow example:
	*/
	static {
		System.loadLibrary("tensorflow_demo");
	}

	private static final String TAG = "TensorFlowImageClassifier";

	// Config values.
	private String inputName;
	private String outputName;
	private int inputSize;

	private int numClasses;

	// Pre-allocated buffers.
	private List<String> labels = new ArrayList<String>();
	private int[] intValues;
	private float[] floatValues;
	private float[] outputs;
	private String[] outputNames;

	private TensorFlowInferenceInterface inferenceInterface;

	private TensorFlowImageClassifier() {
	}

	/**
	 * Initializes a native TensorFlow session for classifying images.
	 *
	 * @param assetManager
	 *            The asset manager to be used to load assets.
	 * @param modelFilename
	 *            The filepath of the model GraphDef protocol buffer.
	 * @param labelFilename
	 *            The filepath of label file for classes.
	 * @param numClasses
	 *            The number of classes output by the model.
	 * @param inputSize
	 *            The input size. A square image of inputSize x inputSize is
	 *            assumed.
	 * @param imageMean
	 *            The assumed mean of the image values.
	 * @param imageStd
	 *            The assumed std of the image values.
	 * @param inputName
	 *            The label of the image input node.
	 * @param outputName
	 *            The label of the output node.
	 * @throws IOException
	 */
	public static TensorFlowImageClassifier create(AssetManager assetManager, String modelFilename,
			String labelFilename, int numClasses, int inputSize, int imageMean, float imageStd, String inputName,
			String outputName) throws IOException {
		TensorFlowImageClassifier c = new TensorFlowImageClassifier();
		c.inputName = inputName;
		c.outputName = outputName;

		// Read the label names into memory.
		String actualFilename = labelFilename.split("file:///android_asset/")[1];
		BufferedReader br = null;
		br = new BufferedReader(new InputStreamReader(assetManager.open(actualFilename)));
		String line;
		while ((line = br.readLine()) != null) {
			c.labels.add(line);
		}
		br.close();

		c.inputSize = inputSize;

		// Pre-allocate buffers.
		c.outputNames = new String[] { outputName };
		c.intValues = new int[inputSize * inputSize];

		c.floatValues = new float[inputSize * inputSize];

		c.outputs = new float[numClasses];
		c.numClasses = numClasses;

		c.inferenceInterface = new TensorFlowInferenceInterface();

		final int status = c.inferenceInterface.initializeTensorFlow(assetManager, modelFilename);
		if (status != 0) {
			throw new RuntimeException("TF init status (" + status + ") != 0");
		}
		return c;
	}

	/**
	* Run the inference on a given image and return the recognition.
	*/
	public Recognition recognizeImage(final Bitmap bitmap) {
		bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
		for (int i = 0; i < intValues.length; ++i) {
			final int val = intValues[i];

			// These numbers may seem random... They are used due to the image type we are using.
			// You can look into the initialization of the bitmap in DrawingView to understand the image type more.
			intValues[i] = val == 0 ? -16777216 : -1; // invert the image
			floatValues[i] = val != 0 ? 1 : 0;
		}

		// Copy the input data into TensorFlow.
		inferenceInterface.fillNodeFloat(inputName, new int[] { inputSize * inputSize }, floatValues);

		// Run the inference call.
		inferenceInterface.runInference(outputNames);

		// Copy the output Tensor back into the output array.
		int error = inferenceInterface.readNodeFloat(outputName, outputs);
		
		if(error != 0){
			// This really sucks. Make sure you have all the input and output names correct.
			throw new RuntimeException("Failed to read inference output with error code " + error);
		}

		// Find the best classification.
		int minIndex = -1;
		double minValue = Double.NEGATIVE_INFINITY;
		for (int i = 0; i < numClasses; i++) {
			if (outputs[i] > minValue) {
				minIndex = i;
				minValue = outputs[i];
			}
		}

		bitmap.setPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
		Recognition recognition = new Recognition(bitmap, labels.get(minIndex), minIndex);

		return recognition;
	}
}
