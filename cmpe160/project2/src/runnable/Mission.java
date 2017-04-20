package runnable;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Keeps each mission as a object
 * @author EnesCakir
 *
 */
public class Mission {
	/**
	 * Starting station of mission
	 */
	private City startingStation;
	
	/**
	 * Midway station of mission
	 */
	private City midwayStation;
	
	/**
	 * Target station of mission
	 */
	private City targetStation;
	
	/**
	 * The number of the wagon should taken from starting station
	 */
	private int takingStarting;
	
	/**
	 * The number of the wagon should taken from midway station
	 */
	private int takingMidway;
	
	/**
	 * The index of the wagons should leaved at midway station
	 */
	private int[] removingIndexs;
	
	/**
	 * Keeps all the missions that uploaded to application
	 */
	private static ArrayList<Mission> missions;

	/**
	 * 
	 * Constructs a new mission with given line of string
	 * @param line one line version of mission
	 * @param cities all the cities uploaded to app
	 * 
	 */
	public Mission ( String line, ArrayList<City> cities){
		// Splits line to parts
		String[] parts = line.split("\\-");
		startingStation = City.getCity(parts[0]);
		midwayStation = City.getCity(parts[1]);
		targetStation = City.getCity(parts[2]);
		takingStarting = Integer.parseInt(parts[3]);
		takingMidway = Integer.parseInt(parts[4]);
		String[] indexes = parts[5].split("\\,");
		removingIndexs = new int[indexes.length];
		/* When you remove an object that has smaller index than other object, 
		 * index of the second object will be changed. So we need to rearrage indexes.
		 */
		for( int i = 0; i < indexes.length; i++){
			int reducer = 0;
			for ( int j = 0; j < i; j++){
				if(Integer.parseInt(indexes[j]) < Integer.parseInt(indexes[i]))
					reducer++;
			}
			removingIndexs[i] = Integer.parseInt(indexes[i]) - reducer + 1;
		}
	}
		
	/**
	 * Runs the mission.
	 */
	public void run() {
		// Construct train
		Train train = new Train();
		
		// Take locomotive
		train.add( startingStation.giveLocomotive() );
		
		// Take wagons from starting station.
		for( int i = 0; i < takingStarting; i++)
			train.add( startingStation.giveWaggon() );
		
		// Take wagons from midway station.
		for( int i = 0; i < takingMidway; i++)
			train.add( midwayStation.giveWaggon() );
		
		// Leave wagons to midway station.
		for( int i = 0 ; i < removingIndexs.length; i++){
			midwayStation.addWaggon( train.remove(removingIndexs[i]));
		}
		
		// Disband remained train to target station.
		targetStation.addLocomotive( (Locomotive) train.remove(0));
		while( train.size() != 0)
			targetStation.addWaggon(train.remove(0));
		
	}

	/**
	 * Returns all the missions.
	 * @return  all the missions object uploaded application
	 */
	public static ArrayList<Mission> all(){
		return missions;
	}
	
	/**
	 * Loads given text files to application, and converts missions to Mission object. 
	 * @param missionsFileName the location of the file that contains missons
	 * @param cities all the cities uploaded to app
	 */
	public static void loadData(String missionsFileName, ArrayList<City> cities) {
		missions = new ArrayList<Mission>();
		try {
			Scanner sc = new Scanner(new File( missionsFileName ));
			while( sc.hasNextLine() ){
				Mission newMission = new Mission(sc.nextLine(), cities);
				missions.add(newMission);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
