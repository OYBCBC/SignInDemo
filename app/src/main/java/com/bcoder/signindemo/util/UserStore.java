package com.bcoder.signindemo.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/9/2.
 */

public class UserStore extends SQLiteOpenHelper {

    private static final String DBAdapter = null;



    public UserStore(Context context) {
        super(context, "UserInfoDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE user(" + "_id integer primary key autoincrement,"
                + "userid text not null,"
                + "psw text not null )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


}
