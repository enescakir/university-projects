package com.enescakir.auctioner.Model;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Bid {
    private int point;
    private String date;
    private String username;

    public Bid(String username, int point, String date) {
        this.point = point;
        this.date = date;
        this.username = username;
    }

    public Bid(String username, int point, Date date) {
        this.point = point;
        this.username = username;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            this.date = sdf.format(date);
        } catch (Exception e) {
            this.date = "2018-12-11 11:11:11";
            Log.e("Date Parsing", e.getMessage());
        }

    }

    public String getUsername() {
        return username;
    }

    public int getPoint() {
        return point;
    }

    public String getDate() {
        return this.date;
    }

    public Date getDateObject() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf.parse(this.getDate());
        } catch (Exception e) {
            Log.e("Date Parsing", e.getMessage());
        }
        return null;
    }

    public String getHumanDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss");
        try {
            return sdf.format(this.getDateObject());
        } catch (Exception e) {
            Log.e("Date Parsing", e.getMessage());
        }
        return null;
    }

    @Override
    public String toString() {
        return Integer.toString(point) + " bid on by " + username;
    }
}
