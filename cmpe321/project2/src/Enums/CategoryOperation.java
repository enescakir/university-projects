package com.EnesCakir.Enums;

/**
 * Created by EnesCakir on 7/27/17.
 */

public enum CategoryOperation implements Operation {
    TYPE("Type operations (DDL)"),
    RECORD("Record operations (DML)"),
    QUIT("Quit the program");

    private String text;

    private CategoryOperation(String value) {
        this.text = value;
    }

    @Override
    public String text() {
        return this.text;
    }

};