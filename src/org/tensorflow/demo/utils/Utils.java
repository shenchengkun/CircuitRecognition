package org.tensorflow.demo.utils;

import android.graphics.Bitmap;

/**
 * Usful Utility functions
 */
public class Utils {
	/**
	* @Return a new bitmap with the requested width and height that is
	* a scaled version of the input bitmap.
	*/
	public static Bitmap scale(Bitmap image, int width, int height) {
		return Bitmap.createScaledBitmap(image, width, height, false);
	}
}
