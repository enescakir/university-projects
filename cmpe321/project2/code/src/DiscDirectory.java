package com.EnesCakir;

import sun.rmi.runtime.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created by EnesCakir on 7/27/17.
 */
public class DiscDirectory extends Page {

    public DiscDirectory() {
        super();
    }

    public DiscDirectory(PageHeader pageHeader) {
        super(pageHeader);
    }

    public DiscDirectory(String pageString) {
        super(pageString);
    }


    public int addFile(String fileName) {
        int address = this.getFreeAddress();
        records.add(fileName + Constants.FIELD_DELIMETER + address);
        return address;
    }

    public void addFile(String fileName, int pageID) {
        records.add(fileName + Constants.FIELD_DELIMETER + pageID);
    }

    public int removeFile(String fileName) {
        int address = 0;
        for (int i = 0; i < records.size(); i++) {
            String[] fields = records.get(i).split(Constants.FIELD_DELIMETER);
            if (fields[0].equalsIgnoreCase(fileName)) {
                address = Integer.valueOf(fields[1]);
                records.remove(i);
                break;
            }
        }
        return address;
    }

    public int getAddress(String fileName) {
        for (String record : records) {
            String[] fields = record.split(Constants.FIELD_DELIMETER);
            if (fields[0].equalsIgnoreCase(fileName)) {
                return Integer.valueOf(fields[1]);
            }
        }
        return 0;
    }

    public int getSysCatAddress() {
        return this.getAddress(Constants.SYS_CAT_FILE);
    }

    public int getFreeAddress() {
        int freeAddress = this.getAddress(Constants.FREE_SPACE);
        this.setFreeAddress(freeAddress + 1);
        return freeAddress;
    }

    public void setFreeAddress(int address) {
        for (int i = 0; i < records.size(); i++) {
            String[] fields = records.get(i).split(Constants.FIELD_DELIMETER);
            if (fields[0].equalsIgnoreCase(Constants.FREE_SPACE)) {
                records.set(i, fields[0] + Constants.FIELD_DELIMETER + address);
            }
        }
    }
}