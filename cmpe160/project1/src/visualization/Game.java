package visualization;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import acm.graphics.GObject;

/**
 * Manages the rules of the game. Such as speed.
 * Has a board and an user for playing game.
 * 
 * @author EnesCakir
 *
 */
public class Game {
	private Board board;
	private ArrayList<MyVehicle> vehicles;
	private int speed;
	private User user;
	private boolean over; // keeps game status
	private boolean isHalfway; // keeps did user get the top of the board.

	
	/**
	 * Constructs new board and user for game. Set field to initial values.
	 * @param gameName name of the game.
	 * @param userName	name of the player from user.
	 */
	public Game(String gameName, String userName){
		board = new Board(gameName, 1300, 966);
		user = new User(userName);
		speed = 1;
		isHalfway = false;
		vehicles = new ArrayList<MyVehicle>();
	}
	
	/**
	 * 
	 * Move each vehicle on the game's <code>vehicles</code> arraylist steps that based on <em>speed</em> in their corresponding direction.
	 * 
	 */
	public void moveVehicles() {
		for (Vehicle vehicle : vehicles) {
			vehicle.move( -2 * speed * vehicle.getDirection() + speed, 0);
		}
	}

	/**
	 * Create new vehicle checking the already created vehicles.
	 * Creation, type and lane of the vehicle are decided randomly.
	 * 
	 * Once the vehicle created, if the lane is not available (occupied with some other vehicle),
	 * the newly created one is discarded.
	 * 
	 * @return vehicle to be added to the board. If vehicles lane occupied with some other vehicle returns null.
	 */
	public MyVehicle createVehicle() {
		double createNew = Math.random(); // possibility of creating new vehicle %1
		MyVehicle randomVehicle = null;
		if (createNew < 0.01){
			double typeChance = Math.random();  // possibility of vehicle type %50 Car/Bus
			if ( typeChance < 0.5){
				randomVehicle = Car.random();
				if (randomVehicle.checkCollision(vehicles))
					randomVehicle = null;
			}
			else{
				randomVehicle = Bus.random();
				if (randomVehicle.checkCollision(vehicles))
					randomVehicle = null;
			}
		}
		return randomVehicle;
	}
	
	/**
	 * Checks collisions for moving cars. If they collide, changes their directions
	 */
	private void checkVehicleCollisions() {
		for (int i = 0; i < vehicles.size(); i++) {
			for (int j = i; j < vehicles.size(); j++) {
				MyVehicle veh1 = vehicles.get(i);
				MyVehicle veh2 = vehicles.get(j);
				double veh1X = veh1.getBounds().getX();
				double veh2X = veh2.getBounds().getX();
				if ( veh1.getLane() == veh2.getLane()){
					if ((veh2X > veh1X) && (veh2X - veh1X < veh1.getWidth())){
						veh1.changeDirection();
						veh2.changeDirection();
					}
					else if((veh1X > veh2X) && (veh1X - veh2X < veh2.getWidth())){
						veh1.changeDirection();
						veh2.changeDirection();

					}
				}

			}
		}
	}
	
	/**
	 * Checks both vehicle collisions and turtle collision.
	 */
	public void checkCollisions() {
		checkVehicleCollisions();
		if(board.checkTurtleCollision(vehicles))
			over = true;
	}
	
	/**
	 * Wraps game's board waitFor method. Stops game for given millisecs.
	 * @param millisecs milliseconds to wait game.
	 * @see Board
	 */
	public void waitFor(long millisecs) {
		board.waitFor(millisecs);
	}
	
	/**
	 * Checks the game status.
	 * @return true if game is over, otherwise false
	 */
	public boolean isOver() {
		return over;
	}
	
	/**
	 * Manages game overing tasks.
	 * Saves user's score to text, and shows game over labels
	 * @throws FileNotFoundException couldn't find highscore.txt
	 */
	public void gameOver() throws FileNotFoundException {
		user.save();
		board.showGameOverLabel(user.getScore(), user.getRound(), user.getName());
	}
	
	/**
	 * Wraps game board's <code>updateMessage(String)</code> method.
	 * Shows given message to player.
	 * @param message message to show player
	 * @see Board
	 */
	public void showMessage(String message) {
		board.updateMessage(message);
	}


	/**
	 * Wraps game board's <code>addObject(GObject)</code> method.
	 * Adds given object to board..
	 * @param  g object to add board
	 * @see Board
	 */
	public void addObject(GObject g) {
		board.addObject(g);
	}
	
	/**
	 * Adds given vehicle to game's vehicles arrayList.
	 * @param vehicle vehicle to add arrayList
	 */
	public void addVehicle(MyVehicle vehicle) {
		vehicles.add(vehicle);
	}
	
	/**
	 * Removes vehicles, if they are out of canvas.
	 */
	public void removeVehicles() {
		for (int i = 0; i < vehicles.size(); i++) {
			MyVehicle veh = vehicles.get(i);
			double vehX = veh.getBounds().getX();
			if (vehX > 1500 || vehX < -250){
				vehicles.remove(i);
				board.removeObject(veh);
			}
		}
	}

	/**
	 * Updates user's score field and corresponding labels.
	 * Checks user's moving next round situation.
	 */
	public void updateScore() {
		user.increaseScore();
		if ( board.getTurtleY() < 50 ){
			isHalfway = true;
			showMessage("Almost done " + user.getName() + ". You done half of it. Go back!");
		}
		
		if ( board.getTurtleY() > 790 && isHalfway ){
			isHalfway = false;
			user.nextRound();
			speed += 2;
			showMessage("You did it " + user.getName() + ". Round " + user.getRound() + " more difficult.");
		}
		board.updateLabels(user.getRound(), user.getScore());
	}
}
