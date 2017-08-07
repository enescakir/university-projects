package com.EnesCakir.Enums;

/**
 * Created by EnesCakir on 7/27/17.
 */

public enum FileOperation implements Operation {
    OVERWRITE("Overwrite old database file with new one (You lose your data)"),
    USE_OLD("Use old database file");

    private String text;

    private FileOperation(String value) {
        this.text = value;
    }

    @Override
    public String text() {
        return this.text;
    }

};