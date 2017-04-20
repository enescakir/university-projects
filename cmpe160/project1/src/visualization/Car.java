package visualization;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import acm.graphics.GLabel;
import acm.graphics.GOval;
import acm.graphics.GRect;

/**
 * Reprents small vehicles named Car
 * @author Enes Cakir
 *
 */
public class Car extends MyVehicle{

	/**
	 * Constructs new car
	 * @param objX <em>x</em> coordinate of car
	 * @param objY <em>y</em> coordinate of car
	 * @param lane number that corresponding for lane of car
	 * @param direction the direction of car
	 */
	public Car(int objX, int objY,  int lane, int direction) {
		width = 150;
		height = 80;
		windowLength = 20;
		wheelCircle = 20;
		this.lane = lane;
		this.direction = direction;
		addBody(objX, objY);
		addWindows(objX, objY);
		addWheels(objX, objY);
		addLabel(objX, objY);
	}
	
	/**
	 * Adds body of car
	 *
	 * @param objX <em>x</em> coordinate of car
	 * @param objY <em>y</em> coordinate of car
	 */
	@Override
	public void addBody(int objX, int objY) {
		body = new GRect(width, height);
		body.setFillColor(Color.BLACK);
		body.setFilled(true); 
		add(body, objX, objY);
	}

	/**
	 * Adds windows of car
	 *
	 * @param objX <em>x</em> coordinate of car
	 * @param objY <em>y</em> coordinate of car
	 */
	@Override
	public void addWindows(int objX, int objY) {
		windows = new GRect[2];
		for (int i=0; i<2; i++) {
			windows[i] = new GRect(windowLength,windowLength);
			windows[i].setFillColor(Color.WHITE);
			windows[i].setFilled(true); 
		}
		add(windows[0], objX+(width-2*windowLength)/3, objY+10);
		add(windows[1], objX+windowLength+2*(width-2*windowLength)/3, objY+10);
		
	}

	/**
	 * Adds wheels of car
	 *
	 * @param objX <em>x</em> coordinate of car
	 * @param objY <em>y</em> coordinate of car
	 */
	@Override
	public void addWheels(int objX, int objY) {
		wheels = new GOval[2];
		for (int i=0; i<2; i++) {
			wheels[i] = new GOval(wheelCircle,wheelCircle);
			wheels[i].setFillColor(Color.RED);
			wheels[i].setFilled(true); 
		}
		add(wheels[0], objX+(width-2*wheelCircle)/3, objY+height-10);
		add(wheels[1], objX+wheelCircle+2*(width-2*wheelCircle)/3, objY+height-10);
		
	}

	/**
	 * Adds label of car
	 *
	 * @param objX <em>x</em> coordinate of car
	 * @param objY <em>y</em> coordinate of car
	 */
	@Override
	public void addLabel(int objX, int objY) {
		label = new GLabel("CAKIR OTO");
		label.setFont(new Font("Arial", Font.BOLD, 18));
		label.setColor(Color.WHITE);
		add(label,objX + (width - label.getWidth()) / 2,objY+50);
		
	}
	
	/**
	 * Creates random car
	 * @return car that positioned randomly
	 */
	public static Car random() {
		int directionRandom = (int) (Math.random() * 100) % 2;
		int lineRandom = (int) (Math.random() * 100) % 4;
		return new Car( directionRandom * 1450 - 150, 75 + lineRandom * 190, lineRandom + 1, directionRandom);
	}
	
}
