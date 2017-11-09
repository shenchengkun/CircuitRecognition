package org.tensorflow.demo.visitors;

import org.tensorflow.demo.circuit.And;
import org.tensorflow.demo.circuit.Nand;
import org.tensorflow.demo.circuit.Nor;
import org.tensorflow.demo.circuit.Not;
import org.tensorflow.demo.circuit.Or;
import org.tensorflow.demo.circuit.Sink;
import org.tensorflow.demo.circuit.Source;
import org.tensorflow.demo.circuit.Xnor;
import org.tensorflow.demo.circuit.Xor;

/**
 * Uses the visitor pattern to generalize gate access. 
 */
public interface Visitor<T> {
	public T visit(final And gate);
	public T visit(final Nand gate);
	public T visit(final Or gate);
	public T visit(final Nor gate);
	public T visit(final Xor gate);
	public T visit(final Xnor gate);
	public T visit(final Not gate);

	public T visit(final Sink sink);
	public T visit(final Source source);
}
