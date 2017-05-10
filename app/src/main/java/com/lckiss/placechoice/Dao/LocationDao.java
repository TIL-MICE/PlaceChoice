package com.lckiss.placechoice.Dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.lckiss.placechoice.Model.Location;
import com.lckiss.placechoice.Util.WeatherDBhelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 17-4-19.
 */

public class LocationDao {
    private WeatherDBhelper helper;

    public LocationDao(Context context){
        this.helper=new WeatherDBhelper(context);
    }

    public void addProvince(Location province){
        SQLiteDatabase db=helper.getWritableDatabase();
        db.execSQL("insert into province (provincename,provincecode) values(?,?)",
                new Object[]{province.getName(),province.getCode()});
        db.close();
    }

    public void addCity(Location city){
        SQLiteDatabase db=helper.getWritableDatabase();
        db.execSQL("insert into city (cityname,citycode,provincecode) values(?,?,?)",
                new Object[]{city.getName(),city.getCode(),city.getHigherCode()});
        db.close();
    }

    public void addCounty(Location county){
        SQLiteDatabase db=helper.getWritableDatabase();
        db.execSQL("insert into county (countyname,weatherid,citycode) values(?,?,?)",
                new Object[]{county.getName(),county.getWeatherId(),county.getHigherCode()});
        db.close();
    }



    public List<Location> findProvince(){
        SQLiteDatabase db=helper.getReadableDatabase();
        List<Location> provinces=new ArrayList<Location>();
//        //参数分别为：表名，查询列名，查询条件，条件值，分组条件，having条件，排序方式
//        Cursor cursor=db.query("province",null,null,null,null,null,null);
        Cursor cursor = db.rawQuery("select provincename,provincecode from province",null);
        while(cursor.moveToNext()){
            String provincename=cursor.getString(cursor.getColumnIndex("provincename"));
            int provincecode=cursor.getInt(cursor.getColumnIndex("provincecode"));
            provinces.add(new Location(provincename,provincecode,0,null));
        }
        cursor.close();
        db.close();
        return provinces;
    }

    public List<Location> findCity(int provinceCode){
        SQLiteDatabase db=helper.getReadableDatabase();
        List<Location> cities=new ArrayList<Location>();
        Cursor cursor = db.rawQuery("select cityname,citycode from city where provincecode=?", new String[]{String.valueOf(provinceCode)});
        while(cursor.moveToNext()){
            String cityname=cursor.getString(cursor.getColumnIndex("cityname"));
            int citycode=cursor.getInt(cursor.getColumnIndex("citycode"));
            cities.add(new Location(cityname,citycode,0,null));
        }
        cursor.close();
        db.close();
        return cities;
    }

    public List<Location> findCounty(int cityCode){
        SQLiteDatabase db=helper.getReadableDatabase();
        List<Location> counties=new ArrayList<Location>();
        //参数分别为：表名，查询列名，查询条件，条件值，分组条件，having条件，排序方式
        Cursor cursor = db.rawQuery("select countyname from county where citycode=?", new String[]{String.valueOf(cityCode)});
        while(cursor.moveToNext()){
            String countyname=cursor.getString(cursor.getColumnIndex("countyname"));
            counties.add(new Location(countyname,0,0,null));
        }
        cursor.close();
        db.close();
        return counties;
    }
}
