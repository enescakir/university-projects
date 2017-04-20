package intfs;

/**
 * To be implemented while implementing MyStack class.
 * @param <T> type of the elements this stack keeps.
 */
public interface StackIntf<T> {
	/**
	 * Appends the item to the end of the list.
	 * @param item item to be added to the list.
	 * @return added item.
	 */
	public T push(T item);
	
	/**
	 * Retrieves and removes the item at the end of the list.
	 * @return the item to be removed.
	 */
	public T pop();
	
	/**
	 * Retrieves the item at the end of the list.
	 * @return the item at the end of the list.
	 */
	public T peek();
	
	/**
	 * Checks if the stack is empty.
	 * @return true if the stack is empty; false, otherwise.
	 */
	public boolean isEmpty();
	
	/**
	 * Returns the number of elements in the stack.
	 * @return the number of elements.
	 */
	public int count();
}
