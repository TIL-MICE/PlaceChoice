package com.lckiss.placechoice.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * Created by root on 17-4-19.
 */

public class SettingSave {
//读取上次离开时保存的设置
    public static Bundle getSelect(Context context){
        SharedPreferences sp=context.getSharedPreferences("data",context.MODE_PRIVATE);
        Bundle bundle=new Bundle();
        bundle.putInt("selectLevel",sp.getInt("selectLevel",-1));
        bundle.putInt("provinceId",sp.getInt("provinceId",-1));
        bundle.putInt("cityId",sp.getInt("cityId",-1));
        bundle.putInt("id",sp.getInt("id",-1));
        return bundle;
    }
//保存用户选择的层级
    public static boolean saveSelectLevel(Context context,int selectLevel){
        SharedPreferences sp=context.getSharedPreferences("data",context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putInt("selectLevel",selectLevel);
        editor.commit();
        return true;
    }
    //保存用户选择的省级
    public static boolean saveSelectProvince(Context context,int provinceId){
        SharedPreferences sp=context.getSharedPreferences("data",context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putInt("provinceId",provinceId);
        editor.commit();
        return true;
    }
    //保存用户选择的市级
    public static boolean saveSelectCity(Context context,int cityId){
        SharedPreferences sp=context.getSharedPreferences("data",context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putInt("cityId",cityId);
        editor.commit();
        return true;
    }
    //保存用户选择的县级
    public static boolean saveSelectCounty(Context context,int CountyId){
        SharedPreferences sp=context.getSharedPreferences("data",context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putInt("CountyId",CountyId);
        editor.commit();
        return true;
    }

}
