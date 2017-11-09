package org.tensorflow.demo.values;

import org.tensorflow.demo.circuit.Connection;
import org.tensorflow.demo.utils.Pair;

import java.util.Collection;

/**
* Interface for an element with multiple inputs.
*/
public interface MultipleInputs extends Input{
	/**
	* Set the specified input, and return the removed and new connection.
	*/
	Pair<Collection<Connection>, Connection> setInput(int index, Intermediate input);
	
	/**
	* Get the specified input
	*/
	Intermediate getInput(int index);
}
