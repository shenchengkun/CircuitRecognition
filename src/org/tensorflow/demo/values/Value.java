package org.tensorflow.demo.values;

/**
* Helper class for boolean algebra
*/
public class Value {
	private boolean val;

	public Value(boolean val) {
		setValue(val);
	}

	public void setValue(boolean val) {
		this.val = val;
	}

	public boolean getValue() {
		return val;
	}

	public Value copy() {
		return new Value(getValue());
	}

	public void invert() {
		setValue(!getValue());
	}

	public void and(Value val) {
		setValue(getValue() && val.getValue());
	}

	public void or(Value val) {
		setValue(getValue() || val.getValue());
	}

	public void xor(Value val) {
		setValue(getValue() ^ val.getValue());
	}
}
