/*
 * Author:  Karley Herschelman, kherschelman2012@my.fit.edu
 * Course:  datamining, Section 01, Spring 2016
 * Project: hw1, DecisionTreeInduction
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class DecisionTreeInduction {
	private static String maxClass;
	 static double trainAccurate = 0;
	 static double testAccurate = 0;
	protected static class Node {
		protected String head; //name of node
		protected ArrayList<String> edges; //name of edges from node
		protected Node[] children = null; //name of nodes adj to node
		protected boolean marked = false; //marked by dfs
		protected Node(ArrayList<String> bestSplit) {
			head = bestSplit.get(0); //first value in list
			edges = new ArrayList<String>(); //rest of list
			for (int i = 1; i < bestSplit.size(); i++){
				edges.add(bestSplit.get(i));
			}
			//same amount as edges
			children = new Node[edges.size()]; 
		}
		protected Node() {} //default constructor
	}

	/**
	 * controls flow of execution
	 * @param args attributeFileName trainingFileName [testingFileName]
	 * @throws FileNotFoundException
	 */
	public static void main(String args[]) throws FileNotFoundException {
		ArrayList<ArrayList<String>> attribute = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> training = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> testing = new ArrayList<ArrayList<String>>();
		if (args.length >= 2) {
			//get the attribute and training data from file
			parseInput(args[0], attribute);
			parseInput(args[1], training);
			//the immutable equivalent of the training data, better for recursion
			String[][] trainingA = arrayList2array(training);
			//This is the class most used at the root node. If the training set is empty, this will be used
			maxClass = getMaxClass(trainingA, attribute);
			//Due to the variable size of the attribute list, it must be stored in an arraylist which is mutable
			//This keeps track of the attributes used in an immutable structure, better for recursion
			boolean[] attributesUsed = new boolean [attribute.size()-1];
			if (args.length < 3){
				//print the tree and the training set accuracy using depth first search
				printTree(TreeGrowth (trainingA, attribute, attributesUsed), 1, attribute, trainingA, null);
				System.out.println("");
				System.out.println("Training Set Accuracy: " + (trainAccurate/training.size()) * 100 + "%");
			}
			else if (args.length == 3) {
				// if test file is given
				parseInput(args[2], testing);
				//print the tree, training set accuracy, and test set accuracy using depth first search
				printTree(TreeGrowth (trainingA, attribute, attributesUsed), 1, attribute, trainingA, arrayList2array(testing));
				System.out.println("");
				System.out.println("Training Set Accuracy: " + (trainAccurate/training.size()) * 100 + "%");
				System.out.println("Test Set Accuracy: " + (testAccurate/testing.size()) * 100 + "%");
			}
			else 
				System.out.println("Too many files. All files beyond the first three arguments are ignored.");
		} else 
			System.out.println("Not enough files. Must have at least attribute and training file.");

	}
	
	/**
	 * gets the most used class in the training set given
	 * @param training training set to get most used class from
	 * @param attribute attribute set with last row containing the class list
	 * @return most used class
	 */
	private static String getMaxClass (String [][] training, ArrayList<ArrayList<String>> attribute){
		String mostClass;
		//a tally of how many times each class is in the training list
		int[] tally = new int[attribute.get(attribute.size()-1).size() - 1];
		//increase tally by matching training list to attribute list
		for (int i = 0; i < training.length; i++){
			for (int j = 1; j < attribute.get(attribute.size()-1).size(); j++){
				if (training[i][attribute.size()-1].equals(attribute.get(attribute.size()-1).get(j))){
						tally[j-1] ++;
				}
			}
		}
		//find max
		int max = tally[0];
		mostClass = attribute.get(attribute.size()-1).get(1);
		for (int i = 1; i < tally.length; i++){
			if (tally[i] > max){
				max = tally[i];
				mostClass = attribute.get(attribute.size()-1).get(i+1);
			}
		}
		return mostClass;
	}

	/**
	 * reads file into a 2D arrayList, removing whitespace
	 * @param arg fileName
	 * @param arrayList2D arrayList to add the data from file to
	 * @throws FileNotFoundException
	 */
	private static void parseInput(String arg,
			ArrayList<ArrayList<String>> arrayList2D)
			throws FileNotFoundException {
		ArrayList<String> trimmed;
		Scanner file = new Scanner(new File(arg));
		while (file.hasNextLine()) {
			trimmed = new ArrayList<String>();
			// get next line of input, split on spaces
			String[] line = file.nextLine().split(" ");
			// loop through line array
			for (int i = 0; i < line.length; i++) {
				// add all values that are not "" to trimmed arrayList
				if (!line[i].trim().equals("")) {
					trimmed.add(line[i]);
				}
			}
			// add trimmed ArrayList to the 2D ArrayList
			arrayList2D.add(trimmed);
		}
		file.close();
	}
	
	/**
	 * Compute the Gini Index
	 * @param classification
	 *            classification matrix
	 * @return Gini Index
	 */
	private static double giniIndex(int[][] classification) {
		int[] sum = new int[classification.length]; //sum of each row
		int total = 0; //total count of entire matrix
		int expectedValue = 0;
		//find the sum of each row, and the total for probabilities
		for (int i = 0; i < classification.length; i++) {
			for (int j = 0; j < classification[0].length; j++) {
				sum[i] += classification[i][j];
				total += classification[i][j];
			}
		}
		//matrix is empty, gini index is 1
		if (total == 0){
			return 1;
		}
		//1- sum of p(i|t)^2
		for (int i = 0; i < classification.length; i++) {
			double gini = 1; 
			for (int j = 0; j < classification[0].length; j++) {
				if (sum[i] != 0){
					gini -= Math.pow(classification[i][j] / sum[i], 2);
				}
			}
			expectedValue += (gini * sum[i] / total);
		}
		return expectedValue;
	}
	
	/**
	 * Finds the best split using the Gini index
	 * @param training 2D array of training set
	 * @param attributes 2D arrayList of attributes
	 * @param attributesUsed boolean array corresponding to which attributes have been used
	 * @return ArrayList best split with first value in array list being the
	 *         split attribute and the following values being the edges
	 */
	private static ArrayList<String> findBestSplit(String[][] training, ArrayList<ArrayList<String>> attributes, boolean[] attributesUsed) {
		ArrayList<String> bestSplit = new ArrayList<String>();
		double gini = 1;
		int numClasses = attributes.get(attributes.size() - 1).size() - 1;
		// loop through each remaining attribute to find the best one
		for (int i = 0; i < attributes.size() - 1; i++) {
			if (!attributes.get(i).get(1).equalsIgnoreCase("continuous")) {
				if (!attributesUsed[i]){
					// if the attribute is not continuous
					// classification matrix with the count of each occurrence
					int[][] classification = new int[attributes.get(i).size() - 1][numClasses];
					// loop through training set
					for (int j = 0; j < training.length; j++) {
						// loop through each attribute
						for (int k = 1; k < attributes.get(i).size(); k++) {
							// loop through each class
							for (int l = 1; l <= numClasses; l++) {
								// classify the line of training data into one of
								// the combination of attribute/class
								if (training[j][i]
										.equals(attributes.get(i).get(k))
										&& training[j][attributes.size()-1]
												.equals(attributes.get(
														attributes.size()-1).get(
														l))) {
									// increase the count in the matrix if the data
									// fits in this combination
									classification[k - 1][l - 1]++;
								}
							}
						}
					}
					// check if the gini index for this matrix is smallest
					if (giniIndex(classification) < gini) {
						gini = giniIndex(classification);
						// add attribute line as the best split data
						bestSplit = attributes.get(i);
					}
				}
			} else {
				// if the attribute is continuous
				// store the values and their corresponding class
				double[] values = new double[training.length];
				String[] corrClass = new String[training.length];
				for (int j = 0; j < training.length; j++) {
					values[j] = Double.parseDouble(training[j][i]);
					corrClass[j] = training[j][attributes.size()-1];
				}
				// sort the values, keeping the corresponding classes in the
				// same order
				quickSort(values, corrClass, 0, values.length - 1);
				// do first split to get classification table and then shift
				// from there
				// average the second value and the first as the first split
				double split = (values[1] + values[0]) / 2.0;
				// classify the combinations to < or >= and the classes
				int[][] classification = new int[2][numClasses];
				// loop through values
				for (int j = 0; j < values.length; j++) {
					// loop though classes
					for (int l = 1; l <= numClasses; l++) {
						// if >= split and in current class
						if (values[j] >= split
								&& corrClass[j].equals(attributes.get(
										attributes.size() - 1).get(l))) {
							classification[0][l - 1]++;
						} else { // if < split and in current class
							classification[1][l - 1]++;
						}
					}
				}
				// check if the gini index for this matrix is smallest
				if (giniIndex(classification) < gini) {
					gini = giniIndex(classification);
					bestSplit = new ArrayList<String>();
					// add the head
					bestSplit.add(attributes.get(i).get(0));
					// edge 1. < split
					bestSplit.add("<" + split);
					// edge 2. >= split
					bestSplit.add(">=" + split);
				}
				// loop through remaining values
				for (int j = 2; j < values.length; j++) {
					// find split between two current values
					double newSplit = (values[j] + values[j - 1]) / 2.0;
					// loop through all values
					for (int k = 0; k < values.length; k++) {
						// if current value is < current split
						if (values[k] < newSplit) {
							// and greater or equal to old split
							if (values[k] >= split) {
								// this value will be classified different than
								// the old matrix
								// loop though classes
								for (int l = 1; l <= numClasses; l++) {
									// if value falls in this class
									if (corrClass[j].equals(attributes.get(
											attributes.size() - 1).get(l))) {
										// shift count for this class from
										// the >= side to < side
										classification[0][l - 1]++;
										classification[1][l - 1]--;
									}
								}
							}
						} else { // once value is > new Split
							split = newSplit;
							break; // move on to next split
						}
					}
					// check if the gini index for this matrix is smallest
					if (giniIndex(classification) < gini) {
						gini = giniIndex(classification);
						bestSplit.clear();
						// add the head
						bestSplit.add(attributes.get(i).get(0));
						// edge 1. < split
						bestSplit.add("<" + split);
						// edge 2. >= split
						bestSplit.add(">=" + split);
					}
				}
			}
		}
		return bestSplit;
	}
	
	/**
	 * quick sort from http://www.programcreek.com/2012/11/quicksort-array-in-java/
	 * added: also keeps corresponding string list in the same order
	 * @param arr list of values to sort
	 * @param corr corresponding strings to keep in same order
	 * @param low bottom of list, pass 0
	 * @param high top of list, pass arr.length-1
	 */
	private static void quickSort(double[] arr, String[] corr, int low, int high) {
		// http://www.programcreek.com/2012/11/quicksort-array-in-java/
		if (arr == null || arr.length == 0)
			return;
		if (low >= high)
			return;
		// pick the pivot
		int middle = low + (high - low) / 2;
		double pivot = arr[middle];
		// make left < pivot and right > pivot
		int i = low, j = high;
		while (i <= j) {
			while (arr[i] < pivot) {
				i++;
			}
			while (arr[j] > pivot) {
				j--;
			}
			if (i <= j) {
				double temp = arr[i];
				arr[i] = arr[j];
				arr[j] = temp;
				// keep the corresponding array in the same order
				String temp1 = corr[i];
				corr[i] = corr[j];
				corr[j] = temp1;
				i++;
				j--;
			}
		}
		// recursively sort two sub parts
		if (low < j)
			quickSort(arr, corr, low, j);
		if (high > i)
			quickSort(arr, corr, i, high);
	}
	/**
	 * creates subset of training records based on the current attribute and the edge it is going down
	 * @param training 2D array of training records 
	 * @param edge string name of edge going down
	 * @param attributeIndex column index of attribute in training set
	 * @return subset of training records that fit the given criteria
	 */
	private static String[][] getRecords(String[][] training, String edge, int attributeIndex){
		ArrayList <String[]> reducedSet = new ArrayList <String[]>();
		//loop through training records
		for (int i = 0; i < training.length; i++) { // shrink records
			//if the edge contains <
			if (edge.contains("<")) {
				//and the value in the training set is less than the edge
				if (Double.parseDouble(training[i][attributeIndex]) < Double
						.parseDouble(edge.substring(1))) {
					//add it to the subset
					reducedSet.add(training[i]); 
				}
			} else if (edge.contains(">=")) {
				//if the edge contains >= and the value is >= the edge
				if (Double.parseDouble(training[i][attributeIndex]) >= Double
						.parseDouble(edge.substring(2))) {
					//add it to the subset
					reducedSet.add(training[i]); 
				}
			} else {
				if (training[i][attributeIndex].equals(edge)) {
					//if the value and the edge are the same string 
					reducedSet.add(training[i]); 
					//add it to the subset
				}
			}
		}
		//return as an array
		String [][] array = new String [reducedSet.size()][training[0].length];
		for (int i = 0; i < array.length; i++){
			array[i] = reducedSet.get(i);
		}
		
		return array;
	}
	
	/**
	 * Decision Tree Induction Algorithm
	 * @param training 2D array of training set
	 * @param attributes 2D arrayList of attributes
	 * @param attributesUsed boolean array marking attributes used
	 * @return root node
	 */
	private static Node TreeGrowth(String[][] training,
			ArrayList<ArrayList<String>> attributes, boolean[] attributesUsed) {
		if (stoppingCond(training, attributesUsed)) {
			//reached a leaf node
			Node leaf = new Node();
			//get string classification
			leaf.head = classify(training, attributes);
			return leaf;
		} 
		Node root = new Node(findBestSplit(training, attributes, attributesUsed));
		// find the index of the current attribute
		int attributeIndex = 0;
		for (int i = 0; i < attributes.size() - 1; i++) {
			if (attributes.get(i).get(0).equals(root.head)) {
				attributeIndex = i;
				break;
			}
		}
		if (!root.edges.get(0).contains("<")){
			//not continuous, mark attribute to not be used again
			attributesUsed[attributeIndex] = true;
		}
		for (int v = 0; v < root.children.length; v++) {
			// for each child
			root.children[v] = TreeGrowth((getRecords (training, root.edges.get(v), attributeIndex)), attributes, attributesUsed);
		}
		return root;
	}
	/**
	 * Used to terminate the tree-growing process by testing 3 conditions: 1. no
	 * more records 2. no more attributes 3. all records belong to one class
	 * @param training 2D array of training set
	 * @param attributesUsed boolean array marking attributes used
	 * @return true if tree has reached a leaf node, false otherwise
	 */
	private static boolean stoppingCond(String[][] training,
			boolean[] attributesUsed) {
		if (training.length < 2) { // no more splits to be made
			return true;
		}
		boolean stop = true;
		for (int i = 0; i < attributesUsed.length; i++){
			if (!attributesUsed[i]){
				//not all attributes have been used
				stop = false;
				break;
			}
		}
		if (stop){
			//all attributes have been used
			return true;
		}
		for (int i = 1; i < training.length; i++) {
			if (!training[i][attributesUsed.length].equals(training[0][attributesUsed.length])) {
				// records exist, attributes exist, class are not equal
				return false;
			}
		}
		return true; // all remaining records fall in the same class
	}
	
	/**
	 * Converts 2D ArrayList to 2D array
	 * @param alist 2D arraylist
	 * @return 2D array
	 */
	private static String[][] arrayList2array (ArrayList<ArrayList<String>> alist){
		//create 2D array of equivalent size
		String [][] array = new String [alist.size()][alist.get(0).size()];
		//copy over all values
		for (int i = 0; i < alist.size(); i++){
			for (int j = 0; j< alist.get(0).size(); j++){
				array[i][j] = alist.get(i).get(j);
			}
		}
		return array;
	}	 
	
	/**
	 * prints the tree and calculates accuracy of training and testing set(optional)
	 * @param n current node
	 * @param depth keeps track of the depth of the current node
	 * @param attributes 2D arraylist of attributes
	 * @param training 2D array of training set
	 * @param testing 2D array of testing set or null if no testing set
	 */
	 private static void printTree(Node n, int depth, ArrayList<ArrayList<String>> attributes, String[][] training, String[][]testing) {
		n.marked = true; //mark the node as visited
		if (n.children == null)
		{
			//reached a leaf node
			//check how many values in the training set = the leaf node value
			for (int i = 0; i < training.length; i++){
				if (training[i][attributes.size() -1].equals(n.head)){
					//add to the trainAccurate for each one found
					trainAccurate += 1; 
				}
			}
			if (testing != null){
				//check how many values in the testing set = the leaf node value
				for (int i = 0; i < testing.length; i++){
					if (testing[i][attributes.size() -1].equals(n.head)){
						//add to the testAccurate for each one found
						testAccurate += 1;
					}
				}
			}
			return;
		}
		depth++; //increase depth
		//find current nodes attribute index
		int attributeIndex = 0;
		for (int i = 0; i < attributes.size() - 1; i++) {
			if (attributes.get(i).get(0).equals(n.head)) {
				attributeIndex = i;
				break;
			}
		}
		//loop through all children of current node
		for (int w = 0; w < n.children.length; w++) {
			//if the child has not been visited
			if (!n.children[w].marked) {
				//pint out a | for how deep
				for (int i = 0; i < depth-2; i++){
					System.out.print ("|");
				}
				//print the head and the current edge
				System.out.print(n.head + " " + n.edges.get(w) + ":  ");
				if (n.children[w].children == null){
					//if the child on that edge is a leaf, print it too
					System.out.print (n.children[w].head);
				}
				System.out.println("");
				//recurse
				if (testing != null)
					printTree(n.children[w], depth, attributes, getRecords(training, n.edges.get(w), attributeIndex), getRecords(testing, n.edges.get(w), attributeIndex));
				else
					printTree(n.children[w], depth, attributes, getRecords(training, n.edges.get(w), attributeIndex), testing);
			}
		}
	}
	
	 /**
	  * @param training 2D string array of training set
	  * @param attribute 2D string arraylist of attribute set
	  * @return string corresponding to leaf
	  */
	private static String classify (String[][] training, ArrayList<ArrayList<String>> attribute){ 
		if (training.length == 0){
			//no training records, use maxClass at root node
			return maxClass;
		}
		//otherwise, get max class for the training records
		return getMaxClass(training, attribute);
	}
}