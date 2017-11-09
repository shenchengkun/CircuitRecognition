package org.tensorflow.demo.circuit;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.AppCompatImageButton;

import org.tensorflow.demo.utils.Constants;
import org.tensorflow.demo.utils.Utils;
import org.tensorflow.demo.visitors.Visitor;

import java.util.ArrayList;
import java.util.Collection;

/**
* Provide a generic circuit element that can be used rather than manually dealing with images on a per class basis.
*/
public abstract class CircuitElement extends AppCompatImageButton {
	private Bitmap[] images;
	private Bitmap image;

	/**
	* Context is needed because this is an image button, also we allow for any number of different states a gate can be in.
	*/
	public CircuitElement(Context context, Bitmap ... images) {
		super(context);
		this.images = images;
		setImage(0);
	}

	/**
	* Set the image of this image button to be a specific image. It will be scaled to the expected size automatically.
	*/
	protected void setImage(Bitmap image) {
		this.image = Utils.scale(image, Constants.GATE_WIDTH, Constants.GATE_WIDTH);
		super.setImageBitmap(this.image);
	}

	/**
	* Set the image of this button based on the input images from the constructor.
	* They will be scaled to the expected size automatically.
	*/
	protected void setImage(int index) {
		image = Utils.scale(images[index], Constants.GATE_WIDTH, Constants.GATE_WIDTH);
		super.setImageBitmap(image);
	}

	/**
	* @Return a Bitmap of the current image of this image button.
	*/
	public Bitmap getImage() {
		return image;
	}

	/**
	* @Return a collection of all connections involving this element. Inputs and/or Outputs are returned.
	*/
	public Collection<Connection> getConnections(){
		Collection<Connection> inputs = getInputConnections();
		Collection<Connection> outputs = getOutputConnections();
		
		Collection<Connection> connections = new ArrayList<>(inputs);
		connections.addAll(outputs);
		return connections;
	}

	/**
	* @Return a collection of all connections going into this element.
	*/
	public Collection<Connection> getInputConnections(){
		Collection<Connection> connections = new ArrayList<Connection>();
		return connections;
	}
	
	/**
	* @Return a collection of all connections coming from this element.
	*/
	public Collection<Connection> getOutputConnections(){
		Collection<Connection> connections = new ArrayList<Connection>();
		return connections;
	}

	/**
	* Release all connections involving this element.
	*/
	public abstract void unattachAll();
	
	/**
	* Provide a fully generic method that allows for utilizing different sub classes for specific different things.
	* Google the Visitor design pattern for more clarification.
	*/
	public abstract <T> T accept(Visitor<T> v);
}
