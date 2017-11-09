package org.tensorflow.demo.circuit;

import android.content.Context;
import android.graphics.Bitmap;

import org.tensorflow.demo.database.GateElement;
import org.tensorflow.demo.values.Value;
import org.tensorflow.demo.visitors.Visitor;

/**
* Class used to represent an And gate.
*/
public class And extends BinaryGate {
	/**
	* And constructor. The need to pass in the image parameter into this file is because this class is extended by Nand.
	*/
	public And(GateElement databaseElement, Context context, Bitmap image) {
		super(databaseElement, context, image);
	}

	@Override
	public Value evaluate() {
		Value val = super.getLeftInput().evaluate();
		val.and(super.getRightInput().evaluate());
		return val;
	}
	
	@Override
	public <T> T accept(Visitor<T> v) {
		return v.visit(this);
	}
}
