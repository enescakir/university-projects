package com.EnesCakir.Enums;

/**
 * Created by EnesCakir on 7/27/17.
 */

public enum TypeOperation implements Operation {
    CREATE("Create a new type"),
    DELETE("Delete type"),
    LIST("List all types");

    private String text;

    private TypeOperation(String value) {
        this.text = value;
    }

    @Override
    public String text() {
        return this.text;
    }

};