package util;

/**
 * Log helper
 */
public class Logger {
    public static void error(String text) {
        System.out.println("[Error] " + text);
    }

    public static void thread(String text) {
        System.out.println("[Thread] " + text);
    }

    public static void server(String text) {
        System.out.println("[Server] " + text);
    }

    public static void client(String text) {
        System.out.println("[Client] " + text);
    }
}
