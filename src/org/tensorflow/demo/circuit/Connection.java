package org.tensorflow.demo.circuit;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import org.tensorflow.demo.utils.Constants;
import org.tensorflow.demo.values.Input;
import org.tensorflow.demo.values.Intermediate;

/**
* This class provides a manhattan connection between two views.
*/
public class Connection {
	private View from, to;
	private Line l1, l2, l3;
	private RelativeLayout.LayoutParams p1, p2, p3;
	
	/**
	* context is needed to create the line elements.
	*/
	public Connection(Context context, Intermediate from, Input to) {
		/*
		* While being guarunteed to work in the project at the time this was written,
		* it may be a good idea to change the input types of *from* and *to* to View.
		*/
		this.from = (View) from;
		this.to = (View) to;

		l1 = new Line(context);
		l2 = new Line(context);
		l3 = new Line(context);
		
		recalculate();
	}
	
	/**
	* Generate the line positions for the views.
	*/
	private void recalculate(){
		// TODO handle the case where *from* has a larger X value than *to*. (two of the wires currently don't show up)
		// This could be fixed by doing two vertical wires and one horizontal wire rather than two horizontal and 
		// one vertical.
		int x_1 = (int)(from.getX() + 3 * from.getWidth() / 4);
		int y_1 = (int)(from.getY() + from.getHeight() / 2 - Constants.LINE_WIDTH / 2);
		
		int x_3 = (int)to.getX();
		int y_3 = (int)(to.getY() + to.getHeight() / 2 - Constants.LINE_WIDTH / 2);

		// TODO Kind of horrible coding... but it lines up the wires to look good...
		// This only works at all because we decided not to handle multiple inputs from the same gate.
		// This could be fixed by adding an offset parameter to the constructor of this Connection.
		if (to instanceof BinaryGate){
			if (((BinaryGate) to).getLeftInput() == from){
				y_3 -= 7 * to.getHeight() / 32;
			}
			else{
				y_3 += 7 * to.getHeight() / 32;
			}
		}
		
		int x_2 = (x_1 + x_3 + Constants.LINE_OFFSET - Constants.LINE_WIDTH) / 2;
		int y_2 = Math.min(y_1, y_3);
		

		p1 = new RelativeLayout.LayoutParams(x_2 - x_1, Constants.LINE_WIDTH);
		p1.leftMargin = x_1;
		p1.topMargin = y_1;
		

		p2 = new RelativeLayout.LayoutParams(Constants.LINE_WIDTH, Constants.LINE_WIDTH + Math.abs(y_1 - y_3));
		p2.leftMargin = x_2;
		p2.topMargin = y_2;
		

		p3 = new RelativeLayout.LayoutParams(x_2 - x_1, Constants.LINE_WIDTH);
		p3.leftMargin = x_2 + Constants.LINE_WIDTH;
		p3.topMargin = y_3;
	}

	/**
	* Add this wire connection to a specified ViewGroup.
	*/
	public void addTo(ViewGroup mainView) {
		mainView.addView(l1, p1);
		mainView.addView(l2, p2);
		mainView.addView(l3, p3);
	}

	/**
	* Remove this wire connection from a specified ViewGroup.
	*/
	public void removeFrom(ViewGroup mainView) {
		mainView.removeView(l1);
		mainView.removeView(l2);
		mainView.removeView(l3);
	}

	/**
	* Realign this wire connection in a specified ViewGroup. If the connection wasn't
	* in the ViewGroup it will be added.
	*/
	public void refresh(ViewGroup mainView) {
		removeFrom(mainView);
		recalculate();
		addTo(mainView);
	}
}
