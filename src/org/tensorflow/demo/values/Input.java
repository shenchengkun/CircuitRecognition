package org.tensorflow.demo.values;

import org.tensorflow.demo.circuit.Connection;

import java.util.Collection;

/**
* Provides an interface for CircuitElements that take input.
*/
public interface Input extends Evaluateable {
	/**
	* Attach an input to this element.
	* @Return the new connection.
	*/
	Connection attach(Intermediate input);
	
	/**
	* Unattach an input from this element.
	* @Return the connections to remove
	*/
	Collection<Connection> unattach(Intermediate input);

	/**
	* Similar to unattach, yet doesn't deal with the connections.
	*/
	void remove(Intermediate input);
}
