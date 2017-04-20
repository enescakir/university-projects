package runnable;

/**
 * Represents wagons
 * @author EnesCakir
 *
 */

public class Waggon {
	/**
	 * Name of the wagon
	 */
	private String name;
	
	/**
	 * Constructs a new locomotive with given name.
	 * @param name_ name of the locomotive
	 * 
	 */
	public Waggon( String name_ ){
		name = name_;
	}
	
	/**
	 * Return name of the wagon.
	 * @return name of the wagon
	 */
	public String getName(){ return name; }
	
	/**
	 * Returns string representation of the wagon.
	 * @return	string representation of wagon.
	 */	
	public String toString(){
		return name;
	}

}
