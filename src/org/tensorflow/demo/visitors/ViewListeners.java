package org.tensorflow.demo.visitors;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import org.tensorflow.demo.circuit.And;
import org.tensorflow.demo.circuit.BinaryGate;
import org.tensorflow.demo.circuit.CircuitElement;
import org.tensorflow.demo.circuit.CircuitFactory;
import org.tensorflow.demo.circuit.Connection;
import org.tensorflow.demo.circuit.Gate;
import org.tensorflow.demo.circuit.Nand;
import org.tensorflow.demo.circuit.Nor;
import org.tensorflow.demo.circuit.Not;
import org.tensorflow.demo.circuit.Or;

import org.tensorflow.demo.circuit.Sink;
import org.tensorflow.demo.circuit.Source;
import org.tensorflow.demo.circuit.UnaryGate;
import org.tensorflow.demo.circuit.Xnor;
import org.tensorflow.demo.circuit.Xor;
import org.tensorflow.demo.database.GateElement;
import org.tensorflow.demo.ui.DrawGates;
import org.tensorflow.demo.ui.listAll1;
import org.tensorflow.demo.utils.Constants;
import org.tensorflow.demo.utils.Pair;
import org.tensorflow.demo.values.Intermediate;

import java.util.Collection;

/**
* Class for attaching listeners to new CircuitElements
*/
public class ViewListeners extends Listeners {
	private Context context;
	private ViewGroup viewGroup;
	private CircuitFactory factory;

	private Intermediate toConnect;
	private Sink sink;

	public ViewListeners(Context context, ViewGroup viewGroup, CircuitFactory factory) {
		this.context = context;
		this.viewGroup = viewGroup;
		this.factory = factory;
	}

	/**
	* This method is required because the factory must have a reference to this
	* class and needs to be passed in after creation.
	*/
	public void setFactory(CircuitFactory factory) {
		this.factory = factory;
	}
	
	/**
	* This method is required because the sink must be made inside DrawingView
	* a reference to this class and needs to be passed in after creation.
	*/
	public void setSink(Sink sink) {
		this.sink = sink;
	}

	/**
	* This is a helper method for updating the image of the sink.
	*/
	private void updateSink() {
		if (sink.isEvaluatable()){
			sink.evaluate();
		}
	}





	@Override
	public Boolean visit(final And gate){
		attachListeners(gate);
		return true;
	}
	
	@Override
	public Boolean visit(final Nand gate){
		attachListeners(gate);
		return true;
	}
	
	@Override
	public Boolean visit(final Or gate){
		attachListeners(gate);
		return true;
	}
	
	@Override
	public Boolean visit(final Nor gate){
		attachListeners(gate);
		return true;
	}
	
	@Override
	public Boolean visit(final Xor gate){
		attachListeners(gate);
		return true;
	}
	
	@Override
	public Boolean visit(final Xnor gate){
		attachListeners(gate);
		return true;
	}
	
	@Override
	public Boolean visit(final Not gate){
		attachListeners(gate);
		return true;
	}

	
	
	@Override
	public Boolean visit(final Sink sink){
		attachListeners(sink);
		return true;
	}

	@Override
	public Boolean visit(final Source source){
		attachListeners(source);
		return true;
	}
	




	/**
	* This attaches all needed listeners to BinaryGates
	*/
	private void attachListeners(final BinaryGate gate) {
		gate.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View view) {
				openMenu(gate);
				return true;
			}
		});
		gate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (toConnect == null){
					toConnect = gate;
				}
				else if (toConnect != gate){
					connectTo(gate);
				}
				else{
					toConnect = null;
				}
			}
		});
	}
	
	/**
	* This attaches all needed listeners to UnaryGates
	*/
	private void attachListeners(final UnaryGate gate) {
		gate.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View view) {
				openMenu(gate);
				return true;
			}
		});
		gate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (toConnect == null){
					toConnect = gate;
				}
				else if (toConnect != gate){
					connectTo(gate);
				}
				else{
					toConnect = null;
				}
			}
		});
	}
	
	/**
	* This attaches all needed listeners to a sink
	*/
	private void attachListeners(final Sink sink) {
		sink.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View view) {
				openMenu(sink);
				return true;
			}
		});
		sink.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (toConnect != null){
					connectTo(sink);
				}
				else{
					toConnect = null;
				}
			}
		});
	}
	
	/**
	* This attaches all needed listeners to a source
	*/
	private void attachListeners(final Source source) {
		source.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View view) {
				openMenu(source);
				return true;
			}
		});
		source.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (toConnect == null){
					toConnect = source;
				}
				else{
					toConnect = null;
				}
			}
		});
	}
	
	
	
	
	
	/**
	* This opens the menu for a BinaryGate to the screen and handles the input
	*/
	private void openMenu(final BinaryGate gate) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(Constants.MENU_TITLE);
		builder.setItems(Constants.BINARY_MENU_LABELS, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					changeGate(gate);
					break;
				case 1:
					delete(gate);
					break;
		    	case 2:
		    		removeInput(gate);
					break;
				}
			}
		});
		builder.show();
	}

	/**
	* This opens the menu for a UnaryGate to the screen and handles the input
	*/
	private void openMenu(final UnaryGate gate) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(Constants.MENU_TITLE);
		builder.setItems(Constants.UNARY_MENU_LABELS, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
					case 0:
						changeGate(gate);
						break;
					case 1:
						delete(gate);
						break;
					case 2:
						removeInput(gate);
						break;
				}
			}
		});
		builder.show();
	}
	
	/**
	* This opens the menu for a sink to the screen and handles the input
	*/
	private void openMenu(final Sink gate) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(Constants.MENU_TITLE);
		builder.setItems(Constants.SINK_MENU_LABELS, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
					case 0:
						removeInput(gate);
						break;
				}
			}
		});
		builder.show();
	}

	/**
	* This opens the menu for a source to the screen and handles the input
	*/
	private void openMenu(final Source gate) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(Constants.MENU_TITLE);
		builder.setItems(Constants.SOURCE_MENU_LABELS, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
					case 0:
						delete(gate);
						break;
					case 1:
						gate.invert();
						updateSink();
						break;
				}
			}
		});
		builder.show();
	}
	
	

	
	
	/**
	* This attaches the element to connect with one of the inputs into a BinaryGate.
	*/
	private void connectTo(final BinaryGate gate){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(Constants.BINARY_INPUT_CONNECTION_TITLE);
		builder.setItems(Constants.BINARY_INPUT_LABELS, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Connection newConnection;
				switch (which) {
					case 0:
						removeInput(gate, which);
						newConnection = gate.setLeft(toConnect).getY();
						newConnection.addTo(viewGroup);
						toConnect = null;
						break;
					case 1:
						removeInput(gate, which);
						newConnection = gate.setRight(toConnect).getY();
						newConnection.addTo(viewGroup);
						toConnect = null;
						break;
				}
				updateSink();
			}
		});
		builder.show();
	}

	/**
	* This attaches the element to connect with a BinaryGate.
	*/
	private void connectTo(final UnaryGate gate){
		removeInput(gate);
		Connection connection = gate.setInput(toConnect).getY();
		connection.addTo(viewGroup);
		toConnect = null;
		updateSink();
	}

	/**
	* This attaches the element to connect with a sink.
	*/
	private void connectTo(final Sink sink){
		removeInput(sink);
		Connection connection = sink.setInput(toConnect).getY();
		connection.addTo(viewGroup);
		toConnect = null;
		updateSink();
	}


	
	
	

	/**
	* This changes the type of gate. This will remove all connections with the input gate.
	*/
	private void changeGate(final Gate gate) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(Constants.GATE_TITLE);
		builder.setItems(Constants.GATE_LABELS, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				GateElement dbElement = gate.getDatabaseElement();
				// TODO remove dbElement from the database.
				dbElement.setActualGate(Constants.GATE_LABELS[which]);
				Gate newGate = factory.makeGate(dbElement, which);
				// TODO add dbElement to the database.

				RelativeLayout.LayoutParams para = new RelativeLayout.LayoutParams(gate.getWidth(), gate.getHeight());
				para.leftMargin = (int)gate.getX();
				para.topMargin = (int)gate.getY();
				
				delete(gate);
				viewGroup.addView(newGate, para);
				updateSink();
			}
		});
		builder.show();
	}
	
	
	
	
	/**
	* This will delete everything having to do with a CircuitElement
	*/
	private void delete(final CircuitElement element) {
		Collection<Connection> connections = element.getConnections();
		for (Connection connection : connections){
			connection.removeFrom(viewGroup);
		}
		
		element.unattachAll();
		viewGroup.removeView(element);
		updateSink();
	}





	/**
	* This determines which input to remove from the BinaryGate.
	*/
	private void removeInput(final BinaryGate gate){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(Constants.BINARY_INPUT_REMOVAL_TITLE);
		builder.setItems(Constants.BINARY_INPUT_LABELS, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				removeInput(gate, which);
			}
		});
		builder.show();
	}
	
	/**
	* This removes the selected input from the BinaryGate.
	*/
	private void removeInput(final BinaryGate gate, int which){
		Pair<Collection<Connection>, Connection> pair = gate.setInput(which, null);

		if (pair.getX() != null) {
			for (Connection connection : pair.getX()) {
				connection.removeFrom(viewGroup);
			}
		}
		updateSink();
	}

	/**
	* This removes the input from a UnaryGate.
	*/
	private void removeInput(final UnaryGate gate){
		Pair<Collection<Connection>, Connection> pair = gate.setInput(null);

		if (pair.getX() != null) {
			for (Connection connection : pair.getX()) {
				connection.removeFrom(viewGroup);
			}
		}
		updateSink();
	}

	/**
	* This removes the input from a Sink.
	*/
	private void removeInput(final Sink sink){
		Pair<Collection<Connection>, Connection> pair = sink.setInput(null);

		if (pair.getX() != null) {
			for (Connection connection : pair.getX()) {
				connection.removeFrom(viewGroup);
			}
		}
		updateSink();
	}
}
