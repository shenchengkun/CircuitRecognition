package org.tensorflow.demo.database;

import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

/**
 * This is used to maintain a databse of drawn gates.
 */
public class GateElement extends DatabaseElement {
    private String time, predictedGate, actualGate;
    private Bitmap drawnImage;

    /**
    * The names are pretty self evident. For the time, this is the time that the gate was drawn.
    * Also initially the actualGate will be set to the predicted gate. The actualGate can be changed
    * later by the user.
    */
    public GateElement(Bitmap drawnImage, String predictedGate, String actualGate, String time){
        this.drawnImage = drawnImage;
        this.predictedGate = predictedGate;
        this.actualGate = actualGate;
        this.time = time;
    }

    /**
    * Our inference got it wrong, therfore we need to change what the actual gate was. 
    */
    public void setActualGate(String actualGate){
        this.actualGate = actualGate;
    }

    /*
    * A bunch of getter methods.
    */
    
    public Bitmap getDrawnImage() {
        return drawnImage;
    }

    public String getActualGate() {
        return actualGate;
    }

    public String getPredictedGate() {
        return predictedGate;
    }

    public String getTime() {
        return time;
    }

    @Override
    public String getRawStatement() {
        return "(?, ?, ?, ?)";
    }

    @Override
    public void bindTo(SQLiteStatement statement) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        drawnImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        statement.bindString(1, actualGate);
        statement.bindString(2, predictedGate);
        statement.bindBlob(3, byteArray);
        statement.bindString(4, time);
    }
}
