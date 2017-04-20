package runnable;

import java.io.*;
import java.util.*;

import adts.MyPQ;
import adts.MyStack;

/**
 * Represents destinations. It has 2 type garages at its station. 
 * @author EnesCakir
 *
 */
public class City {
	/**
	 * Keeps name of the city.
	 */
	private String name;
	
	/**
	 * Keeps parked wagons at the city.
	 */
	private MyStack<Waggon> waggonGarage;
	
	/**
	 * Keeps parked locomotives at the city.
	 */
	private MyPQ<Locomotive> locomotiveGarage;
	
	/**
	 * Keeps all the cities that uploaded to application
	 */
	private static ArrayList<City> cities;
	
	/**
	 * Constructs a new city with given name.
	 * @param name_ name of the city
	 */
	public City(String name_) {
		name = name_;
		waggonGarage = new MyStack<Waggon>();
		locomotiveGarage = new MyPQ<Locomotive>();
	}
	
	/**
	 * Returns string representation of the city.
	 * @return	string representation of city.
	 */	
	public String toString() {
		String text = name;
		text += "\nWaggon Garage:";
		text += waggonGarage.toString();
		text += "\nLocomotive Garage:";
		text += locomotiveGarage.toString();
		return text;
	}
	
	/**
	 * Return name of the city.
	 * @return name of the city
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Gives the most powerful locomotive from city's locomotive garage
	 * @return the most powerful locomotive at the city
	 */
	public Locomotive giveLocomotive(){
		return locomotiveGarage.poll();
	}

	/**
	 * Gives a wagon from top of the city's wagon garage
	 * @return a wagon from the garage
	 */
	public Waggon giveWaggon(){
		return waggonGarage.pop();
	}
	
	/**
	 * Adds given wagon to city.
	 * @param wagon vehicle to add to garage
	 */
	public void addWaggon( Waggon wagon){
		waggonGarage.push(wagon);
	}

	/**
	 * Adds given locomotive to city.
	 * @param locomotive vehicle to add to garage
	 */
	public void addLocomotive( Locomotive locomotive){
		locomotiveGarage.offer(locomotive);
	}
	
	/**
	 * Searches on cities list by name and returns city object representation of given name.
	 * @param name the name of given city
	 * @return city object representation of given city name
	 */
	public static City getCity(String name){
		for( City city : cities){
			if( city.getName().equals(name)){
				return city;
			}
		}
		return null;
	}
	
	/**
	 * Saves all the city's current situation to text file.
	 * @param outputFileName the location of the file that contains results
	 */
	public static void save(String outputFileName){
		try {
			PrintStream out = new PrintStream( new File(outputFileName));
			for( City city : cities){
				out.println(city.toString());
				out.println("----------");
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Returns all the cities.
	 * @return  all the cities object uploaded application
	 */
	public static ArrayList<City> all() {
		return cities;
	}
	
	/**
	 * Loads given text files to application, and converts datas to objects 
	 * @param destsFileName the location of the file that contains destinations
	 * @param waggonsFileName the location of the file that contains wagons
	 * @param locsFileName the location of the file that contains locomotives
	 */
	public static void loadData(String destsFileName, String waggonsFileName, 
			String locsFileName) {
		cities = new ArrayList<City>();
		Scanner sc;
		try {
			sc = new Scanner(new File( destsFileName ));
			while( sc.hasNextLine() ){
				City newCity = new City(sc.nextLine());
				cities.add(newCity);
			}
			sc = new Scanner(new File( waggonsFileName ));
			while( sc.hasNextLine() ){
				if( sc.hasNext()){
					String wName = sc.next();
					String cName = sc.next();
					City city = City.getCity(cName);
					city.waggonGarage.push(new Waggon( wName ));
				}else break;
			}
			sc = new Scanner(new File( locsFileName ));
			while( sc.hasNextLine() ){
				if( sc.hasNext()){
					String lName = sc.next();
					String cName = sc.next();
					double lPower = sc.nextDouble();
					City city = City.getCity(cName);
					city.locomotiveGarage.offer(new Locomotive( lName, lPower));
				}else break;
			}


		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
