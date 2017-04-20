package visualization;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

import acm.graphics.GObject;

/**
 * Represents players of game. 
 * @author Enes Cakir
 *
 */
public class User {
	private String name; // Name of the player
	private double score; // Score that the player has
	private int round; // Round that player on
	
	/**
	 * Constructs new user with given name 
	 * @param pName name of the new player
	 */
	public User(String pName){
		name = pName;
		score = 0;
		round = 1;
	}
	
	/**
	 * Constructs new user with given name, score and round.
	 * Generally used for parsing high scores from text file.
	 * 
	 * @param pName given name of the player
	 * @param pScore given score that the player has
	 * @param pRound given round that the player on
	 */
	public User(String pName, int pScore, int pRound){
		name = pName;
		score = pScore;
		round = pRound;
	}
	
	/**
	 * Saves new score to highscores text file.
	 * @throws FileNotFoundException couldn't find highscore.txt
	 */
	public void save() throws FileNotFoundException{
		// Get old records.
		ArrayList<User> highscores = parseHighscores();
		
		// Add this turn score.
		highscores.add(this);
		
		// Sorts highscores based on score
	    Collections.sort(highscores, new Comparator<User>() {
	        @Override public int compare(User user1, User user2) {
	            return user2.getScore() - user1.getScore();
	        }

	    });
		
		File hiFile = new File("highscores.txt");
		PrintStream out = new PrintStream(hiFile);
		
		// Add table header
		out.printf("    Name \t\t\t \tScore\t \tRound");
		
		// Write max 10 elements
		for (int i = 0; i < Math.min (highscores.size(), 10 ); i++) {
			User row = highscores.get(i);
			out.printf("\n%3d %s \t\t\t \t%d\t \t%d", i+1 , row.getName(), row.getScore(), row.round);

		}
		
	}
	

	/**
	 * Parses highscores datas from file to application.
	 * @return listed old highscores.
	 */
	private ArrayList<User> parseHighscores(){
		ArrayList<User> highscores = new ArrayList<User>();
		try {
			File file = new File("highscores.txt");
			Scanner scanner = new Scanner(file);
			scanner.nextLine();
			while(scanner.hasNextLine()){
				
				// Get line by line
				int rank = scanner.nextInt();
				String name = scanner.next();
				while(scanner.hasNext() && !scanner.hasNextInt())
					name = name + " " + scanner.next();
				int score = scanner.nextInt();
				int round = scanner.nextInt();
				
				// Construct new user with lined data
				User newRow = new User(name, score, round);
				highscores.add(newRow);
			}

		} catch (Exception e) {
			System.out.println("It's the first game.");
		}
		return highscores;
	}
	
	/**
	 * Returns this user's name
	 * @return name that this user has.
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Returns this user's current score
	 * @return score that this user has.
	 */
	public int getScore(){
		return (int) score;
	}
	
	/**
	 * Returns this user's current round
	 * @return round that this user on it.
	 */
	public int getRound(){
		return round;
	}
	
	/**
	 * Increases user score based on round number.
	 */
	public void increaseScore(){
		score += round/10.0 ;
	}

	/**
	 * Moves user to next round.
	 */
	public void nextRound(){
		round++;
		score += round * 200;
	}
}
