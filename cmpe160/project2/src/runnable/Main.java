package runnable;

public class Main {

	public static void main(String[] args) {
		runSimulation("data/dests.txt", "data/waggons.txt", "data/locs.txt", "data/missions.txt", "data/result.txt");
	}

	public static void runSimulation(String destsFileName, String waggonsFileName, 
			String locsFileName, String missionsFileName, String outputFileName) {
		// your code goes below	
		
		/* 	
		  	Actually I declared cities and missions Arraylist in main class.
			But we can't write above this line. So I can't import java.util.*
			Then I move cities and missions Arraylist to their class as static property.
		*/
		
		// Load all cities and garages from files.
		City.loadData(destsFileName, waggonsFileName, locsFileName);
		
		// Load all missions from file.
		Mission.loadData(missionsFileName, City.all());
		
		// Run all missions by one by.
		for( Mission mission : Mission.all()){
			mission.run();
		}
		
		// Save all cities currents state
		City.save(outputFileName);

	}
	
	

}
