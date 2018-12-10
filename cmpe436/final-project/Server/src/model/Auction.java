package model;

import persistence.Database;

import java.util.*;

public class Auction {
    private int id;
    private String name;
    private String description;
    private int startPoint;
    private String date;

    public Auction(int id, String name, String description, int startPoint, String date) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startPoint = startPoint;
        this.date = date;
    }

    public Auction(int id) {
        this.id = id;
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
        return Database.stringToDate(date);
    }

    public String getFilename() {
        String filename = Database.BID_DB_SUFFIX;
        return filename.replace("[ID]", Integer.toString(this.id));
    }

    @Override
    public String toString() {
        return "Auction #" + Integer.toString(id) + ": " + name;
    }
}
