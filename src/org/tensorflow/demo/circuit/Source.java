package org.tensorflow.demo.circuit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;

import org.tensorflow.demo.values.Input;
import org.tensorflow.demo.values.Intermediate;
import org.tensorflow.demo.values.Value;
import org.tensorflow.demo.visitors.Visitor;

/**
 * This reprents a Source or initial value for the circuit.
 */
public class Source extends CircuitElement implements Intermediate {
	private Value toReturn;
	private Map<Input, List<Connection>> outputs;

	public Source(Context context, Bitmap off, Bitmap on) {
		super(context, off, on);
		super.setImage(1); // initial image is on

		outputs = new HashMap<>();
		toReturn = new Value(true); // initial state (as seen with set image) is on (or true).
	}

	/**
	* invert the value and image of this source.
	*/
	public void invert() {
		toReturn.invert();

		if (toReturn.getValue()) {
			super.setImage(1);
		} else {
			super.setImage(0);
		}
	}

	@Override
	public boolean isEvaluatable() {
		return true;
	}

	@Override
	public Value evaluate() {
		return toReturn.copy();
	}

	public <T> T accept(Visitor<T> v) {
		return v.visit(this);
	}

	@Override
	public Connection attach(Input output){
		if (output != null){
			Connection connection = new Connection(getContext(), this, output);

			List<Connection> connections = outputs.get(output);
			if (connections == null){
				connections = new ArrayList<>();
				outputs.put(output, connections);
			}
			connections.add(connection);

			return connection;
		}
		return null;
	}

	@Override
	public Collection<Connection> unattach(Input output){
		if (output != null){
			output.remove(this);
			return outputs.remove(output);
		}
		return null;
	}

	@Override
	public void unattachAll(){
		List<Input> outputs = new ArrayList<>();
		outputs.addAll(this.outputs.keySet());
		for (Input output : outputs){
			output.unattach(this);
		}

		this.outputs = new HashMap<>();
	}

	@Override
	public Collection<Connection> getOutputConnections(){
		Collection<Connection> connections = new ArrayList<>();

		for(Collection<Connection> connectionGroup : outputs.values()){
			connections.addAll(connectionGroup);
		}

		return connections;
	}

	/**
	* Return the connections going from this source to the input.
	* This is a slightly confusing naming convention, Input means that it accepts input.
	*/
	public Collection<Connection> getOutputConnections(Input input){
		return outputs.get(input);
	}
}
