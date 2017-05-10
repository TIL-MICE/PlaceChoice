package com.lckiss.placechoice.Util;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by root on 17-4-19.
 */

public class WeatherDBhelper extends SQLiteOpenHelper{

    public WeatherDBhelper(Context context) {
        super(context,"weather.db", null, 1);
        //context 数据库游标上下文
        //null 游标工厂一般为空
        //1:数据库版本
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE if not exists province("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "provincename TEXT,"+
                "provincecode INTEGER)");
        db.execSQL("CREATE TABLE if not exists city("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "cityname TEXT,"+
                "citycode INTEGER,"+
                "provincecode INTEGER)");
        db.execSQL("CREATE TABLE if not exists county("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "weatherid TEXT,"+
                "countyname TEXT,"+
                "citycode INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
