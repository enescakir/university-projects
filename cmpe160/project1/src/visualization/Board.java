package visualization;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JFrame;

import acm.graphics.GCanvas;
import acm.graphics.GCompound;
import acm.graphics.GImage;
import acm.graphics.GLabel;
import acm.graphics.GObject;
import acm.graphics.GRect;
import acm.graphics.GTurtle;
/**
 * Performs UI operations of game.
 * @author Enes Cakir
 *
 */
public class Board {

	private static final int TURTLE_WIDTH = 80;
	private static final int INFO_LABEL_HEIGHT = 90;
	private static final String TURTLE_IMAGE_PATH = "turtle.png";
	private static final String BACKGROUND_IMAGE_PATH = "asfalt.jpg";

	public JFrame frame;
	private GCanvas canvas;
	private GImage turtle;
	private GImage background;
	private GLabel scoreLabel; // Bold text part of score label
	private GLabel roundLabel; // Bold text part of round label
	private GLabel scoreLabelNumber; // Plain number part of score label
	private GLabel roundLabelNumber; // Plain number part of round label
	private GLabel messageLabel; // Label for giving message to player

	private ArrayList<GObject> objects = new ArrayList<GObject>();

	/**
	 * Constructs new board
	 * @param boardName name of the board
	 * @param width board's width
	 * @param height board's height
	 */
	public Board(String boardName, int width, int height) {
		setCanvas(boardName, width, height);
		setBackground();
		addTurtle();
		addGameInfoLabels();

	}

	/**
	 * Sets board's canvas
	 * @param boardName top bar text of the frame 
	 * @param width frame width
	 * @param height frame height
	 */
	private void setCanvas(String boardName, int width, int height) {
		frame = new JFrame(boardName);
		frame.setSize(width, height);
		canvas = new GCanvas();
		frame.getContentPane().add(canvas);

		frame.setVisible(true);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				System.exit(0);
			}
		});
		addKeyBoardListener();
	}

	/**
	 * Adds listeners for keyboard events.
	 */
	private void addKeyBoardListener() {
		canvas.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent event) {
				
				int keyCode = event.getKeyCode();
				if (keyCode == KeyEvent.VK_UP) {
					turtle.move(0, -10);
				} else if (keyCode == KeyEvent.VK_DOWN) {
					turtle.move(0, 10);
				} else if (keyCode == KeyEvent.VK_RIGHT) {
					turtle.move(10, 0);
				} else if (keyCode == KeyEvent.VK_LEFT) {
					turtle.move(-10, 0);
				}
			}
		});
	}

	/**
	 * Adds turtle to middle-bottom of board.
	 */
	private void addTurtle() {
		turtle = new GImage(TURTLE_IMAGE_PATH);
		turtle.setSize(TURTLE_WIDTH,TURTLE_WIDTH);
		// turtle = new GTurtle();
		// turtle.setDirection(90);
		turtle.setLocation(frame.getWidth() / 2, frame.getHeight() - TURTLE_WIDTH - INFO_LABEL_HEIGHT);
		canvas.add(turtle);
		System.out.println("added");
		turtle.sendToFront();
	}

	/**
	 * Sets background image.
	 * Push up background for info labels.
	 */
	private void setBackground() {
		background = new GImage(BACKGROUND_IMAGE_PATH);
		background.setSize(frame.getWidth(), frame.getHeight() - INFO_LABEL_HEIGHT);
		canvas.add(background);
		background.sendBackward();
	}

	/**
	 * Stops game for given millisecs.
	 * @param millisecs milliseconds to wait game.
	 */
	public void waitFor(long millisecs) {
		try {
			Thread.sleep(millisecs);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adds object to board.
	 * @param g object to add
	 */
	public void addObject(GObject g) {
		canvas.add(g);
		objects.add(g);
		turtle.sendToFront();
	}
	
	/**
	 * Removes object from board.
	 * @param g object to remove
	 */
	public void removeObject(GObject g) {
		canvas.remove(g);
		objects.remove(g);
		turtle.sendToFront();
	}

	/**
	 * Adds game info label to board.
	 * 
	 */
	public void addGameInfoLabels(){
		roundLabel = new GLabel("ROUND:");
		roundLabel.setFont(new Font("Arial", Font.BOLD, 18));
		roundLabel.setColor(Color.BLACK);
		roundLabel.setLocation(20, frame.getHeight() - INFO_LABEL_HEIGHT + 25);
		addObject(roundLabel);
		
		roundLabelNumber = new GLabel("1");
		roundLabelNumber.setFont(new Font("Arial", Font.PLAIN, 18));
		roundLabelNumber.setColor(Color.BLACK);
		roundLabelNumber.setLocation( roundLabel.getX() + roundLabel.getWidth() + 10 , roundLabel.getY());
		addObject(roundLabelNumber);
		
		scoreLabel = new GLabel("SCORE:");
		scoreLabel.setFont(new Font("Arial", Font.BOLD, 18));
		scoreLabel.setColor(Color.BLACK);
		scoreLabel.setLocation(20, roundLabel.getY() + 25);
		addObject(scoreLabel);
		
		scoreLabelNumber = new GLabel("9999");
		scoreLabelNumber.setFont(new Font("Arial", Font.PLAIN, 18));
		scoreLabelNumber.setColor(Color.BLACK);
		scoreLabelNumber.setLocation( roundLabelNumber.getX() , scoreLabel.getY());
		addObject(scoreLabelNumber);
		
		messageLabel = new GLabel("");
		messageLabel.setFont(new Font("Arial", Font.BOLD, 36));
		messageLabel.setColor(Color.RED);
		messageLabel.setLocation( roundLabelNumber.getX() + roundLabelNumber.getWidth() + 150,  frame.getHeight() - INFO_LABEL_HEIGHT + 40);
		addObject(messageLabel);
	}
	
	
	/**
	 * Updates labels corresponding with score and round.
	 * @param round number to show for round
	 * @param score number to show for score
	 * 
	 */

	public void updateLabels( int round, int score){
		roundLabelNumber.setLabel( " " + round );
		scoreLabelNumber.setLabel( " " + score );
	}
	
	/**
	 * Updates <code>messageLabel</code> with given text.
	 * @param message text to show
	 */
	public void updateMessage( String message){
		messageLabel.setLabel(message);
	}
	
	/**
	 * Adds game over labels to board.
	 * @param score the last score of user
	 * @param round the last round of user
	 * @param username name of the user
	 */
	public void showGameOverLabel(int score, int round, String username){

		// Big game over text
		GLabel gameOverLabel = new GLabel("GAME OVER!");
		gameOverLabel.setFont(new Font("Arial", Font.BOLD, 120));
		gameOverLabel.setColor(Color.RED);
		gameOverLabel.setLocation( (frame.getWidth() - gameOverLabel.getWidth()) / 2, (frame.getHeight() - gameOverLabel.getHeight()) / 2);
		addObject(gameOverLabel);
		
		// User's name label
		GLabel gameOverNameLabel = new GLabel("Name: " + username);
		gameOverNameLabel.setFont(new Font("Arial", Font.BOLD, 50));
		gameOverNameLabel.setColor(Color.WHITE);
		gameOverNameLabel.setLocation( (frame.getWidth() - gameOverLabel.getWidth()) / 2 + 40, gameOverLabel.getY() + gameOverNameLabel.getHeight() + 20);
		addObject(gameOverNameLabel);

		// Round label
		GLabel gameOverRoundLabel = new GLabel("Round: " + round);
		gameOverRoundLabel.setFont(new Font("Arial", Font.BOLD, 50));
		gameOverRoundLabel.setColor(Color.WHITE);
		gameOverRoundLabel.setLocation( (frame.getWidth() - gameOverLabel.getWidth()) / 2 + 40, gameOverNameLabel.getY() + gameOverRoundLabel.getHeight() + 20);
		addObject(gameOverRoundLabel);

		// Score label
		GLabel gameOverScoreLabel = new GLabel("Score: " + score);
		gameOverScoreLabel.setFont(new Font("Arial", Font.BOLD, 50));
		gameOverScoreLabel.setColor(Color.WHITE);
		gameOverScoreLabel.setLocation( (frame.getWidth() - gameOverLabel.getWidth()) / 2 + 40, gameOverRoundLabel.getY()+ gameOverScoreLabel.getHeight() + 20);
		addObject(gameOverScoreLabel);

		// Game over black background
		GRect gameOverBack = new GRect(gameOverLabel.getWidth() + 20 , gameOverLabel.getHeight() + gameOverNameLabel.getHeight() + gameOverRoundLabel.getHeight() + gameOverScoreLabel.getHeight() + 100);
		gameOverBack.setLocation((frame.getWidth() - gameOverBack.getWidth()) / 2, (frame.getHeight() - gameOverBack.getHeight()) / 2);
		gameOverBack.setFillColor(Color.BLACK);
		gameOverBack.setFilled(true);
		addObject(gameOverBack);
		
		gameOverBack.sendToFront();
		gameOverLabel.sendToFront();
		gameOverNameLabel.sendToFront();
		gameOverRoundLabel.sendToFront();
		gameOverScoreLabel.sendToFront();
	}

	/**
	 * Returns <em>y</em> location of <code>turtle</code>
	 * @return <em>y</em> coordinate of turtle
	 */
	public double getTurtleY(){
		return turtle.getBounds().getY();
	}

	/**
	 * Checks collision of turtle with all the other vehicles on game.
	 * 
	 * @param vehicles These are all vehicles on board.
	 * @return true if turtle collide with vehicle, otherwise false
	 */
	public boolean checkTurtleCollision(ArrayList<MyVehicle> vehicles) {
		for (MyVehicle vehicle : vehicles) {
			if ( turtle.getBounds().intersects(vehicle.getBounds())){
				return true;
			}
			
			// It's my implementation for collision.
			// I discover intersects method.
			
			//	double vehX = vehicle.getBounds().getX();
			//	double vehY = vehicle.getBounds().getY();
			//	double turtX = turtle.getBounds().getX();
			//	double turtY = turtle.getBounds().getY();
			//	double turtW = turtle.getWidth();
			//	double turtH = turtle.getHeight();
			//	double vehW = vehicle.getWidth();
			//	double vehH = vehicle.getHeight();
			
			//	if ( vehX > turtX && vehX - turtX < turtW ){
			//		if ((vehY > turtY)  && (vehY - turtY < turtH))
			//			return true;
			//		else if ((turtY > vehY) && (turtY - vehY < vehH))
			//			return true;
			//	}
			//	else if ( vehX < turtX && turtX - vehX < vehW ){
			//		if ((vehY > turtY)  && (vehY - turtY < turtH))
			//			return true;
			//		else if ((turtY > vehY) && (turtY - vehY < vehH))
			//			return (( turtY >= vehY) && (turtY - vehY < vehH));
			//	}
		}
		return false;
	}
	
	
}