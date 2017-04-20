package visualization;

import acm.graphics.GCompound;
import acm.graphics.GLabel;
import acm.graphics.GOval;
import acm.graphics.GRect;

public abstract class Vehicle extends GCompound {

	protected GRect body;
	protected GRect[] windows;
	protected GOval[] wheels;
	protected GLabel label;
	
	protected int width, height, windowLength, wheelCircle;
	protected int direction, lane;
	
	public abstract void addBody(int objX, int objY);
	
	public abstract void addWindows(int objX, int objY);

	public abstract void addWheels(int objX, int objY);

	public abstract void addLabel(int objX, int objY);
	
	public int getDirection(){
		return direction;
	}
	
	public int getLane() {
		return lane;
	}
	
}
