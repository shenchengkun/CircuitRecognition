package org.tensorflow.demo.inference;

import android.graphics.Bitmap;

/**
 * This is a class for returning needed information from the classifer
 */
public class Recognition {
	private Bitmap image;
	private String label;
	private int recognitionType;

	public Recognition(Bitmap image, String label, int recognitionType) {
		this.image = image;
		this.label = label;
		this.recognitionType = recognitionType;
	}

	public Bitmap getImage() {
		return image;
	}

	public String getLabel() {
		return label;
	}

	public int getRecognitionType() {
		return recognitionType;
	}
}
