package com.EnesCakir;

/**
 * Created by EnesCakir on 7/27/17.
 */
public class Logger {

    public static void print(String text) {
        System.out.println(text);
    }

    public static void info(String text) {
        System.out.println("[Info] " + text);
    }

    public static void warning(String text) {
        System.out.println("[Warning] " + text);
    }

    public static void error(String text) {
        System.out.println("[Error] " + text);
    }

    public static void access(String text) {
        System.out.println("[Access] " + text);
    }

    public static void input(String text) {
        System.out.print("[Input] " + text + ": ");
    }

    public static void newline() {
        System.out.print("");
    }

}
