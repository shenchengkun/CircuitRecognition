package org.tensorflow.demo.values;

import org.tensorflow.demo.circuit.Connection;

import java.util.Collection;

/**
* Provides an interface for a CircuitElement that has output connections.
*/
public interface Intermediate extends Evaluateable {
	/**
	* Add another output to this element.
	*/
	Connection attach(Input output);
	
	/**
	* Unattach all the outputs going to the output element.
	*/
	Collection<Connection> unattach(Input output);
	
	/**
	* Return the connections going from this element to the input element.
	*/
	Collection<Connection> getOutputConnections(Input input);
}
