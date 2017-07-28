package com.EnesCakir.Enums;

/**
 * Created by EnesCakir on 7/27/17.
 */

public enum RecordOperation implements Operation {
    CREATE("Create a new record"),
    DELETE("Delete record"),
    RETRIEVE("Retrieve a record"),
    LIST("List all records of a type");

    private String text;

    private RecordOperation(String value) {
        this.text = value;
    }

    @Override
    public String text() {
        return this.text;
    }

};