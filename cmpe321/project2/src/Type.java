package com.EnesCakir;

import java.util.*;
import java.io.*;

/**
 * Created by EnesCakir on 7/27/17.
 */
public class Type {
    static Scanner keyboard = new Scanner(System.in);
    String name;
    boolean isEmpty;
    int nOfFields;
    String[] fields;

    public Type(String typeName, int nOfFields, String[] fields) {
        this.name = typeName;
        this.isEmpty = false;
        this.nOfFields = nOfFields;
        this.fields = fields.clone();
    }

    public Type(String typeStr) {
        String[] parts = typeStr.split(Constants.FIELD_DELIMETER);
        this.name = parts[0];
        this.isEmpty = "1".equals(parts[1]);
        this.nOfFields = Integer.valueOf(parts[2]);
        this.fields = new String[this.nOfFields];
        for (int i = 3; i < parts.length; i++) {
            this.fields[i-3] = parts[i];
        }
    }

    public String getName(){
        return this.name;
    }

    public int getNOfFields(){
        return this.nOfFields;
    }

    public String[] getFields(){
        return this.fields;
    }

    public boolean getIsEmpty(){
        return this.isEmpty;
    }
    public void setIsEmpty(boolean isEmpty){
        this.isEmpty = isEmpty;
    }

    public static void create(File file) {
        try {
            Logger.input("Enter type name");
            String typeName = keyboard.next();

            Logger.input("Enter number of fields: [1-" + Constants.NUM_OF_FIELDS + "]");
            int nOfFields = keyboard.nextInt();

            String[] fields = new String[nOfFields];
            for (int i = 0; i < nOfFields; i++) {
                Logger.input("Enter name for field " + (i+1));
                fields[i] = keyboard.next();
            }
            Type type = new Type(typeName, nOfFields, fields);

            String[] pages = FileManager.getPages(file);

            DiscDirectory discDir = new DiscDirectory(pages[0]);
            int sysAddress = discDir.getSysCatAddress();
            int saveAddress = discDir.addFile(typeName + ".txt");
            pages[0] = discDir.toString();
            FileManager.writePages(file, pages);

            RecordPage rPage = new RecordPage(pages[saveAddress]);
            rPage.pageHeader.setIsEmpty(false);
            pages[saveAddress] = rPage.toString();
            FileManager.writePages(file, pages);

            SysCatalogue sysPage = new SysCatalogue(pages[sysAddress]);
            while(sysPage.pageHeader.getPointerToNext() != 0) {
                sysAddress = sysPage.pageHeader.getPointerToNext();
                sysPage = new SysCatalogue(pages[sysAddress]);
            }

            if (sysPage.pageHeader.getNofRecords() == Constants.TYPE_PER_PAGE) {
                int freeAddress = discDir.getFreeAddress();
                pages[0] = discDir.toString();
                FileManager.writePages(file, pages);

                sysPage.pageHeader.setPointerToNext(freeAddress);
                pages[sysAddress] = sysPage.toString();
                FileManager.writePages(file, pages);


                sysPage = new SysCatalogue(pages[freeAddress]);
                sysPage.pageHeader.setIsEmpty(false);
                sysPage.pageHeader.increaseRecords();
                sysPage.records.add(type.toString());
                pages[freeAddress] = sysPage.toString();
                FileManager.writePages(file, pages);
            } else {
                sysPage.records.add(type.toString());
                sysPage.pageHeader.increaseRecords();
                pages[sysAddress] = sysPage.toString();
                FileManager.writePages(file, pages);
            }
            Logger.newline();
            Logger.info("Type \"" + typeName + "\" is added to database successfully.");
        } catch (IOException e) {
            Logger.error("Couldn't read line from text");
            System.exit(0);
        }
    }

    public static void delete(File file) {
        try {
            ArrayList<Type> types = getTypeList(file);
            Logger.newline();
            Logger.info(types.size() + " types are found");
            for (int i = 0; i < types.size(); i++) {
                Logger.print("\t[" + (i+1) + "] " + types.get(i).name);
                Logger.print("\t\t Number of Fields: " + types.get(i).nOfFields);
                Logger.print("\t\t Fields: " + String.join(" / ", types.get(i).fields));
            }

            Logger.input("Select the type that you wish to delete [1-" + types.size() + "]");
            int typeID = keyboard.nextInt();
            Type type = types.get(typeID - 1);

            String[] pages = FileManager.getPages(file);
            DiscDirectory discDir = new DiscDirectory(pages[0]);
            int sysAddress = discDir.getSysCatAddress();
            int removeAddress = discDir.removeFile(type.name + ".txt");
            pages[0] = discDir.toString();
            FileManager.writePages(file, pages);

            RecordPage rPage = new RecordPage(pages[removeAddress]);
            rPage.pageHeader.setIsEmpty(true);
            rPage.pageHeader.setNofRecords(0);
            int nextPointer = rPage.pageHeader.getPointerToNext();
            rPage.pageHeader.setPointerToNext(0);
            rPage.records.clear();
            pages[removeAddress] = rPage.toString();
            FileManager.writePages(file, pages);

            while(nextPointer != 0) {
                removeAddress = nextPointer;
                rPage = new RecordPage(pages[removeAddress]);
                rPage.pageHeader.setIsEmpty(true);
                rPage.pageHeader.setNofRecords(0);
                nextPointer = rPage.pageHeader.getPointerToNext();
                rPage.pageHeader.setPointerToNext(0);
                rPage.records.clear();
                pages[removeAddress] = rPage.toString();
                FileManager.writePages(file, pages);
            }

            SysCatalogue sysPage = new SysCatalogue(pages[sysAddress]);
            int loc = -1;
            while(loc == -1) {
                for (int i = 0; i < sysPage.records.size(); i++) {
                    if (sysPage.records.get(i).equalsIgnoreCase(type.toString())) {
                        loc = i;
                        break;
                    }
                }
                if (sysPage.pageHeader.getPointerToNext() == 0) {
                    break;
                }
                sysAddress = sysPage.pageHeader.getPointerToNext();
                sysPage = new SysCatalogue(pages[sysAddress]);
            }
            if (loc == -1) {
                Logger.error("Couldn't find type");
            } else {
                type.setIsEmpty(true);
                sysPage.records.set(loc, type.toString());
                sysPage.pageHeader.decreaseRecords();
                pages[sysAddress] = sysPage.toString();
                FileManager.writePages(file, pages);
                Logger.newline();
                Logger.info("Type \"" + type.name + "\" is deleted from database successfully.");
            }
        } catch (IOException e) {
            Logger.error("Couldn't read line from text");
            System.exit(0);
        }
    }

    public static void list(File file) {
        ArrayList<Type> types = getTypeList(file);
        Logger.newline();
        Logger.info(types.size() + " types are found");
        for (int i = 0; i < types.size(); i++) {
            Logger.print("\t[" + (i+1) + "] " + types.get(i).name);
            Logger.print("\t\t Number of Fields: " + types.get(i).nOfFields);
            Logger.print("\t\t Fields: " + String.join(" / ", types.get(i).fields));
        }
    }

    public static ArrayList<Type> getTypeList(File file) {
        ArrayList<Type> types = new ArrayList<>();
        try {
            String[] pages = FileManager.getPages(file);
            DiscDirectory discDir = new DiscDirectory(pages[0]);
            int sysAddress = discDir.getSysCatAddress();

            SysCatalogue sysPage = new SysCatalogue(pages[sysAddress]);
            for ( String str :  sysPage.records) {
                Type type = new Type(str);
                if (!type.getIsEmpty()) {
                    types.add(type);
                }
            }
            while (sysPage.pageHeader.getPointerToNext() != 0) {
                sysPage = new SysCatalogue(pages[sysPage.pageHeader.getPointerToNext()]);
                for ( String str :  sysPage.records) {
                    Type type = new Type(str);
                    if (!type.getIsEmpty()) {
                        types.add(type);
                    }
                }
            }
        } catch (IOException e) {
            Logger.error("Couldn't read line from text");
            System.exit(0);
        }
        return types;
    }

    @Override
    public String toString() {
        ArrayList<String> parts = new ArrayList<>();
        parts.add(name);
        if (isEmpty)
            parts.add("1");
        else
            parts.add("0");
        parts.add(Integer.toString(nOfFields));
        parts.addAll(Arrays.asList(fields));
        return String.join(Constants.FIELD_DELIMETER, parts);
    }
}
