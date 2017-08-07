package com.EnesCakir;

/**
 * Created by EnesCakir on 7/26/17.
 */
public class Constants {
    public static final String PAGE_DELIMETER = "&"; // "\\" is added for split regex issue.
    public static final String RECORD_DELIMETER = "!";
    public static final String FIELD_DELIMETER = "-";
    public static final int NUM_OF_FIELDS = 10;
    public static final int LENGTH_OF_NAME = 12;

    public static final int DB_SIZE = 1000000; // in bytes
    public static final int PAGE_SIZE = 1400; // in bytes

    public static final int PAGE_PER_FILE = 1000000 / 1400;
    public static final int TYPE_PER_PAGE = 10;
    public static final int RECORD_PER_PAGE = 30;

    public static final String DB_FILE = "database.txt";
    public static final String SYS_CAT_FILE = "SysCat.txt";
    public static final String FREE_SPACE = "FreeSpace";

}
