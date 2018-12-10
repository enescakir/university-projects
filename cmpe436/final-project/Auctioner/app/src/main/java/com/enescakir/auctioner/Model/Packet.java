package com.enescakir.auctioner.Model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class Packet {
    private Type type;
    private String data;

    public enum Type {
        UNKNOWN(0), // Unknown package
        AUCTIONS(1), // Get auctions request, and return it
        BID_LISTEN(2), // Subscribe client to auction bid channel
        BID_NEW(3), // Broadcast new bid to clients
        BID_STORE(4), // Store bid and return result
        USER(5); // Store user and return save user result

        final int data;

        Type(int data) {
            this.data = data;
        }
    }

    Packet(Type type, String data) {
        this.type = type;
        this.data = data;
    }

    public Packet(Type type, Object data) {
        this.type = type;
        this.data = this.getJsonBuilder().toJson(data);
    }

    public static Packet fromJson(String json) {
        return getJsonBuilder().fromJson(json, Packet.class);
    }

    public <T> ArrayList<T> toArrayList(Class<T> clazz) {
        return getArrayList(this.data, clazz);
    }

    public <T> T toObject(Class<T> clazz) {
        return getJsonBuilder().fromJson(this.data, clazz);
    }

    public String toJson() {
        return getJsonBuilder().toJson(this);
    }

    public String getData() {
        return data;
    }

    public Type getType() {
        return type;
    }

    public static Gson getJsonBuilder() {
        return new GsonBuilder().create();
    }

    public static <T> ArrayList<T> getArrayList(String rawJson, Class<T> clazz) {
        java.lang.reflect.Type type = TypeToken.getParameterized(List.class, clazz).getType();
        ArrayList<T> data = getJsonBuilder().fromJson(rawJson, type);

        if (data != null)
            return data;

        return new ArrayList<>();
    }

}