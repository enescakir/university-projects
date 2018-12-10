package com.enescakir.auctioner.Model;

public class User {
    private String name;
    private String password;
    private int point;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public User(String name, String password, int point) {
        this.name = name;
        this.password = password;
        this.point = point;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    @Override
    public String toString() {
        return this.name + ":" + Integer.toString(this.point);
    }
}