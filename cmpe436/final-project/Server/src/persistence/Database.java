package persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import model.*;
import util.*;

import java.io.*;
import java.util.*;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;

/**
 * Accesses files, handles data races and persistence
 */
public class Database {
    // CONFIGURATIONS
    public static final String DATA_PATH = "src/persistence/data";
    public static final String AUCTION_DB = DATA_PATH + "/auctions.json";
    public static final String BID_DB_SUFFIX = DATA_PATH + "/[ID]_bids.json";
    public static final String USER_DB = DATA_PATH + "/users.json";
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static HashMap<Integer, BinarySemaphore> bidLocks = new HashMap<Integer, BinarySemaphore>();
    public static BinarySemaphore auctionLock = new BinarySemaphore();
    public static BinarySemaphore userLock = new BinarySemaphore();

    // JSON HELPERS
    public static Gson getJsonBuilder() {
        return new GsonBuilder().create();
    }

    public static <T> ArrayList<T> getArrayList(String rawJson, Class<T> clazz) {
        Type type = TypeToken.getParameterized(List.class, clazz).getType();
        ArrayList<T> data = getJsonBuilder().fromJson(rawJson, type);

        if (data != null)
            return data;

        return new ArrayList<>();
    }

    // USER METHODS
    public static ArrayList<User> getUsers() {
        return getArrayList(getContent(USER_DB), User.class);
    }

    public static User getUserByName(String username) {
        userLock.P();
        ArrayList<User> users = getUsers();
        User foundUser = null;
        for (User user : users) {
            if (username.equals(user.getName())) {
                foundUser = user;
                break;
            }
        }
        userLock.V();
        return foundUser;
    }

    public static int getUserPoint(String username) {
        userLock.P();
        ArrayList<User> users = getUsers();
        int point = -1;
        for (User user : users) {
            if (username.equals(user.getName())) {
                point = user.getPoint();
                break;
            }
        }
        userLock.V();
        return point;
    }

    public static boolean authUser(String username, String password) {
        User authUser = getUserByName(username);

        if (authUser != null) {
            return Integer.parseInt(authUser.getPassword()) == password.hashCode();
        }

        return saveUser(username, password);
    }

    public static void updateUserPoint(String username, int point) {
        userLock.P();

        ArrayList<User> users = getUsers();
        User foundUser = null;
        for (User user : users) {
            if (username.equals(user.getName())) {
                foundUser = user;
                break;
            }
        }

        if (foundUser != null) {
            foundUser.setPoint(point);
        }
        writeContent(USER_DB, getJsonBuilder().toJson(users));

        userLock.V();
    }

    public static boolean saveUser(String username, String password) {
        userLock.P();
        ArrayList<User> users = getUsers();
        boolean exist = false;

        for (User user : users) {
            if (username.equals(user.getName())) {
                exist = true;
                break;
            }
        }

        if (!exist) {
            User user = new User(username, Integer.toString(password.hashCode()), 2000);
            users.add(user);
            writeContent(USER_DB, getJsonBuilder().toJson(users));
        }
        userLock.V();

        return !exist;
    }

    // AUCTION METHODS
    public static ArrayList<Auction> getAuctions() {
        return getArrayList(getContent(AUCTION_DB), Auction.class);
    }

    public static void saveAuction(Auction auction) {
        auctionLock.P();

        ArrayList<Auction> auctions = getAuctions();
        auctions.add(auction);
        writeContent(AUCTION_DB, getJsonBuilder().toJson(auctions));

        auctionLock.V();
    }

    // BID METHODS
    public static ArrayList<Bid> getBids(Auction auction) {
        return getArrayList(getContent(auction.getFilename()), Bid.class);
    }

    public static int saveBid(Auction auction, Bid bid) {
        BinarySemaphore bidLock = new BinarySemaphore();

        if (bidLocks.containsKey(auction.getId())) {
            bidLock = bidLocks.get(auction.getId());
        } else {
            bidLocks.put(auction.getId(), bidLock);
        }
        bidLock.P();
        User user = getUserByName(bid.getUsername());
        ArrayList<Bid> bids = getBids(auction);

        int result = 0;

        if (user.getPoint() < bid.getPoint()) {
            result = -1;
        } else if (bids.size() > 0 && (bid.getPoint() <= bids.get(0).getPoint())) {
            result = -2;
        } else {
            result = 1;
            bids.add(0, bid);
            writeContent(auction.getFilename(), getJsonBuilder().toJson(bids));
            updateUserPoint(user.getName(), user.getPoint() - bid.getPoint());
        }
        bidLock.V();

        return result;
    }

    // FILE HELPERS
    public static void writeContent(String filename, String content) {
        File file = new File(filename);

        File db = new File(DATA_PATH);

        if (!db.exists())
            db.mkdir();

        try {
            PrintWriter out = new PrintWriter(file);
            out.print(content);
            out.close();
        } catch (IOException e) {
            Logger.error("Couldn't write the pages to file");
        }
    }

    public static String getContent(String filename) {
        File file = new File(filename);
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line).append("\n");
            }
            return stringBuffer.toString();
        } catch (Exception e) {
            Logger.error("File open error: " + filename);
        }
        return "";
    }

    public static void flush() {
        File db = new File(DATA_PATH);

        if (db.exists()) {
            String[] entries = db.list();
            for (String s : entries) {
                File currentFile = new File(db.getPath(), s);
                currentFile.delete();
            }
            db.delete();
        }
    }

    // DATE CONVERSION HELPERS
    public static Date stringToDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat(Database.DATE_FORMAT);
        try {
            return sdf.parse(date);
        } catch (Exception e) {
            Logger.error("String to Date: " + date);
            Logger.error(e.getMessage());
        }
        return null;
    }

    public static String dateToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(Database.DATE_FORMAT);
        try {
            return sdf.format(date);
        } catch (Exception e) {
            Logger.error("Date to String: " + date);
            Logger.error(e.getMessage());
        }
        return null;
    }
}