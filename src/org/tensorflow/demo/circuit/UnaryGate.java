package org.tensorflow.demo.circuit;

import android.content.Context;
import android.graphics.Bitmap;

import org.tensorflow.demo.database.GateElement;
import org.tensorflow.demo.utils.Pair;
import org.tensorflow.demo.values.Input;
import org.tensorflow.demo.values.Intermediate;
import org.tensorflow.demo.values.SingleInput;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a Gate with one input and one output. (basically and inverter... but I wanted to make everything similar).
 */
public abstract class UnaryGate extends Gate implements Intermediate, SingleInput {
	private Intermediate input;
	private Map<Input, List<Connection>> outputs;

	protected UnaryGate(GateElement databaseElement, Context context, Bitmap image) {
		super(databaseElement, context, image);
		outputs = new HashMap<>();
	}

	@Override
	public boolean isEvaluatable() {
		return input != null && input.isEvaluatable();
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

		List<Input> outputs = new ArrayList<>();
		outputs.addAll(this.outputs.keySet());
		for (Input output : outputs){
			output.unattach(this);
		}
		
		this.outputs = new HashMap<>();
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

	@Override
	public Collection<Connection> getOutputConnections(){
		Collection<Connection> connections = new ArrayList<>();

		for(Collection<Connection> connectionGroup : outputs.values()){
			connections.addAll(connectionGroup);
		}

		return connections;
	}

	/**
	* @Return the connections going from this source to the input.
	* This is a slightly confusing naming convention, Input means that it accepts input.
	*/
	public Collection<Connection> getOutputConnections(Input input){
		return outputs.get(input);
	}

}

