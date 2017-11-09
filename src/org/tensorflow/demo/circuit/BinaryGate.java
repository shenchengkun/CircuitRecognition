package org.tensorflow.demo.circuit;

import android.content.Context;
import android.graphics.Bitmap;

import org.tensorflow.demo.database.GateElement;
import org.tensorflow.demo.utils.Pair;
import org.tensorflow.demo.values.Input;
import org.tensorflow.demo.values.Intermediate;
import org.tensorflow.demo.values.MultipleInputs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A generic gate that takes two inputs and has one output.
 */
public abstract class BinaryGate extends Gate implements Intermediate, MultipleInputs {
	private Intermediate left, right;
	private Map<Input, List<Connection>> outputs;

	/**
	* Generic constructor.
	*/
	public BinaryGate(GateElement databaseElement, Context context, Bitmap image) {
		super(databaseElement, context, image);
		outputs = new HashMap<>();
	}

	@Override
	public boolean isEvaluatable() {
		if (left == null || right == null) {
			return false;
		}
		return left.isEvaluatable() && right.isEvaluatable();
	}
	
	@Override
	public Pair<Collection<Connection>, Connection> setInput(int index, Intermediate input){
		if (index == 0){
			return setLeft(input);
		}
		else{
			return setRight(input);
		}
	}

	@Override
	public Intermediate getInput(int index){
		if (index == 0){
			return getLeftInput();
		}
		else{
			return getRightInput();
		}
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
		setLeft(null);
		setRight(null);

		List<Input> outputs = new ArrayList<>();
		outputs.addAll(this.outputs.keySet());
		for (Input output : outputs){
			output.unattach(this);
		}

		this.outputs = new HashMap<>();
	}

	@Override
	public void remove(Intermediate input) {
		if (left == input){
			left = null;
		}
		if (right == input){
			right = null;
		}
	}

	/**
	* Sets the left input to this gate to be the intermediate value. If there was previsously a connection 
	* this connection is removed.
	* @Returns A pair of connections, the first is the connection removed (or null if there wasn't one) and
	* the second is the new connection.
	*/
	public Pair<Collection<Connection>, Connection> setLeft(Intermediate left) {
		Collection<Connection> old = unattach(this.left);
		this.left = left;
		return new Pair<>(old, attach(left));
	}

	/**
	* Sets the right input to this gate to be the intermediate value. If there was previsously a connection 
	* this connection is removed.
	* @Returns A pair of connections, the first is the connection removed (or null if there wasn't one) and
	* the second is the new connection.
	*/
	public Pair<Collection<Connection>, Connection> setRight(Intermediate right) {
		Collection<Connection> old = unattach(this.right);
		this.right = right;
		return new Pair<>(old, attach(right));
	}

	/**
	* @Returns The left input to this gate.
	*/
	public Intermediate getLeftInput() {
		return left;
	}

	/**
	* @Returns The right input to this gate.
	*/
	public Intermediate getRightInput() {
		return right;
	}
	
	@Override
	public Collection<Connection> getInputConnections(){
		List<Connection> connections = new ArrayList<Connection>();

		if (left != null){
			connections.addAll(left.getOutputConnections(this));
		}
		if (right != null){
			connections.addAll(right.getOutputConnections(this));
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
	* @Returns a collection of output wires that are a requested circuit element's inputs.
	*/
	public Collection<Connection> getOutputConnections(Input input){
		return outputs.get(input);
	}
}
