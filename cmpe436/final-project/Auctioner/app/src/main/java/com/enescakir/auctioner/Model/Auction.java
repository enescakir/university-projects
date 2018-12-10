package com.enescakir.auctioner.Model;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.*;

public class Auction {
    private int id;
    private String name;
    private String description;
    private int startPoint;
    private String date;

    public Auction() {
    }

    public Auction(int id, String name, String description, int startPoint, String date) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startPoint = startPoint;
        this.date = date;
    }

    public Auction(int id, String name, String description, String date) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getStartPoint() {
        return startPoint;
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
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy HH:mm");
        try {
            return sdf.format(this.getDateObject());
        } catch (Exception e) {
            Log.e("Date Parsing", e.getMessage());
        }
        return null;
    }

    public String getImageName() {
        String filename = "auction[ID]";
        return filename.replace("[ID]", Integer.toString(this.id));
    }

    @Override
    public String toString() {
        return "Auction #" + Integer.toString(id) + ": " + name;
    }
}
