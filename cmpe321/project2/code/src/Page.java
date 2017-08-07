package com.EnesCakir;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by EnesCakir on 7/27/17.
 */
public class Page {
    PageHeader pageHeader;
    ArrayList<String> records = new ArrayList<>();

    public Page() {
        this.pageHeader = new PageHeader(0, 0, 0, false);
    }

    public Page(PageHeader pageHeader) {
        this.pageHeader = pageHeader;
    }

    public Page(String pageString) {
        String[] parts = pageString.split(Constants.RECORD_DELIMETER);
        this.pageHeader = new PageHeader(parts[0]);
        Logger.access("Reading " + this.pageHeader.getPageID() + " page");
        this.records = new ArrayList<>(Arrays.asList(Arrays.copyOfRange(parts, 1, parts.length)));
    }

    @Override
    public String toString() {
        String result = pageHeader.toString();
        if (!records.isEmpty()) {
            result += Constants.RECORD_DELIMETER;
            result += String.join(Constants.RECORD_DELIMETER, records);
        }
        return result;
    }
}
