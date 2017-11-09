package org.tensorflow.demo.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * A Usful class for holding all the constants used in the project.
 */
public class Constants {
	// This is the width of the line that is drawn by the user when drawing a Gate.
	public static final int DRAW_WIDTH = 25;

	// This is the width of the gate image placed on the screen
	public static final int GATE_WIDTH = 100;
	// This is the height of the gate image placed on the screen
	public static final int GATE_HEIGHT = 60;
	// This is the width of the connection line
	public static final int LINE_WIDTH = 2;
	// This is the offset used for adjusting for binary gate inputs.
	public static final int LINE_OFFSET = 30;

	public static final String MENU_TITLE = "Menu";
   	public static final String[] BINARY_MENU_LABELS = new String[]{
            "Change Gate",
            "Delete Gate",
	    "Remove Input"
    	};
	public static final String[] UNARY_MENU_LABELS = new String[]{
			"Change Gate",
			"Delete Gate",
			"Remove Input"
	};
	public static final String[] SINK_MENU_LABELS = new String[]{
			"Remove Input"
	};
	public static final String[] SOURCE_MENU_LABELS = new String[]{
			"Delete Source",
			"Invert Value"
	};



	public static final String GATE_TITLE = "Which gate did you mean?";
	public static final String BINARY_INPUT_CONNECTION_TITLE = "Which input did you want to connect?";
	public static final String BINARY_INPUT_REMOVAL_TITLE = "Which input did you want to remove?";
	public static final String[] GATE_LABELS = new String[]{
            "And",
            "Nand",
            "Nor",
            "Not",
            "Or",
            "Xnor",
            "Xor"
	};
	public static final String[] BINARY_INPUT_LABELS = new String[]{
	    "Top",
	    "Bottom"
	};

	
	
	// Element IDs
	public static final int AND = 0;
	public static final int NAND = AND + 1;
	public static final int NOR = NAND + 1;
	public static final int NOT = NOR + 1;
	public static final int OR = NOT + 1;
	public static final int XNOR = OR + 1;
	public static final int XOR = XNOR + 1;

	public static final int SINK_ON = XOR + 1;
	public static final int SINK_OFF = SINK_ON + 1;
	public static final int SINK_UNDEFINED = SINK_OFF + 1;
	public static final int SOURCE_ON = SINK_UNDEFINED + 1;
	public static final int SOURCE_OFF = SOURCE_ON + 1;
	public static final int SINK = SOURCE_OFF + 1;
	public static final int SOURCE = SINK + 1;

	// Number of different possible classifications from the classifier
	public static final int NUM_CLASSES = 7;
	// Classifier's image dimensions
	public static final int INPUT_SIZE = 28;

	// minimum size needed to run a regression
	public static final int MIN_SIZE = 100;
	// maximum size to use to create a source
	public static final int SOURCE_SIZE = 3 * DRAW_WIDTH;

	public static final String ASSET_PATH = "file:///android_asset/";

	public static final String MODEL_NAME = "model.pb";
	public static final String MODEL_PATH = ASSET_PATH + MODEL_NAME;

	// Location of Gate names, TODO, just use the previous constant in the code
	public static final String LABELS_NAME = "GateLabels.txt";
	public static final String LABELS_PATH = ASSET_PATH + LABELS_NAME;

	// Model input and output names
	public static final String INPUT_NAME = "input:0";
	public static final String OUTPUT_NAME = "output:0";

	// Database constants
	public static final String GATE_TABLE_NAME = "GATES";
	public static final String GATE_KEY_NAME = "time";
	public static final String CREATE_GATE_TABLE = "CREATE TABLE IF NOT EXISTS " + GATE_TABLE_NAME + "(actualGate VARCHAR, predictedGate VARCHAR, drawnImage BLOB, " + GATE_KEY_NAME + " VARCHAR PRIMARY KEY)";

	public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
}
