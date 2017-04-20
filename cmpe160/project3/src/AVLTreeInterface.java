import java.util.ArrayList;

public interface AVLTreeInterface<T> {
	
	/**
	 * @return true if the tree is empty
	 */
	public boolean isEmpty();

	/**
	 * @return the number of nodes in the tree
	 */
	public int size() ;
	
	/**
	 * Returns true if the object in the parameter
	 * is in the tree
	 * 
	 * @param element Element to be searched
	 * @return Whether tree contains the parameter
	 */
	public boolean contains(T element);
	

	/**
	 * Inserts the element in the parameter to the tree
	 * 
	 * If tree already contains the parameter,
	 * no update is done on the tree
	 * 
	 * @param element Element to be added
	 */
	public void insert(T element);
	
	/**
	 * Deletes the element in the parameter 
	 * 
	 * If tree does not contain the element in the parameter,
	 * no update is done on the tree
	 * 
	 * @param element Element to be deleted
	 */
	public void delete(T element);
	
	/**
	 * The height of a node is defined as the number of edges 
	 * from the node to the deepest leaf. 
	 * The height of a tree is the height of the root.
	 * 
	 * @return The height of the tree
	 */
	public int height();
	
	/**
	 * Traverses the tree "in order".
	 * This means, the left subtree of the node is traversed first.
	 * Then, the node itself is visited.
	 * Then, the the right subtree is traversed.
	 * 
	 * The data in each node is added to an ArrayList as the 
	 * node is processed. In other words, this ArrayList stores the order of visiting
	 * of the nodes in the tree
	 * 
	 * @return An ArrayList that stores the "data" field in nodes visited
	 */
	public ArrayList<T> inOrderTraversal();
	
	/**
	 * Visits all the nodes in a breadth first manner. 
	 * As nodes are visited, "data" fields are added to an ArrayList.
	 * 
	 * @return An ArrayList that stores the "data" field in nodes visited
	 */
	public ArrayList<T> bfTraverse();
	
	
	/**
	 * For two nodes, if their distances to the root is the same and
	 * their parents are not the same, they are said to be "cousins".
	 * 
	 * Of course, if one of the parameters is not in the list, they
	 * cannot be cousins.
	 * 
	 * @return true if elements in the parameter are cousins, false otherwise
	 */
	public boolean areCousins(T element1, T element2);
	
	
	/**
	 * Returns the number of elements in the tree that are 
	 * greater than <code>lower</code> and less than <code>upper</code>
	 * (bounds are not inclusive, i.e., not less than or equal to)
	 * 
	 * @param lower the lower limit
	 * @param upper the upper limit
	 * @return		the number of elements within the range
	 */
	public int numElementsInRange(T lower, T upper);
	
	/**
	 * Returns the balance factor of the node that stores the data
	 * given as parameter
	 * BalanceFactor: height(leftSubtree) - height(rightSubtree)
	 * 
	 * You may assume that there will be one and only one node 
	 * whose balance factor is calculated
	 * 
	 * @param data
	 * @return balance factor of the node storing data
	 */
	public int balanceFactor(T data);
	
	
}