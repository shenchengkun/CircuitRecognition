package org.tensorflow.demo.circuit;

import android.content.Context;
import android.graphics.Bitmap;

import org.tensorflow.demo.database.GateElement;
import org.tensorflow.demo.values.Value;
import org.tensorflow.demo.visitors.Visitor;

public class Or extends BinaryGate {

    public Or(GateElement databaseElement, Context context, Bitmap image) {
        super(databaseElement, context, image);
    }

    @Override
    public Value evaluate() {
        Value val = super.getLeftInput().evaluate();
        val.or(super.getRightInput().evaluate());
        return val;
    }

	public <T> T accept(Visitor<T> v) {
		return v.visit(this);
	}

}
