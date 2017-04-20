package adts;

import intfs.PriorityQueueIntf;

import java.util.LinkedList;

import adts.MyDLL.Node;

public class MyPQ<T extends Comparable<T>> implements PriorityQueueIntf<T> {


	/**
	 * data added to the stack must be stored in <code>list</code>
	 */
	LinkedList<T> list = new LinkedList<T>();

	// CHANGES START BELOW THIS LINE
	
	@Override
	public boolean offer(T item) {
		return list.add(item);
	}

	@Override
	public T poll() {
		T max = this.peek();
		list.remove(max);
		return max;
	}

	@Override
	public T peek() {
		if(this.count() == 0)
			return null;
		T max = list.getFirst();
		for(T item : list){
			if( item.compareTo(max) > 0)
				max = item;
		}
		return max;
	}

	@Override
	public int count() {
		return list.size();
	}
	
	/**
	 * Returns string representation of the queue.
	 * @return	string representation of queue.
	 */	
	public String toString(){
		String text = "";
		for( int i = list.size() - 1; i >= 0; i--)
			text += "\n" + list.get(i);
		return text;
	}

	// CHANGES END ABOVE THIS LINE
	
}
