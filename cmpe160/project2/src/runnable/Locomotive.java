package runnable;

/**
 * Represents locomotives
 * @author EnesCakir
 *
 */
public class Locomotive extends Waggon implements Comparable<Locomotive>{
	/**
	 * Keeps power of the locomotive.
	 */
	double power;

	/**
	 * Constructs a new locomotive with given name and power.
	 * @param name_ name of the locomotive
	 * @param power_  power of the locomotive
	 */
	public Locomotive(String name_, double power_) {
		super(name_);
		power = power_;
	}
	
	/**
	 * Compares this locomotive with the given locomotive based on power.
	 * @param locomotive a locomotive to be compared
	 */
	@Override
	public int compareTo(Locomotive locomotive) {
		return (int) ((this.power - locomotive.power) * 100);
	}
}
