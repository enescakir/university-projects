package server;

import persistence.Database;

import java.util.ArrayList;

/**
 * Communication protocol between client and server
 */
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
        this.data = Database.getJsonBuilder().toJson(data);
    }

    public static Packet fromJson(String json) {
        return Database.getJsonBuilder().fromJson(json, Packet.class);
    }

    public <T> ArrayList<T> toArrayList(Class<T> clazz) {
        return Database.getArrayList(this.data, clazz);
    }

    public <T> T toObject(Class<T> clazz) {
        return Database.getJsonBuilder().fromJson(this.data, clazz);
    }

    public String toJson() {
        return Database.getJsonBuilder().toJson(this);
    }

    public String getData() {
        return data;
    }

    public Type getType() {
        return type;
    }
}