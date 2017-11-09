package org.tensorflow.demo.circuit;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

/**
* A VERY bare-bones class to use as a Line inside the Connection class.
* Honestly probably could be an inner class in Connection.
*/
public class Line extends View {
	public Line(Context context){
		super(context);
		setBackgroundColor(Color.BLACK);
	}
}
