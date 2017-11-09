package org.tensorflow.demo.values;

import org.tensorflow.demo.circuit.Connection;
import org.tensorflow.demo.utils.Pair;

import java.util.Collection;

/**
* Provies an interface for an element that only accepts one input.
*/
public interface SingleInput extends Input{
	/**
	* Set the input and return the old connection and the new connection.
	*/
	Pair<Collection<Connection>, Connection> setInput(Intermediate input);
	
	/**
	* @Return the input
	*/
	Intermediate getInput();
}
