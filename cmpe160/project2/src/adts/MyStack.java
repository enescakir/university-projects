package adts;

import java.util.ArrayList;

import intfs.StackIntf;

public class MyStack<T> implements StackIntf<T> {
	
	/**
	 * data added to the stack must be stored in <code>list</code>
	 */
	ArrayList<T> list = new ArrayList<T>();
	
	// CHANGES START BELOW THIS LINE
	
	@Override
	public T push(T item) {
		list.add(item);
		return item;
	}

	@Override
	public T pop() {
		if(this.count() == 0)
			return null;
		T last = list.get( this.count() - 1 );
		list.remove(last);
		return last;
	}

	@Override
	public T peek() {
		if(this.count() == 0)
			return null;
		return list.get( this.count() - 1 );
	}

	@Override
	public boolean isEmpty() {
		return list.size() == 0;
	}

	@Override
	public int count() {
		return list.size();
	}	
	
	/**
	 * Returns string representation of the stack.
	 * @return	string representation of stack.
	 */	
	public String toString(){
		String text = "";
		for( int i = list.size() - 1; i >= 0; i--)
			text += "\n" + list.get(i);
		return text;
	}

	// CHANGES END ABOVE THIS LINE
}
