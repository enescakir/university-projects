

package visualization;

import java.util.ArrayList;

/**
 * 
 * Behaves like a layer between <code>Vehicle</code> class and <code>Bus/Car</code> classes.
 * @author Enes Cakir
 * @see Vehicle
 * @see Bus
 * @see Car
 * 
 */
public abstract class MyVehicle extends Vehicle {

	/**
	 * Changes direction of vehicle.
	 */
	public void changeDirection(){
		 if(direction == 0)
			 direction = 1;
		 else
			 direction = 0;
	}

	/**
	 * Checks collision of this vehicle with all the other vehicles on board.
	 * 
	 * @param vehicles These are all vehicles on board.
	 * @return true if vehicles collide, otherwise false
	 */
	public boolean checkCollision(ArrayList<MyVehicle> vehicles) {
		for (MyVehicle vehicle : vehicles) {
			if ( this.getBounds().intersects(vehicle.getBounds())){
				return true;
			}
			// It's my implementation for collision.
			// I discover intersects method.

			//	double vehX = vehicle.getBounds().getX();
			//	double thisX = this.getBounds().getX();
			//	if ( vehicle.getLane() == this.getLane()){
			//		if ((vehX > thisX) && (vehX - thisX < this.getWidth())){
			//			return true;
			//		}
			//		else if((thisX > vehX) && (thisX - vehX < vehicle.getWidth())){
			//			return true;
			//		}
			//	}
		}
		return false;
	}

}
