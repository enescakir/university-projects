package com.EnesCakir;

import java.util.*;
import java.io.*;

/**
 * Created by EnesCakir on 7/27/17.
 */


public class Record {
    static int lastID = 0;
    static Scanner keyboard = new Scanner(System.in);

    int recordID;
    boolean isEmpty;
    String[] fields;
    Type type;

    public Record(Type type, String[] fields) {
        lastID++;
        this.recordID = lastID;
        this.isEmpty = false;
        this.type = type;
        this.fields = fields.clone();
    }

    public Record(Type type, String typeStr) {
        String[] parts = typeStr.split(Constants.FIELD_DELIMETER);
        this.recordID = Integer.valueOf(parts[0]);
        this.isEmpty = "1".equals(parts[1]);
        this.type = type;
        this.fields = new String[type.getNOfFields()];
        for (int i = 2; i < parts.length; i++) {
            this.fields[i-2] = parts[i];
        }
    }

    public boolean getIsEmpty(){
        return this.isEmpty;
    }
    public void setIsEmpty(boolean isEmpty){
        this.isEmpty = isEmpty;
    }
    public String[] getFields(){
        return this.fields;
    }

    public static void create(File file) {
        try {
            ArrayList<Type> types = Type.getTypeList(file);
            for (int i = 0; i < types.size(); i++) {
                Logger.print("\t[" + (i+1) + "] " + types.get(i).name);
                Logger.print("\t\t Number of Fields: " + types.get(i).nOfFields);
                Logger.print("\t\t Fields: " + String.join(" / ", types.get(i).fields));
            }
            Logger.input("Select the type that you wish to create new record [1-" + types.size() + "]");
            int typeID = keyboard.nextInt();
            Type type = types.get(typeID - 1);

            Logger.print("You are creating record for type " + type.getName());
            String[] fields = new String[type.getNOfFields()];
            for (int i = 0; i < type.getNOfFields(); i++) {
                Logger.input("Enter data for " + type.getFields()[i]);
                fields[i] = keyboard.next();
            }
            Record record = new Record(type, fields);

            String[] pages = FileManager.getPages(file);
            DiscDirectory discDir = new DiscDirectory(pages[0]);
            int address = discDir.getAddress(type.getName() + ".txt");

            RecordPage rPage = new RecordPage(pages[address]);
            while(rPage.pageHeader.getPointerToNext() != 0) {
                address = rPage.pageHeader.getPointerToNext();
                rPage = new RecordPage(pages[address]);
            }

            if (rPage.pageHeader.getNofRecords() == Constants.RECORD_PER_PAGE) {
                int freeAddress = discDir.getFreeAddress();
                pages[0] = discDir.toString();
                FileManager.writePages(file, pages);

                rPage.pageHeader.setPointerToNext(freeAddress);
                pages[address] = rPage.toString();
                FileManager.writePages(file, pages);


                rPage = new RecordPage(pages[freeAddress]);
                rPage.pageHeader.setIsEmpty(false);
                rPage.pageHeader.increaseRecords();
                rPage.records.add(record.toString());
                pages[freeAddress] = rPage.toString();
                FileManager.writePages(file, pages);
            } else {
                rPage.records.add(record.toString());
                rPage.pageHeader.increaseRecords();
                pages[address] = rPage.toString();
                FileManager.writePages(file, pages);
            }
            Logger.print("Record for type \"" + type.getName() + "\" is added to database successfully.");
        } catch (IOException e) {
            Logger.error("Couldn't read line from text");
            System.exit(0);
        }
    }

    public static void delete(File file) {
        try {
            Logger.newline();
            ArrayList<Type> types = Type.getTypeList(file);
            for (int i = 0; i < types.size(); i++) {
                Logger.print("\t[" + (i+1) + "] " + types.get(i).name);
                Logger.print("\t\t Number of Fields: " + types.get(i).nOfFields);
                Logger.print("\t\t Fields: " + String.join(" / ", types.get(i).fields));
            }
            Logger.input("Select the type that you wish to delete a record [1-" + types.size() + "]");
            int typeID = keyboard.nextInt();
            Type type = types.get(typeID - 1);

            Logger.newline();
            Logger.info("Records for type" + type.getName());

            ArrayList<Record> records = getRecords(file, type);
            for (int i = 0; i < records.size(); i++) {
                Record record = records.get(i);
                ArrayList<String> parts = new ArrayList<>();
                for (int j = 0; j < record.type.getNOfFields(); j++) {
                    parts.add(record.type.fields[j] + ": " + record.fields[j]);
                }
                Logger.print("\t[" + (i+1) + "] => " + String.join(" / ", parts));
            }
            Logger.input("Select the record that you wish to delete [1-" + records.size() + "]");
            int recordID = keyboard.nextInt();
            Record record = records.get(recordID - 1);
            Logger.info("Looking for: " + record.toString());
            String[] pages = FileManager.getPages(file);
            DiscDirectory discDir = new DiscDirectory(pages[0]);
            int address = discDir.getAddress(type.getName() + ".txt");
            RecordPage rPage = new RecordPage(pages[address]);
            rPage = new RecordPage(pages[address]);

            int loc = -1;
            while(loc == -1) {
                for (int i = 0; i < rPage.records.size(); i++) {
                    Logger.info("Tring for: " + rPage.records.get(i));

                    if (rPage.records.get(i).equalsIgnoreCase(record.toString())) {
                        loc = i;
                        break;
                    }
                }
                if (rPage.pageHeader.getPointerToNext() == 0) {
                    break;
                }
                address = rPage.pageHeader.getPointerToNext();
                rPage = new RecordPage(pages[address]);
            }
            if (loc == -1) {
                Logger.error("Couldn't find record");
            } else {
                record.setIsEmpty(true);
                rPage.records.set(loc, record.toString());
                rPage.pageHeader.decreaseRecords();
                pages[address] = rPage.toString();
                FileManager.writePages(file, pages);
                Logger.newline();
                Logger.info("Record is deleted from database successfully.");
            }
        } catch (IOException e) {
            Logger.error("Couldn't read line from text");
            System.exit(0);
        }

    }

    public static void retrieve(File file) {
        ArrayList<Type> types = Type.getTypeList(file);
        for (int i = 0; i < types.size(); i++) {
            Logger.print("\t[" + (i+1) + "] " + types.get(i).name);
            Logger.print("\t\t Number of Fields: " + types.get(i).nOfFields);
            Logger.print("\t\t Fields: " + String.join(" / ", types.get(i).fields));
        }
        Logger.input("Select the type that you wish to retrieve record [1-" + types.size() + "]");
        int typeID = keyboard.nextInt();
        Type type = types.get(typeID - 1);

        Logger.print("Primary key for type " + type.getName() + " is " + type.getFields()[0]);
        Logger.input("Enter " + type.getFields()[0] + " for retrieve record");
        String pk = keyboard.next();

        ArrayList<Record> records = getRecords(file, type);
        Record found = null;
        for (Record record : records) {
            if (pk.equalsIgnoreCase(record.fields[0])){
                found = record;
                break;
            }
        }
        if (found != null) {
            Logger.print("The record is found");
            Logger.print("\t[" + found.recordID + "] Type " + found.type.getName());
            ArrayList<String> parts = new ArrayList<>();
            for (int j = 0; j < found.type.getNOfFields(); j++) {
                parts.add(found.type.fields[j] + ": " + found.fields[j]);
            }
            Logger.print("\t\t" + String.join(" / ", parts));
        } else {
            Logger.error("The record that has given primary key couldn't found");
        }
    }

    public static void list(File file) {
        Logger.newline();
        ArrayList<Type> types = Type.getTypeList(file);
        for (int i = 0; i < types.size(); i++) {
            Logger.print("\t[" + (i+1) + "] " + types.get(i).name);
            Logger.print("\t\t Number of Fields: " + types.get(i).nOfFields);
            Logger.print("\t\t Fields: " + String.join(" / ", types.get(i).fields));
        }
        Logger.input("Select the type that you wish to list records [1-" + types.size() + "]");
        int typeID = keyboard.nextInt();
        Type type = types.get(typeID - 1);

        Logger.newline();
        Logger.info("Records for type " + type.getName());
        ArrayList<Record> records = Record.getRecords(file, type);
        for (int i = 0; i < records.size(); i++) {
            Record record = records.get(i);
            ArrayList<String> parts = new ArrayList<>();
            for (int j = 0; j < record.type.getNOfFields(); j++) {
                parts.add(record.type.fields[j] + ": " + record.fields[j]);
            }
            Logger.print("\t[" + record.recordID + "] => " + String.join(" / ", parts));
        }
    }

    public static ArrayList<Record> getRecords(File file, Type type) {
        ArrayList<Record> records = new ArrayList<>();
        try {
            String[] pages = FileManager.getPages(file);
            DiscDirectory discDir = new DiscDirectory(pages[0]);
            int address = discDir.getAddress(type.getName() + ".txt");
            RecordPage rPage = new RecordPage(pages[address]);
            for ( String str :  rPage.records) {
                Record record = new Record(type, str);
                if (!record.getIsEmpty()) {
                    records.add(record);
                }
            }

            while (rPage.pageHeader.getPointerToNext() != 0) {
                rPage = new RecordPage(pages[rPage.pageHeader.getPointerToNext()]);
                for ( String str :  rPage.records) {
                    Record record = new Record(type, str);
                    if (!record.getIsEmpty()) {
                        records.add(record);
                    }
                }
            }
        } catch (IOException e) {
            Logger.error("Couldn't read line from text");
            System.exit(0);
        }
        return records;
    }


    @Override
    public String toString() {
        ArrayList<String> parts = new ArrayList<>();
        parts.add(Integer.toString(recordID));
        if (isEmpty)
            parts.add("1");
        else
            parts.add("0");
        parts.addAll(Arrays.asList(fields));
        return String.join(Constants.FIELD_DELIMETER, parts);
    }
}
