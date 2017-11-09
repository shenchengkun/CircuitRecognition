package org.tensorflow.demo.database;

import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;

/**
 * Basic blueprint for using SQLite.
 */
public abstract class DatabaseElement {
    public abstract String getRawStatement();
    public abstract void bindTo(SQLiteStatement statement);
    public abstract Bitmap getDrawnImage();
    public abstract String getPredictedGate();
}
