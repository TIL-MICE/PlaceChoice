package com.lckiss.placechoice.Model;

/**
 * Created by Administrator on 2017/4/22 0022.
 */

public class Location {

    private int id;
    private String name;
    private int code;
    private int higherCode;
    private String weatherId;

    public Location() {
    }

    public Location(String name, int code, int higherCode, String weatherId) {
        this.name = name;
        this.code = code;
        this.higherCode = higherCode;
        this.weatherId = weatherId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getHigherCode() {
        return higherCode;
    }

    public void setHigherCode(int higherId) {
        this.higherCode = higherId;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }
}
