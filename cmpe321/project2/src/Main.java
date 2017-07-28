package com.EnesCakir;
import com.EnesCakir.Enums.*;

import java.util.*;
import java.io.*;

public class Main {
    static File dbFile;
    static Scanner keyboard = new Scanner(System.in);

    public static void main(String[] args) {
        Logger.print("");
        Logger.print("======= WELCOME TO CAKIR'S STORAGE MANAGER =======");
        checkDatabaseFile();
        while (true) {
            CategoryOperation category = (CategoryOperation) chooseOperation(CategoryOperation.values(), "Please select an operation");
            switch (category) {
                case TYPE:
                    handleTypeOperation();
                    break;
                case RECORD:
                    handleRecordOperation();
                    break;
                case QUIT:
                    Logger.print("Thanks for using our storage manager");
                    System.exit(0);
                    break;
            }
        }
    }

    public static void checkDatabaseFile() {
        File db = new File(Constants.DB_FILE);
        if (db.exists()) {
            FileOperation choice = (FileOperation) chooseOperation(FileOperation.values(), "You have old database file. Please select an operation");
            switch (choice) {
                case OVERWRITE:
                    createDatabaseFile();
                    break;
                case USE_OLD:
                    dbFile = db;
                    Logger.print("");
                    Logger.info("Old database file is used");
                    break;
            }
        } else {
            Logger.print("");
            Logger.warning("Old database file couldn't found");
            createDatabaseFile();
        }
        try {
            FileManager.setOldPages(FileManager.getPages(dbFile));
        } catch (IOException e) {
            Logger.error("Couldn't gel old pages");
        }

    }

    public static void createDatabaseFile() {
        dbFile = new File(Constants.DB_FILE);
        try ( PrintWriter out = new PrintWriter(dbFile)){
            // First page always is Disc Directory file
            DiscDirectory discDirectory = new DiscDirectory();
            discDirectory.addFile(Constants.FREE_SPACE, 2);
            discDirectory.addFile(Constants.SYS_CAT_FILE, 1);
            out.print(discDirectory);
            int totalPageCount = Constants.PAGE_PER_FILE;
            // Add empty page with ID
            for(int i = 1; i < totalPageCount; i++) {
                out.print(Constants.PAGE_DELIMETER);
                out.print(i); // Page ID
                out.print(Constants.FIELD_DELIMETER);
                out.print(""); // Pointer to next page
                out.print(Constants.FIELD_DELIMETER);
                out.print("0"); // Number of records
                out.print(Constants.FIELD_DELIMETER);
                if (i == 1) {
                    out.print("0"); // Sys is full

                } else {
                    out.print("1"); // Empty page
                }
            }
            out.close();
        } catch (IOException e) {
            Logger.print("exception");
        }
        Logger.info(Constants.DB_FILE + " is created.");
    }

    public static boolean isBetween(int number, int min, int max) {
        return (number > min && number < max);
    }

    public static Operation chooseOperation(Operation[] ops, String message) {
        Logger.print("");
        int choice = 0;
        while (true) {
            for(int i = 0; i < ops.length; i++) {
                Logger.print("\t[" + (i + 1) + "] " + ops[i].text());
            }
            Logger.input(message);
            choice = keyboard.nextInt();
            if (isBetween(choice, 0, ops.length + 1)){
                return ops[choice - 1];
            } else {
                Logger.warning("Please select operation between [1-" + ops.length + "]");
            }
        }
    }

    public static void handleTypeOperation() {
        TypeOperation operation = (TypeOperation) chooseOperation(TypeOperation.values(), "Please select an type operation");
        switch (operation) {
            case CREATE:
                Type.create(dbFile);
                break;
            case DELETE:
                Type.delete(dbFile);
                break;
            case LIST:
                Type.list(dbFile);
                break;
        }
    }

    public static void handleRecordOperation() {
        RecordOperation operation = (RecordOperation) chooseOperation(RecordOperation.values(), "Please select an record operation");
        switch (operation) {
            case CREATE:
                Record.create(dbFile);
                break;
            case DELETE:
                Record.delete(dbFile);
                break;
            case RETRIEVE:
                Record.retrieve(dbFile);
                break;
            case LIST:
                Record.list(dbFile);
                break;
        }

    }
}