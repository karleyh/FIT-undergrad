/*
 * Author:  Karley Herschelman, kherschelman2012@my.fit.edu
 * Course:  CSE 4510, Section 01, Spring 2016
 * Project: hw2, SequentialCovering
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class HW2 {
	/**
	 * Rule structure
	 * Constructor: new Rule (consequent)
	 * add antecedents: Rule.antecedents.add(a)
	 */
	protected static class Rule {
		protected boolean[] usedAttributes;
		protected ArrayList<Antecedent> antecedents;
		protected String consequent;
		protected Rule(String consequent) {
			this.antecedents = new ArrayList<Antecedent>();
			this.consequent = consequent;
		}
		/**
		 * prints rule in (ant) & (ant) -> cons format
		 */
		@Override
		public String toString() {
			String s = "";
			for (int i = 0; i < antecedents.size(); i++) {
				if (i != 0)
					s += " & ";
				s += antecedents.get(i).toString();
			}
			if (antecedents.size() == 0) {
				s += "{}";
			}
			s += " -> " + consequent;
			return s;
		}

	}

	/**
	 * Antecedent structure
	 * stores attribute name, index and path
	 * Constructor: new Antecedent (attribute name, attribute index)
	 * Add Path: antecedent.attributePath = pathName;
	 */
	protected static class Antecedent {
		protected String attributeName;
		protected int attributeIndex;
		protected String attributePath;
		/**
		 * Constructor
		 * @param attributeName name of attribute
		 * @param attributeIndex index of attribute (row in attr file, column in training file)
		 */
		protected Antecedent(String attributeName, int attributeIndex) {
			this.attributeName = attributeName;
			this.attributeIndex = attributeIndex;
		}
		/**
		 * Prints antecedent as (name = path)
		 */
		@Override
		public String toString() {
			return "(" + attributeName + " " + attributePath + ")";
		}
	}
	/**
	 * Controls flow of execution
	 * @param args attributeFileName TrainingFileName [TestingFileName]
	 * @throws FileNotFoundException
	 */
	public static void main(String args[]) throws FileNotFoundException {
		ArrayList<ArrayList<String>> attribute = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> training = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> testing = new ArrayList<ArrayList<String>>();
		if (args.length >= 2) {
			// get the attribute and training data from file
			parseInput(args[0], attribute);
			parseInput(args[1], training);
			ArrayList<Rule> rules = sequentialCovering(attribute, training);
			for (int i = 0; i < rules.size(); i++) {
				System.out.println("r" + (i + 1) + ": "
						+ rules.get(i).toString());
			}
			System.out.println("Training set accuracy: " + accuracy (rules, training) + "%");
			if (args.length == 3) {
				// if test file is given
				parseInput(args[2], testing);
				System.out.println("Test set accuracy: " + accuracy (rules, testing) + "%");
				// print the tree, training set accuracy, and test set accuracy
				// using depth first search
			}
			if (args.length > 3)
				System.out
						.println("Too many files. All files beyond the first three arguments are ignored.");
		} else
			System.out
					.println("Not enough files. Must have at least attribute and training file.");
	}
	
	/**
	 * Given a set of records and a list of rule, calculate accuracy
	 * @param rules ArrayList<Rules>
	 * @param set <ArrayList<ArrayList<String>> records
	 * @return
	 */
	private static double accuracy (ArrayList <Rule> rules, ArrayList<ArrayList<String>> set){
		double correct = 0;
		for (int i = 0; i < set.size(); i++){
			for (int j = 0; j < rules.size(); j++){
				if (antecedentMatches(rules.get(j), set.get(i))){
					//consequent matches
					if (set.get(i).get(set.get(0).size() - 1).equals(rules.get(j).consequent)){
						correct += 1;
					}
					//use the first rule it matches
					break; 
				}
			}
		}
		return correct * 100 / set.size();
	}

	/**
	 * reads file into a 2D arrayList, removing whitespace
	 * 
	 * @param arg
	 *            fileName
	 * @param arrayList2D
	 *            arrayList to add the data from file to
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
	 * Sort classes from least common to most common
	 * @param attribute Attribute data set from file
	 * @param training Training data set from file
	 * @return String[] of class names in order
	 */
	private static String[] orderedClasses(
			ArrayList<ArrayList<String>> attribute,
			ArrayList<ArrayList<String>> training) {
		String[] orderedClasses = new String[attribute
				.get(attribute.size() - 1).size() - 1]; // number of classes
														// given by last row of
														// attributes
		for (int i = 1; i < attribute.get(attribute.size() - 1).size(); i++) {
			orderedClasses[i - 1] = attribute.get(attribute.size() - 1).get(i);
		}
		double[] classCount = new double[attribute.get(attribute.size() - 1)
				.size() - 1];
		int classColumn = training.get(0).size() - 1;
		for (int i = 0; i < training.size(); i++) {
			for (int j = 1; j < attribute.get(attribute.size() - 1).size(); j++) {
				if (training.get(i).get(classColumn)
						.equals(attribute.get(attribute.size() - 1).get(j))) {
					classCount[j - 1] += 1;
				}
			}
		}
		quickSort(classCount, orderedClasses, 0, classCount.length - 1);
		return orderedClasses;
	}

	/**
	 * quick sort from
	 * http://www.programcreek.com/2012/11/quicksort-array-in-java/ added: also
	 * keeps corresponding string list in the same order
	 * 
	 * @param arr
	 *            list of values to sort
	 * @param corr
	 *            corresponding strings to keep in same order
	 * @param low
	 *            bottom of list, pass 0
	 * @param high
	 *            top of list, pass arr.length-1
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
	 * Remove records that are covered by the rule
	 * @param training Set of training records
	 * @param r Rule
	 */
	private static void removeCoveredRecords(
			ArrayList<ArrayList<String>> training, Rule r) {
		int classColumn = training.get(0).size() - 1;
		for (int i = 0; i < training.size(); i++) {
			//antecedent and consequent of rule match record
			if (antecedentMatches (r, training.get(i)) && training.get(i).get(classColumn).equals(r.consequent)) {
				training.remove(i); //remove it from the training set
				i--; // check the index again now that it was removed
			}
		}
	}

	/**
	 * Learn the next best rule, determined by laplace
	 * @param attribute Attribute data set
	 * @param training Training data set
	 * @param currentClass class the rule is for
	 * @return rule
	 */
	private static Rule learnOneRule(ArrayList<ArrayList<String>> attribute,
			ArrayList<ArrayList<String>> training, String currentClass) {
		Rule r = new Rule(currentClass); // general-to-specific
		r.usedAttributes = new boolean[attribute.size() - 1];
		double highestLaplace = 0;
		double currentLaplace = 0;
		// continue adding antecedents until none can improve laplace
		 while(true){
			Antecedent best = null; // assume there is none that make it better
			// to find next antecedent, go through all attributes and their
			// combinations
			for (int i = 0; i < r.usedAttributes.length; i++) {
				// if the attribute hasn't been used before for this rule
				if (!r.usedAttributes[i]) {
					// create an antecedent with the attribute name and index
					Antecedent a = new Antecedent(attribute.get(i).get(0), i);
					if (attribute.get(i).get(1).equals("continuous")) {
						// if the attribute is continuous
						// store the values and their corresponding class
						double[] values = new double[training.size()];
						String[] corrClass = new String[training.size()];
						for (int j = 0; j < training.size(); j++) {
							values[j] = Double.parseDouble(training.get(j).get(i));
							corrClass[j] = training.get(j).get(
									attribute.size() - 1);
						}
						// sort the values, keeping the corresponding classes in
						// the
						// same order
						quickSort(values, corrClass, 0, values.length - 1);
						for (int j = 1; j < values.length; j++){
							double split = (values[j] + values[j - 1]) / 2.0;
							a.attributePath = ">= " + split;
							r.antecedents.add(a);
							// check how it effects laplace
							currentLaplace = laplace(training,r,attribute.get(attribute.size() - 1).size() - 1);
							// if it improves the score
							if (currentLaplace > highestLaplace) {
								// make that the new highest
								highestLaplace = currentLaplace;
								best = new Antecedent (a.attributeName, a.attributeIndex);
								best.attributePath = a.attributePath;
							}
							//remove the antecedent
							r.antecedents.remove(a); 
							a.attributePath = "< " + split;
							r.antecedents.add(a);
							// check how it effects laplace
							currentLaplace = laplace(
									training,
									r,
									attribute.get(attribute.size() - 1).size() - 1);
							// if it improves the score
							if (currentLaplace > highestLaplace) {
								// make that the new highest
								highestLaplace = currentLaplace;
								best = new Antecedent (a.attributeName, a.attributeIndex);
								best.attributePath = a.attributePath;
							}
							//remove the antecedent
							r.antecedents.remove(a); 
						}
					} else {
						// loop through all paths of the attribute
						for (int j = 1; j < attribute.get(i).size(); j++) {
							// make that the path of the antecedent
							a.attributePath = "= " + attribute.get(i).get(j);
							// add it to the rule
							r.antecedents.add(a);
							// check how it effects laplace
							currentLaplace = laplace(training,r,attribute.get(attribute.size() - 1).size() - 1);
							// if it improves the score
							if (currentLaplace > highestLaplace) {
								// make that the new highest
								highestLaplace = currentLaplace;
								best = new Antecedent (a.attributeName, a.attributeIndex);
								best.attributePath = a.attributePath;
							}
							r.antecedents.remove(a);
						}
					}
				}
			}
			// if there is no antecedent that improves the rule
			if (best != null){
				// add the best one to the rule
				r.antecedents.add(best);
				// mark attribute as used if it isn't continuous
				if (attribute.get(best.attributeIndex).get(0).equals("continuous"))
					r.usedAttributes[best.attributeIndex] = true;
			}
			else{
				break;
			}
		}
		return r;
	}
	
	/**
	 * Checks if the antecedent of the rule matches the record
	 * @param r the rule
	 * @param record a line from the data set (training or testing)
	 * @return true if matches, false if it doesn't
	 */
	private static boolean antecedentMatches (Rule r, ArrayList<String> record){
		for (int j = 0; j < r.antecedents.size(); j++) {
			// if this line of the training set at the attribute index
			// doesn't match the antecedent
			if ((r.antecedents.get(j).attributePath.substring(0, 2).equals(
					">=") && Double.parseDouble(record.get(
					r.antecedents.get(j).attributeIndex)) < Double
					.parseDouble((r.antecedents.get(j).attributePath
							.substring(3))))
					|| ((r.antecedents.get(j).attributePath.substring(0, 1)
							.equals("<")) && (Double.parseDouble(record.get(r.antecedents.get(j).attributeIndex)) >= Double
							.parseDouble((r.antecedents.get(j).attributePath
									.substring(2)))))
					|| ((r.antecedents.get(j).attributePath.substring(0, 1)
							.equals("=")) && (!record.get(r.antecedents.get(j).attributeIndex)
							.equals(r.antecedents.get(j).attributePath
									.substring(2))))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Calculate laplace
	 * @param training
	 * @param r
	 * @param numClasses
	 * @return
	 */
	private static double laplace(ArrayList<ArrayList<String>> training,
			Rule r, int numClasses) {
		int classColumn = training.get(0).size() - 1;
		double numCorrect = 0; //records that match the antecedent and the consequent
		double numCovered = 0; //records that match the antecedent
		for (int i = 0; i < training.size(); i++) {
			// if the record matched the antecedent
			if (antecedentMatches (r, training.get(i))) {
				numCovered += 1;
				//and the consequent
				if (training.get(i).get(classColumn).equals(r.consequent))
					numCorrect += 1;
			}
		}
		//System.out.println("numCorrect: " + numCorrect + ",numCovered: " + numCovered + ",numClasses:" + numClasses);
		return (numCorrect + 1) / (numCovered + numClasses);
	}

	/**
	 * Stop when there are no more records for the current class
	 * @param training Set of training records
	 * @param currentClass Current class for developing rules
	 * @return true=stop, false=keep going
	 */
	private static boolean stoppingCond(ArrayList<ArrayList<String>> training,
			String currentClass) {
		if (training.isEmpty()) {   
			return true;
		}
		int classColumn = training.get(0).size() - 1;
		for (int i = 0; i < training.size(); i++) {
			if (training.get(i).get(classColumn).equals(currentClass)) {
				return false; // there is one training record that matches
			}
		}
		return true;
	}

	/**
	 * Algorithm 5.1, p213 Intro to Data Mining
	 * @param attribute Attribute data set
	 * @param training Training data set
	 * @return ArrayList of rules
	 */
	private static ArrayList<Rule> sequentialCovering(
		ArrayList<ArrayList<String>> attribute,
		ArrayList<ArrayList<String>> training) {
		ArrayList<Rule> rules = new ArrayList<Rule>();
		String[] classes = orderedClasses(attribute, training);
		// loops through class list at end of attribute matrix
		for (int i = 0; i < classes.length -1; i++) {
			while (!stoppingCond(training, classes[i])) {
				Rule r = learnOneRule(attribute, training, classes[i]);
				rules.add(r);
				int trainingSize = training.size();
				removeCoveredRecords(training, r);
				if (training.size() == trainingSize){
					//rule didn't improve set
					rules.remove(r);
					break;
				}
			}
		}
		rules.add(new Rule(classes[classes.length - 1]));
		return rules;
	}
}
