package model;

import persistence.Database;

import java.util.*;

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
        this.date = Database.dateToString(date);
        this.username = username;
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
        return Database.stringToDate(this.date);
    }

    @Override
    public String toString() {
        return Integer.toString(point) + " bid on by " + username;
    }

}
