package com.EnesCakir;

import java.util.*;
import java.io.*;

/**
 * Created by EnesCakir on 7/27/17.
 */
public class FileManager {
    static String[] oldPages;

    public static void setOldPages(String[] olds) {
        oldPages = olds.clone();
    }

    public static String[] getPages(File file) throws  IOException{
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        if ((line = br.readLine()) != null) {
            return line.split(Constants.PAGE_DELIMETER);
        } else {
            throw new IOException();
        }
    }

    public static void writePages(File file, String[] pages){
        for (int i = 0; i < pages.length && i < oldPages.length; i++) {
            if (!pages[i].equalsIgnoreCase(oldPages[i])){
                Logger.access("Writing " + i + " page");
            }
        }
        oldPages = pages.clone();
        try ( PrintWriter out = new PrintWriter( file )){
            String data = String.join(Constants.PAGE_DELIMETER, pages);
            out.print(data);
            out.close();
        } catch (IOException e) {
            Logger.error("Couldn't write the pages to file");
        }
    }
}
