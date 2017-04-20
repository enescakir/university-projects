package adts;

import intfs.DoublyLinkedListIntf;

public class MyDLL<T> implements DoublyLinkedListIntf<T>{

	/**
	 * Abstraction of a node in the Doubly Linked List (DLL) ADT.
	 *
	 * Example Usage: A Node in an integer DLL has the following fields:
	 * 		int data;
	 * 		Node<Integer> next, prev;
	 * 
	 * @param <E> type of the data.
	 */
	public class Node<E> {
		E data;
		Node<E> next;
		Node<E> prev;

		public Node(E data_) {
			data = data_;
		}
	}

	/**
	 *	The first node of the list.
	 *	NOTE: Made public for testing purposes.
	 */
	public Node<T> first;

	// CHANGES START BELOW THIS LINE
	
	@Override
	public void add(T item) {
		if(first == null){
			first = new Node<T>(item);
			return;
		}
		Node<T> newLast = new Node<T>(item);
		Node<T> last = first;
		while(last.next != null){ last = last.next; }
		last.next = newLast;
		newLast.prev = last;
	}

	@Override
	public boolean add(T item, int index) {
		Node<T> newNode = new Node<T>(item);
		Node<T> node = first;
		for(int i = 0; i < index; i++){
			node = node.next;
			if( node == null){ return false; }
		}
		node.prev.next = newNode;
		newNode.prev = node.prev;
		newNode.next = node;
		node.prev = newNode;
		return true;
	}

	@Override
	public T remove(int index) {
		Node<T> node = first;
		for(int i = 0; i < index; i++){
			node = node.next;
			if( node == null){ throw new IndexOutOfBoundsException(); }
		}
		if(node.next != null){ node.next.prev = node.prev; }
		if(node.prev != null){ node.prev.next = node.next; }
		else{first = node.next;}
		return node.data;
	}

	@Override
	public T get(int index) {
		Node<T> node = first;
		for(int i = 0; i < index; i++){
			node = node.next;
			if( node == null){ throw new IndexOutOfBoundsException(); }
		}
		return node.data;
	}
	
	/**
	 * Returns string representation of the doubly linked list.
	 * @return	string representation of list.
	 */	
	public String toString(){
		Node<T> node = first;
		String text = "[";
		while(node != null){
			text += node.data;
			if(node.next != null){ text += ", "; }
			node = node.next;
		}
		text += "]";
		return text;
	}
	
	/**
	 * Returns number of elements in list.
	 * @return	size of list.
	 */
	public int size(){
		Node<T> node = first;
		int counter = 0;
		while(node != null){
			node = node.next;
			counter++;
		}
		return counter;
	}

	// CHANGES END ABOVE THIS LINE

}
