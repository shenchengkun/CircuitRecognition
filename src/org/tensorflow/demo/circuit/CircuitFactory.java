package org.tensorflow.demo.circuit;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.tensorflow.demo.R;
import org.tensorflow.demo.database.GateElement;
import org.tensorflow.demo.utils.Constants;
import org.tensorflow.demo.visitors.Listeners;

/**
* A factory class for all sub classes of CircuitElement.
*/
public class CircuitFactory {
	private final Context context;
	private final Resources resources;
	private final Listeners listeners;

	private final Bitmap[] images;

	/**
	* The listeners will be attached to each gate as specified in the Listeners implementation.
	*/
	public CircuitFactory(Context context, Resources resources, Listeners listeners) {
		this.context = context;
		this.resources = resources;
		this.listeners = listeners;

		images = new Bitmap[] { 
				BitmapFactory.decodeResource(resources, R.drawable.and),
				BitmapFactory.decodeResource(resources, R.drawable.nand),
				BitmapFactory.decodeResource(resources, R.drawable.nor),
				BitmapFactory.decodeResource(resources, R.drawable.not),
				BitmapFactory.decodeResource(resources, R.drawable.or),
				BitmapFactory.decodeResource(resources, R.drawable.xnor),
				BitmapFactory.decodeResource(resources, R.drawable.xor),
				BitmapFactory.decodeResource(resources, R.drawable.sink_on),
				BitmapFactory.decodeResource(resources, R.drawable.sink_off),
				BitmapFactory.decodeResource(resources, R.drawable.sink_null),
				BitmapFactory.decodeResource(resources, R.drawable.source_on),
				BitmapFactory.decodeResource(resources, R.drawable.source_off)
		};
	}

	/**
	* Generate a new Gate of the specified type, with the specified storage data.
	*/
	public Gate makeGate(GateElement databaseElement, int type) {
		switch (type) {
		case Constants.AND:
			return makeAnd(databaseElement);
		case Constants.NAND:
			return makeNand(databaseElement);
		case Constants.OR:
			return makeOr(databaseElement);
		case Constants.NOR:
			return makeNor(databaseElement);
		case Constants.XOR:
			return makeXor(databaseElement);
		case Constants.XNOR:
			return makeXnor(databaseElement);
		case Constants.NOT:
			return makeNot(databaseElement);
		default:
			return null;
		}
	}

	/**
	* Generate a new Element of the specified type.
	*/
	public CircuitElement makeElement(int type) {
		switch (type) {
		case Constants.SINK:
			return makeSink();
		case Constants.SOURCE:
			return makeSource();
		default:
			return null;
		}
	}

	public And makeAnd(GateElement databaseElement) {
		Bitmap image = images[Constants.AND];
		And gate = new And(databaseElement, context, image);
		gate.accept(listeners);
		return gate;
	}

	public Nand makeNand(GateElement databaseElement) {
		Bitmap image = images[Constants.NAND];
		Nand gate = new Nand(databaseElement, context, image);
		gate.accept(listeners);
		return gate;
	}

	public Or makeOr(GateElement databaseElement) {
		Bitmap image = images[Constants.OR];
		Or gate = new Or(databaseElement, context, image);
		gate.accept(listeners);
		return gate;
	}

	public Nor makeNor(GateElement databaseElement) {
		Bitmap image = images[Constants.NOR];
		Nor gate = new Nor(databaseElement, context, image);
		gate.accept(listeners);
		return gate;
	}

	public Xor makeXor(GateElement databaseElement) {
		Bitmap image = images[Constants.XOR];
		Xor gate = new Xor(databaseElement, context, image);
		gate.accept(listeners);
		return gate;
	}

	public Xnor makeXnor(GateElement databaseElement) {
		Bitmap image = images[Constants.XNOR];
		Xnor gate = new Xnor(databaseElement, context, image);
		gate.accept(listeners);
		return gate;
	}

	public Not makeNot(GateElement databaseElement) {
		Bitmap image = images[Constants.NOT];
		Not gate = new Not(databaseElement, context, image);
		gate.accept(listeners);
		return gate;
	}

	public Sink makeSink() {
		Bitmap on = images[Constants.SINK_ON];
		Bitmap off = images[Constants.SINK_OFF];
		Bitmap undefined = images[Constants.SINK_UNDEFINED];
		Sink sink = new Sink(context, off, on, undefined);
		sink.accept(listeners);
		return sink;
	}

	public Source makeSource() {
		Bitmap on = images[Constants.SOURCE_ON];
		Bitmap off = images[Constants.SOURCE_OFF];
		Source source = new Source(context, off, on);
		source.accept(listeners);
		return source;
	}
}
