
public class Main {

	public static void main(String[] args) {
		AVLTree<Integer> tree = new AVLTree<Integer>();
		tree.insert(69);
		tree.insert(65);
		tree.insert(70);
		tree.insert(60);
		tree.insert(67);
		tree.insert(80);
		System.out.println("bfTraverse After Adding: " + tree.bfTraverse());
	}

}
