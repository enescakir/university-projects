package intfs;

/**
 * To be implemented while implementing MyDLL class
 * @param <T> type of the elements this queue keeps.
 */
public interface DoublyLinkedListIntf<T> {
	/**
	 * Appends an item to the end of the queue.
	 * @param item item to be appended.
	 */
	public void add(T item);
	
	/**
	 * Inserts an item to the specified location.
	 * @param item	item to be inserted.
	 * @param index	location that item will be inserted.
	 * @return	true if successful; false otherwise.
	 */
	public boolean add(T item, int index);
	
	/**
	 * Retrieves and removes the item at specified location.
	 * @param index	the location of removal.
	 * @return	removed item.
	 */
	public T remove(int index);
	
	/**
	 * Retrieves the item at specified location.
	 * @param index the location of the item to be retrieved.
	 * @return the item at the specified location.
	 */
	public T get(int index);
}
