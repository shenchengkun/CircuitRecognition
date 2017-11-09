package org.tensorflow.demo.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import org.tensorflow.demo.database.DatabaseElement;

/**
 * Created by Quoc Nguyen on 13-Dec-16.
 */

public class GateDatabase extends SQLiteOpenHelper {
    private String tableName, keyName;
    public GateDatabase(Context context, String name, String tableName, String keyName, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

        this.tableName = tableName;
        this.keyName = keyName;
    }

    public void queryData(String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    public void insertData(DatabaseElement element){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO " + tableName +  " VALUES " + element.getRawStatement();

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        element.bindTo(statement);

        statement.executeInsert();
    }

    public Integer deleteData (String key) {
        SQLiteDatabase database = this.getWritableDatabase();
        return database.delete(tableName, keyName + " = ?", new String[] {key});
    }

    public Cursor getData(String sql, String date){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, new String[] {date});
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {}

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {}
}
