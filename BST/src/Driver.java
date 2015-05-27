import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class Driver {
	public static void main(String[] args) {
		// get random object
		Random rgen = new Random();
		rgen.setSeed(15);
		BST<Integer> integerTree = new BST<Integer>();
		// create array of characters
		Integer[] integers = { 9, 5, 2, 3, 6, 10, 57, 8 };
		// shuffle it
		Collections.shuffle(Arrays.asList(integers), rgen);
		System.out.print("To insert: ");
		for (int i = 0; i < integers.length; i++) {
			System.out.print(integers[i] + " ");
			integerTree.insert(integers[i]);
			
		}

		integerTree.delete(5);
		System.out.println();
		// printout level-order traversal
		System.out.println(integerTree.showTree());
		// get random object
		BST<Character> letterTree = new BST<Character>();
		// create array of characters
		Character[] letters = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
				'X' };
		// shuffle it
		Collections.shuffle(Arrays.asList(letters), rgen);
		System.out.print("To insert: ");
		for (int i = 0; i < letters.length; i++) {
			System.out.print(letters[i] + " ");
			letterTree.insert(letters[i]);
		}
		System.out.println();
		// printout level-order traversal
		System.out.println(letterTree.showTree());
		System.out.println("Inorder tree walk:");
		letterTree.getRoot().inorderTreeWalk();
		System.out.println();
		// delete some characters from a tree
		letterTree.delete('A');
		//letterTree.delete('X');
		//letterTree.delete('I');
		System.out.println("Tree after few elements were deleted:");
		System.out.println(letterTree.showTree());
		// search for a subtree rooted at ’G’
        Node<Character> nodeWithLetter=letterTree.searchNode('G');
		System.out.println("Subtree rooted at ’G’:");
		// print level-order traversal of a tree rooted at nodeWithLetter
		System.out.println(nodeWithLetter.toString());
	}

}
