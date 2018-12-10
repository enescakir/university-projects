package persistence;

import model.*;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Generates dummy data for demo
 */
public class Seeder {
    public static void main(String[] args) {
        Seeder.init();
    }

    public static void init() {
        Database.flush();
        Auction auction1 = new Auction(1, "Mona Lisa Auction", "The Mona Lisa is a half-length portrait painting by the Italian Renaissance artist Leonardo da Vinci.", 100, Database.dateToString(getRandomDate()));
        Auction auction2 = new Auction(2, "The Scream Auction", "The works show a figure with an agonized expression against a landscape with a tumultuous orange sky.", 20, Database.dateToString(getRandomDate()));
        Auction auction6 = new Auction(6, "Macintosh Auction", "The Macintosh is a family of personal computers designed, manufactured, and sold by Apple Inc. since January 1984", 400, Database.dateToString(getRandomDate()));
        Auction auction5 = new Auction(5, "Very Old Stamp Auction", "19th Century classics", 400, Database.dateToString(getRandomDate()));
        Auction auction3 = new Auction(3, "Wheat Field with Cypresses", "Very The works were inspired by the view from the window at the asylum towards the Alpilles mountains.", 30, Database.dateToString(getRandomDate()));
        Auction auction7 = new Auction(7, "iPhone (1st generation)", "The iPhone is the first smartphone model designed and marketed by Apple Inc.", 400, Database.dateToString(getRandomDate()));
        Auction auction4 = new Auction(4, "Byzantine Coin Auction", "A very popular denarius featuring the tools of minting coins.", 400, Database.dateToString(getRandomDate()));
        Auction auction8 = new Auction(8, "Apple Newton Auction", "The Newton is a series of personal digital assistants (PDA) developed and marketed by Apple Computer, Inc.", 400, Database.dateToString(getRandomDate()));

        Database.saveAuction(auction1);
        Database.saveAuction(auction2);
        Database.saveAuction(auction3);
        Database.saveAuction(auction4);
        Database.saveAuction(auction5);
        Database.saveAuction(auction6);
        Database.saveAuction(auction7);
        Database.saveAuction(auction8);

        Database.saveUser("Enes", "Enes123");
        Database.saveUser("Ahmet", "Ahmet123");
        Database.saveUser("Mehmet", "Mehmet123");
        Database.saveUser("Osman", "Osman123");

        Database.saveBid(auction1, new Bid("Enes", 10, getRandomDate(false)));
        Database.saveBid(auction1, new Bid("Enes", 26, getRandomDate(false)));
        Database.saveBid(auction1, new Bid("Ahmet", 40, getRandomDate(false)));

        Database.saveBid(auction2, new Bid("Osman", 10, getRandomDate(false)));
        Database.saveBid(auction2, new Bid("Mehmet", 20, getRandomDate(false)));
        Database.saveBid(auction2, new Bid("Enes", 24, getRandomDate(false)));

        Database.saveBid(auction3, new Bid("Ahmet", 75, getRandomDate(false)));
        Database.saveBid(auction3, new Bid("Osman", 86, getRandomDate(false)));
        Database.saveBid(auction3, new Bid("Mehmet", 96, getRandomDate(false)));

        Database.saveBid(auction4, new Bid("Enes", 34, getRandomDate(false)));
        Database.saveBid(auction4, new Bid("Mehmet", 67, getRandomDate(false)));
        Database.saveBid(auction4, new Bid("Ahmet", 85, getRandomDate(false)));
    }

    private static Date getRandomDate() {
        return getRandomDate(true);
    }

    private static Date getRandomDate(boolean isFuture) {
        Random rand = new Random();
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        if (isFuture) {
            c.add(Calendar.DATE, rand.nextInt(20));
        } else {
            c.add(Calendar.DATE, -1 * rand.nextInt(20));
        }
        c.add(Calendar.DATE, rand.nextInt(20));
        c.add(Calendar.HOUR, rand.nextInt(24));
        c.add(Calendar.MINUTE, rand.nextInt(60));
        c.add(Calendar.SECOND, rand.nextInt(60));
        return c.getTime();
    }
}
