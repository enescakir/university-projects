package visualization;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import acm.graphics.GCompound;
import acm.graphics.GLabel;
import acm.graphics.GOval;
import acm.graphics.GRect;

/**
 * Reprents big vehicles named Bus
 * @author Enes Cakir
 *
 */

public class Bus extends MyVehicle{

	/**
	 * Constructs new bus
	 * @param objX <em>x</em> coordinate of bus
	 * @param objY <em>y</em> coordinate of bus
	 * @param lane number that corresponding for lane of bus
	 * @param direction the direction of bus
	 */
	public Bus(int objX, int objY,  int lane, int direction) {
		width = 250;
		height = 120;
		windowLength = 50;
		wheelCircle = 30;	
		this.lane = lane;
		this.direction = direction;
		addBody(objX, objY);
		addWindows(objX, objY);
		addWheels(objX, objY);
		addLabel(objX, objY);
	}

	
	/**
	 * Adds body of bus
	 *
	 * @param objX <em>x</em> coordinate of bus
	 * @param objY <em>y</em> coordinate of bus
	 */
	@Override
	public void addBody(int objX, int objY) {
		body = new GRect(width, height);
		body.setFillColor(Color.RED);
		body.setFilled(true); 
		add(body, objX, objY);
	}

	/**
	 * Adds windows of bus
	 *
	 * @param objX <em>x</em> coordinate of bus
	 * @param objY <em>y</em> coordinate of bus
	 */
	@Override
	public void addWindows(int objX, int objY) {
		windows = new GRect[3];
		for (int i=0; i<3; i++) {
			windows[i] = new GRect(windowLength,windowLength);
			windows[i].setFillColor(Color.WHITE);
			windows[i].setFilled(true); 
		}
		add(windows[0], objX+(width-3*windowLength)/4, objY+10);
		add(windows[1], objX+windowLength+2*(width-3*windowLength)/4, objY+10);
		add(windows[2], objX+ 2 * windowLength+3*(width-3*windowLength)/4, objY+10);
	}

	/**
	 * Adds wheels of bus
	 *
	 * @param objX <em>x</em> coordinate of bus
	 * @param objY <em>y</em> coordinate of bus
	 */
	@Override
	public void addWheels(int objX, int objY) {
		wheels = new GOval[4];
		for (int i=0; i<4; i++) {
			wheels[i] = new GOval(wheelCircle,wheelCircle);
			wheels[i].setFillColor(Color.RED);
			wheels[i].setFilled(true); 
		}
		add(wheels[0], objX+ (width-4*wheelCircle)/5, objY+height-10);
		add(wheels[1], objX+ wheelCircle+2*(width-4*wheelCircle)/5, objY+height-10);
		add(wheels[2], objX+ 2 * wheelCircle+ 3 * (width-4*wheelCircle)/5, objY+height-10);
		add(wheels[3], objX+ 3 * wheelCircle+ 4 * (width-4*wheelCircle)/5, objY+height-10);

	}

	/**
	 * Adds label of bus
	 *
	 * @param objX <em>x</em> coordinate of bus
	 * @param objY <em>y</em> coordinate of bus
	 */
	@Override
	public void addLabel(int objX, int objY) {
		label = new GLabel("CAKIR TURIZM");
		label.setFont(new Font("Arial", Font.BOLD, 18));
		label.setColor(Color.WHITE);
		add(label,objX + (width - label.getWidth()) / 2,objY+80);
	}
	
	/**
	 * Creates random bus
	 * @return bus that positioned randomly
	 */
	public static Bus random() {
		int directionRandom = (int) (Math.random() * 100) % 2;
		int lineRandom = (int) (Math.random() * 100) % 4;
		return new Bus( directionRandom * 1550 - 250, 40 + lineRandom * 200, lineRandom + 1, directionRandom);
	}
	
	
}
