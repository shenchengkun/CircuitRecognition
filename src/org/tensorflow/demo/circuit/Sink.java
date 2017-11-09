package org.tensorflow.demo.circuit;

import android.content.Context;
import android.graphics.Bitmap;

import org.tensorflow.demo.utils.Pair;
import org.tensorflow.demo.values.Intermediate;
import org.tensorflow.demo.values.SingleInput;
import org.tensorflow.demo.values.Value;
import org.tensorflow.demo.visitors.Visitor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
* Class used to represent a Sink element.
*/
public class Sink extends CircuitElement implements SingleInput {
	private Intermediate input;

	public Sink(Context context, Bitmap off, Bitmap on, Bitmap undefined) {
		super(context, off, on, undefined);
		super.setImage(2); // image 2 is undefined as see above ^
	}

	@Override
	public boolean isEvaluatable() {
		boolean isEvaluatable = input != null && input.isEvaluatable();
		if (!isEvaluatable){
			super.setImage(2);
		}
		return isEvaluatable;
	}
	
	@Override
	public Value evaluate() {
		Value val = getInput().evaluate();

		if (val.getValue()) {
			super.setImage(1);
		} else {
			super.setImage(0);
		}

		return val;
	}

	@Override
	public Pair<Collection<Connection>, Connection> setInput(Intermediate input) {
		Collection<Connection> old = unattach(this.input);
		this.input = input;
		return new Pair<>(old, attach(input));
	}

	@Override
	public Intermediate getInput() {
		return input;
	}

	@Override
	public <T> T accept(Visitor<T> v) {
		return v.visit(this);
	}

	@Override
	public Connection attach(Intermediate input){
		if (input != null){
			return input.attach(this);
		}
		return null;
	}

	@Override
	public Collection<Connection> unattach(Intermediate input){
		if (input != null){
			return input.unattach(this);
		}
		return null;
	}

	@Override
	public void unattachAll(){
		unattach(input);
	}

	@Override
	public void remove(Intermediate input) {
		if (this.input == input){
			this.input = null;
		}
	}

	@Override
	public Collection<Connection> getInputConnections(){
		List<Connection> connections = new ArrayList<Connection>();

		if (input != null){
			connections.addAll(input.getOutputConnections(this));
		}

		return connections;
	}
}
