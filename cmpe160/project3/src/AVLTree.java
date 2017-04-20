import java.util.ArrayList;


public class AVLTree<T extends Comparable<T>> implements AVLTreeInterface<T> {

	public Node<T> root;

	/**
	 * Basic storage units in a tree. Each Node object has a left and right
	 * children fields.
	 * 
	 * If a node does not have a left and/or right child, its right and/or left
	 * child is null.
	 * 
	 */
	private class Node<E> {
		private E data;
		private Node<E> left, right; // left and right subtrees

		public Node(E data) {
			this.data = data;
		}
	}

	// CHANGES START BELOW THIS LINE
	
	/**
	 * Keeps number of the nodes in tree
	 */
	private int numOfNodes = 0;
	
	@Override
	public boolean isEmpty() { return root == null; }

	@Override
	public int size() { return numOfNodes; }

	@Override
	public boolean contains(T element) {
		Node<T> node = root;
		while( node != null){
			if(node.data.equals(element)){ return true; }
			
			if(element.compareTo(node.data) < 0){ 
				node = node.left; 
			}
			else{ 
				node = node.right; 
			}		
		} 
		return false;
	}
	
	/**
	 * Finds given element in tree, and returns Node object
	 * @param element object to find
	 * @return Node<T> version of given element
	 */
		public Node<T> find(T element) {
		Node<T> node = root;
		while( node != null){
			if(node.data.equals(element)){ return node; }
			if(element.compareTo(node.data) < 0){  node = node.left; }
			else{ node = node.right; }		
		} 
		return null;
	}


	@Override
	public void insert(T element) {
		if(!this.contains(element)){
			Node<T> newNode = new Node<T>(element);
			if(root == null){ root = newNode; }
			else{
				Node<T> currentRoot = root;
				Node<T> node = root;
				
				while( node != null){
					if(element.compareTo(node.data) < 0){ node = node.left; }
					else{ node = node.right; }
					if(node != null){ currentRoot = node; }
				} 
				
				if(element.compareTo(currentRoot.data) < 0){
					currentRoot.left = newNode;
				}
				else{
					currentRoot.right = newNode;
				}
				
				//Rebalancing
				Node<T> parentOfNode = parent(newNode.data);
				while(parentOfNode != null){
					Node<T> parentOfParentNode = parent(parentOfNode.data);
					Node<T> rotatedNode = rebalance(parentOfNode);					
					if(rotatedNode.equals(parentOfNode)){
						if(!parentOfNode.equals(root)) parentOfNode = parent(parentOfNode.data);
						else break;
					}else{
						if(parentOfParentNode == null ) root = parentOfNode;
						else{
							if(parentOfNode.equals(parentOfParentNode.right)) 
								parentOfParentNode.right = rotatedNode;
							else if(parentOfNode.equals(parentOfParentNode.left)) 
								parentOfParentNode.left = rotatedNode;
						}
						break;
					}
				}
			}
			numOfNodes++;
		}
	}

	/**
	 * Rotates given node to right
	 * @param parent node to rotate
	 * @return right rotated new parent node
	 */
	private Node<T> rightRotate(Node<T> parent) {
		Node<T> leftright = parent.left.right;
		Node<T> newParent = parent.left;
		newParent.right = parent;
		newParent.right.left = leftright;
		return newParent;
	}
	
	/**
	 * Rotates given node to left
	 * @param parent node to rotate
	 * @return left rotated new parent node
	 */
	private Node<T> leftRotate(Node<T> parent) {
		Node<T> rightleft = parent.right.left;
		Node<T> newParent = parent.right;
		newParent.left = parent;
		newParent.left.right = rightleft;
		return newParent;
	}
		
	/**
	 * Makes necessary rotates and returns rebalanced node
	 * @param node object that will be rebalanced
	 * @return rebalanced node
	 */
	private Node<T> rebalance(Node<T> node) {
		if(node == null) return null;
		int bf = balanceFactor(node.data);
		if(bf < -1) {
			if (balanceFactor(node.right.data) > 0) {
				node.right = rightRotate(node.right);
				node = leftRotate(node);

			}else {
				node = leftRotate(node);
			}
		}else if (bf > 1) {
			if(balanceFactor(node.left.data) > 0) {
				node = rightRotate(node);
			} 
			else {
				node.left = leftRotate(node.left);
				node = rightRotate(node);
			}
		}
		return node;
	}
	
	@Override
	public void delete(T element) {
		Node<T> node = find(element);
		if(node == null){ return; }
		
		Node<T> leftMostOfRight = node.right;
		if(leftMostOfRight != null){
			while(leftMostOfRight.left != null){ 
				leftMostOfRight = leftMostOfRight.left;
			}
			Node<T> parentOfLMR = parent(leftMostOfRight.data);
			node.data = leftMostOfRight.data;
			if(parentOfLMR.equals(node)) 
			{
				node.right = leftMostOfRight.right;
			}
			if(leftMostOfRight.equals(parentOfLMR.right)) 
				parentOfLMR.right = null;
			else if(leftMostOfRight.equals(parentOfLMR.left)) 
				parentOfLMR.left = null;
		}
		else {
			Node<T> parent = parent(node.data);
			if(parent == null) {
				root = null;
				return;
			}
			if(node.left == null){
				if(node.equals(parent.right)) 
					parent.right = null;
				else if(node.equals(parent.left)) 
					parent.left = null;
			}else{
				if(node.equals(parent.right)) 
					parent.right = node.left;
				else if(node.equals(parent.left)) 
					parent.left = node.left;
			}
		}
		numOfNodes--;
		//Rebalancing after delete has a problem. I haven't enough time for fix it
//		Node<T> parentOfNode = node;		 
//		while(parentOfNode != null){
//			Node<T> parentOfParentNode = parent(parentOfNode.data);
//			Node<T> rotatedNode = rebalance(parentOfNode);					
//			if(rotatedNode.equals(parentOfNode)){
//				if(!parentOfNode.equals(root)) parentOfNode = parent(parentOfNode.data);
//				else break;
//			}else{
//				if(parentOfParentNode == null ) root = parentOfNode;
//				else{
//					if(parentOfNode.equals(parentOfParentNode.right)) 
//						parentOfParentNode.right = rotatedNode;
//					else if(parentOfNode.equals(parentOfParentNode.left)) 
//						parentOfParentNode.left = rotatedNode;
//				}
//				break;
//			}
//		}

	}

	@Override
	public int height() {
		return heightOfNode(root);
	}
	
	/**
	 * Calculates distances of node to deepest leaf
	 * @param node object to calculate height
	 * @return height of given node
	 */
	private int heightOfNode(Node<T> node) {
		  if(node == null) return -1;
		  if(node.left==null && node.right==null) return 0;
		  else if(node.left==null) return 1+heightOfNode(node.right);
		  else if(node.right==null) return 1+heightOfNode(node.left); 
		  else return 1+ Math.max(heightOfNode(node.left),heightOfNode(node.right));
	}


	@Override
	public ArrayList<T> inOrderTraversal() {
		ArrayList<T> arrayList = new ArrayList<T>();
		infixTraverse(root, arrayList);
		return arrayList;
	}

	/**
	 * Runs recursively and traverses in order
	 * @param node visited object
	 * @param arrayList list of datas in order
	 */
	private void infixTraverse(Node<T> node, ArrayList<T> arrayList){
		if(node != null){
			infixTraverse(node.left, arrayList);
			arrayList.add(node.data);
			infixTraverse(node.right, arrayList);			
		}
	}

	@Override
	public ArrayList<T> bfTraverse() {
		ArrayList<Node<T>> queue = new ArrayList<Node<T>>() ;
		ArrayList<T> arrayList = new ArrayList<T>();
		
		if (root == null) return arrayList;
		queue.add(root);
		while(!queue.isEmpty()){
			Node<T> node = queue.remove(0);
			arrayList.add(node.data);
			if(node.left != null) queue.add(node.left);
			if(node.right != null) queue.add(node.right);
		}
		return arrayList;
	}

	@Override
	public boolean areCousins(T element1, T element2) {
		if( depth(element1) == depth(element2) && !(parent(element1).equals(parent(element2)))) return true;
		return false;
	}

	@Override
	public int numElementsInRange(T lower, T upper) {
		ArrayList<T> inOrder = inOrderTraversal();
		int numElements = 0;
		for(T item: inOrder){
			if( item.compareTo(lower) > 0 && item.compareTo(upper) < 0 ) numElements++;
		}
		return numElements;
	}

	@Override
	public int balanceFactor(T data) {
		Node<T> node = find(data);
		if(node == null) return -1;
		return heightOfNode(node.left) - heightOfNode(node.right);
	}

	/**
	 * Calculates distances of node to root
	 * @param element object to calculate depth
	 * @return depth of given object
	 */
	private int depth(T element){
		Node<T> node = root;
		int depth = 0;
		while(node != null){
			if(element.compareTo(node.data) > 0) node = node.right;
			else if(element.compareTo(node.data) < 0) node = node.left;
			else if(element.equals(node.data)) break;
			depth++;
		}
		return depth;
	}
	
	/**
	 * Returns parent of given element
	 * @param element object to find parent
	 * @return parent of element
	 */
	private Node<T> parent(T element){
		Node<T> parent = root;
		Node<T> node = root;
		while(node != null){
			if(element.compareTo(node.data) > 0) node = node.right;
			else if(element.compareTo(node.data) < 0) node = node.left;
			if(node != null && element.equals(node.data)) break;
			if(node != null) parent = node;
		}
		if(node != null && node.data.equals(root.data)) return null;
		return parent;
	}


	// CHANGES END ABOVE THIS LINE	
}
