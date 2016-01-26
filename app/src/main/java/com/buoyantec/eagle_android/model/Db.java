package com.buoyantec.eagle_android.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kang on 16/1/26.
 */
public class Db extends SQLiteOpenHelper{
    public Db(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "eagle.db", null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
