package runnable;

import java.io.FileNotFoundException;
import java.util.Scanner;

import visualization.Game;
import visualization.MyVehicle;

/**
 * Runs game.
 * @author Enes Cakir
 *
 */
public class Main {

	/**
	 * Provides a main loop to game.
	 * Proceeds game with Game class methods.
	 * 
	 * @param args These are the command line parameters.
	 * @throws FileNotFoundException couldn't find highscore.txt
	 */
	public static void main(String[] args) throws FileNotFoundException {
		// Get name from user with console.
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter your name:");
		String username = scanner.nextLine();
		
		// Create new game
		Game game = new Game("Sprinter Turtle", username);
		
		// Show good luck message
		game.showMessage("Good luck " + username + "!");
		while(true){
			game.checkCollisions();
			if( game.isOver() ){
				game.gameOver();
				break;
			}
			else{
				MyVehicle vehicle = game.createVehicle();
				// If vehicle location occupied by another vehicle does't add it.
				if (vehicle != null){
					game.addObject(vehicle);
					game.addVehicle(vehicle);;
					vehicle.sendToFront();
				}
				game.moveVehicles();
				game.removeVehicles();
				game.updateScore();
			}
			game.waitFor(50);
		}
	}
}