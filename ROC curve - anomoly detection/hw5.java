import java.awt.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Scanner;
public class HW5 {
	/**
	 * Stores a record from the input
	 * values stores location coordinates
	 * c stores the record's class
	 * id stores the name of the record
	 * @author Karley
	 */
	private static class Record implements Comparable<Record>{
		protected String id;
		protected double [] values;
		protected double density;
		protected double outlierScore;
		protected String c;
		//neighborhood of nearest records and their distances
		protected HashMap <Record, Double> nearest = new HashMap <Record, Double> ();
		public Record (String id, double [] values, String classe){
			this.id = id;
			this.values = values;
			this.c = classe;
		}
		//Euclidian distance of this record to record r
		public double distance (Record r){
			double distance = 0;
			for (int i = 0; i < values.length; i++){
				distance += Math.pow(values[i] - r.values[i], 2);
			}
			return Math.sqrt(distance);
		}
		//Records are equal when they have all the same values
		@Override
		public boolean equals(Object o){
			Record r = (Record)o;
			if (Arrays.equals(values, r.values)){
				return true;
			}
			return false;
		}
		//Sort records in descending order based on outlier scores
		@Override
		public int compareTo(Record r) {
			 if (this.outlierScore > r.outlierScore){
            	 return -1;
             }
             if (this.outlierScore < r.outlierScore){
            	 return 1;
             }
             return 0;
		}
	}
	
	/**
	 * Create an ROC curve of the records, print TP/FP pairs and AUC
	 * @param records list of records
	 * @param numPos number of actual outliers
	 */
	private static void ROC (ArrayList<Record> records, int numPos){
		//sort the records in descending order
		Collections.sort (records);
		int numNeg = records.size() - numPos;
		double AUC = 0;
		int a = 0;
		int b = 0;
		double x = 0;
		double y = 0;
		double tp;
		double fp;
		double trapezoid;
		Record r;
		//print out first point at (0,0)
		System.out.println("TP: " + 0 + ", FP: " + 0);
		if (records.size() >= 1){
			r = records.get (1);
			if (r.c.equals (anomClass)){	
				//true positive
				a++;
			}
			else{
				//false postive
				b++;
			}
		}
		//calculate first coordinates
		x = a/(double)numPos;
		y = b/(double)numNeg;
		System.out.println("TP: " + y + ", FP: " + x);
		AUC += (x * y) / 2; //first one is a triangle
		for (int i = 2; i < records.size(); i++){
			r = records.get(i);
			if (r.c.equals (anomClass)){			
				//true positive
				a++;
			}
			else{
				//false positive
				b++;
			}
			//found positive/actual positive
			tp = a/(double)numPos;
			//found neg/actual neg
			fp = b/(double)numNeg;
			//previous height + this height  2
			trapezoid = (((y + tp) /2.0));
			// * width (this x - previous x)
			trapezoid *= Math.abs(fp - x);
			System.out.println("TP: " + tp + ", FP: " + fp);
				//System.out.println ("trap: " + trapezoid);
			AUC += trapezoid;
			//store previous point
			x = fp;
			y = tp;
		}
		//last point is (1,1)
		System.out.println("TP: " + 1 + ", FP: " + 1);
		trapezoid = (((y + 1) /2.0));
		trapezoid *= Math.abs(1 - x);
	
		AUC += trapezoid;
		System.out.println("AUC " + AUC);
	}
	
	private static String anomClass; //store the name of the outlier class
	
	/**
	 * Find the anom class and how many are in it
	 * @param records list of records
	 * @param classes list of classes
	 * @return size of anom class
	 */
	private static int actual(ArrayList<Record> records, ArrayList<String> classes){
		int [] count = new int [classes.size()];
		//get a count of each class
		for (int i = 0; i < records.size(); i++){
			for (int j = 0; j < classes.size(); j ++){
				if (classes.get (j).equals(records.get(i).c)){
					count[j] ++;
				}
			}
		}
		//find min class
		int min = count [0];
		anomClass = classes.get(0);
		for (int i = 1; i < count.length; i++){
			if (count[i] < min){
				min = count[i];
				anomClass = classes.get(i);
			}
		}
		return min;
	}
	
	/**
	 * Find the neighborhood of k values to a record
	 * @param r Record we are finding neighborhood of
	 * @param k number of neighbors
	 * @param records list of records
	 */
	private static void kNearest (Record r, int k, ArrayList<Record> records){
		HashMap<Double, Record> recordDistances = new HashMap<Double, Record>();
		//find the distance to each other record
		for (int i = 0; i < records.size(); i++){
			Record current = records.get(i);
			if (!r.equals(current)){
				recordDistances.put(r.distance(current), current);
			}
		}
		ArrayList<Double> distances = new ArrayList<Double>(recordDistances.keySet());
		Collections.sort(distances);
		//get k smallest distances and add store those in the neighborhood
		for (int i = 0; i < k; i++){
			double d = distances.get(i);
			r.nearest.put(recordDistances.get(d), d);
		}
	}
	
	/**
	 * density is the inverse average distance in the neighborhood
	 * @param r Record
	 * @return density
	 */
	private static double density (Record r){
		double total = 0;
		ArrayList <Double> distances = new ArrayList<Double> (r.nearest.values());
		for (int i = 0; i < distances.size(); i++){
			//sum all the distances
			total += distances.get(i);
		}
		//divide by k
		total/=distances.size();
		//return inverse
		return 1/total;
	}
	
	/**
	 * average relative density is the average of the densities of all the records in the neighborhood
	 * @param r Record
	 * @return average relative density
	 */
	private static double averageRelativeDensity(Record r){
		double total = 0;
		ArrayList <Record> neighbors = new ArrayList<Record> (r.nearest.keySet());
		for (int i = 0; i < neighbors.size(); i++){
			//sum all densities of records in neighborhood
			total += neighbors.get(i).density;
		}
		//divide
		total/=neighbors.size();
		//return total divided by the density of the current record
		return total/r.density;
	}
	
	/** 
	 * Algorithm 10.2
	 * @param records List of Records
	 * @param k number of records in neighborhood
	 */
	private static void outlierScoreAlg (ArrayList<Record> records, int k){
		for (int i = 0; i < records.size(); i++){
			Record r = records.get(i);
			kNearest (r, k, records);
			r.density = density (r);
		}
		for (int i = 0; i < records.size(); i++){
			Record r = records.get (i);
			r.outlierScore = averageRelativeDensity (r);
		}
	}
	
	/**
	 * Execute Experiment 1
	 * @param records List of records
	 */
	private static void e1 (ArrayList<Record> records){
		//find outlier scores for k=5
		outlierScoreAlg(records, 5);
		Collections.sort(records);
		for (int i = 0; i < records.size(); i++){
			//print outlier scores
			Record r = records.get(i);
			System.out.println(r.outlierScore);
		}
	}
	
	/**
	 * Execute Experiment 2
	 * @param records Records
	 * @param classes Classes
	 */
	private static void e2 (ArrayList<Record> records, ArrayList<String> classes){
		int actualAnoms = actual (records, classes);
		for (int k = 3; k <= 10; k++){
			//find outlier scores based on k
			System.out.println("k = " + k);
			outlierScoreAlg (records, k);
			//find ROC curve and print TP,FP and AUC
			ROC (records, actualAnoms);
		}
	}
	/**
	 * Parse input, control flow of execution
	 * @param args attrFile.txt recordsFile.txt k normalize?(true/false)
	 * @throws FileNotFoundException
	 */
	 public static void main(String[] args) throws FileNotFoundException {
		ArrayList<String> attributeNames = new ArrayList<String>();
		ArrayList<String> classes = new ArrayList<String>();
		String arg = args[0]; //get first input argument
		Scanner namesFile;
		Scanner recordsFile;
		if (arg.equalsIgnoreCase("e1") || arg.equalsIgnoreCase("e2")){
			//set up experiment files
			namesFile= new Scanner (new File ("cancer-attr.txt"));
			recordsFile = new Scanner (new File ("cancer.txt"));
		}
		else{
			//otherwise get file names from arguments
			namesFile = new Scanner (new File (arg));
			recordsFile = new Scanner (new File (args[1]));
		}
		//read files
		namesFile.nextLine(); //throw out the first line
		boolean hasClass = false;
		//read names file
		while (namesFile.hasNextLine()){
			String x = namesFile.next();
			if (x.equals("class")){
				hasClass = true;
				while (namesFile.hasNext()){
					classes.add(namesFile.next());
				}
			}
			else{
				attributeNames.add(x);
				namesFile.next();
			}
		}
		namesFile.close();
		//read records file
		ArrayList<Record> records = new ArrayList<Record>();
		while (recordsFile.hasNextLine()){
			String[] line = recordsFile.nextLine().split(" |	");
			ArrayList<String> trimmed = new ArrayList<String>();
			for (int i = 0; i < line.length; i++){
				if (!line[i].trim().equals("") && !line[i].trim().equals("	")){
					trimmed.add (line[i]);
				}
			}
			double [] values = new double [attributeNames.size()];
			for (int i = 0; i < attributeNames.size(); i++){
				values[i] = Double.parseDouble (trimmed.get(i+1));
			}
			if (hasClass){
				records.add (new Record (line[0], values, line[line.length-1]));
			}
			else{
				records.add (new Record (line[0], values, null));
			}
		}
		recordsFile.close();
		
		if (arg.equalsIgnoreCase("e1")){
			//run experiment 1
			e1 (records);
		}
		else if (arg.equalsIgnoreCase("e2")){
			e2 (records, classes);
		}
		else{
			int k = Integer.parseInt(args[2]);
			if (k > records.size()){
				System.out.println("Invalid k, try again with value smaller than your number of records");
				return;
			}
			double threshold = Double.parseDouble (args[3]);
			int actualAnoms = actual (records, classes);
			outlierScoreAlg(records, k);
			double foundAnoms = 0;
			int tp = 0;
			int fp = 0;
			int fn = 0;
			int tn = 0;
			for (int i = 0; i < records.size(); i++){
				Record r = records.get(i);
				double os = r.outlierScore;
				if (os < threshold){
					//not an anomaly
					System.out.println(os);
					if (r.c.equals (anomClass)){
						//class is equal to anomaly class
						fn ++; //false neg
					}
					else{
						//class isnt equal to anomaly class
						tn ++; //true neg
					}
				}
				else {
					//is an anomaly
					foundAnoms += 1;
					if (r.c.equals (anomClass)){
						//is equal to anomaly class
						tp++; //true pos
					}
					else{
						//isn't equal to anomaly class
						fp++; //false pos
					}
				}
			}
			System.out.println("True Positive: " + tp/(double)actualAnoms);
			//System.out.println("False Positive: " + fp);
			System.out.println("False Positive: " + fp/(double)(records.size() - actualAnoms));
			//System.out.println("True Negative: " + tn);
			System.out.println("Accuracy: " + (tn + tp)/(double)records.size() * 100 + "%");
		}
	}

}
