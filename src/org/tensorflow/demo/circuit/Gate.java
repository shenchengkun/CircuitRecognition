package org.tensorflow.demo.circuit;

import android.content.Context;
import android.graphics.Bitmap;

import org.tensorflow.demo.database.GateElement;

/**
* A super class of all the Gates. Basically used to differentiate what is drawn and what isn't.
*/
public abstract class Gate extends CircuitElement {
	private GateElement dbElement;

	public Gate(GateElement databaseElement, Context context, Bitmap... images) {
		super(context, images);
		this.dbElement = databaseElement;
	}

	/**
	* @Return the meta-data related to this gate.
	*/
	public GateElement getDatabaseElement(){
		return dbElement;
	}
}
