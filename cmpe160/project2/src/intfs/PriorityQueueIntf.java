package intfs;

/**
 * To be implemented while implementing MyPQ class
 * @param <T> type of the elements this queue keeps.
 */
public interface PriorityQueueIntf<T> {
	/**
	 * Adds an item into the queue.
	 * @param item item to be added.
	 * @return true if successful; false otherwise.
	 */
	public boolean offer(T item);
	
	/**
	 * Retrieves and removes an item from the queue.
	 * @return the item at the front
	 */
	public T poll();
	
	/**
	 * Retrieves the item at the front.
	 * @return	the item at the front.
	 */
	public T peek();
	
	/**
	 * Returns the number of items in the queue.
	 * @return  the number of items in the queue.
	 */
	public int count();
}
