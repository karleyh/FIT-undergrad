/*
 * Author:  Karley Herschelman, kherschelman2012@my.fit.edu
 * Course:  CSE 4510, Section 01, Spring 2016
 * Project: hw4, Kmeans
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;


public class HW4 {
	/**
	 * Stores a record from the input
	 * values stores location coordinates
	 * c stores the record's class
	 * id stores the name of the record
	 * @author Karley
	 */
	private static class Record{
		protected String id;
		protected double [] values;
		protected String c;
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
		@Override
		public boolean equals(Object o){
			Record r = (Record)o;
			if (Arrays.equals(values, r.values)){
				return true;
			}
			return false;
		}
	}
	
	/**
	 * Stores information about a cluster
	 * a, b, and SC are used to find sillhoette coefficient
	 * find Sillhoette Coeffient of the cluster using findSC(Cluster [] c)
	 * find centroid of the cluster using findCentroid()
	 * find SSE of the cluster using findSSE()
	 * @author Karley
	 *
	 */
	private static class Cluster{
		protected ArrayList<Record> records = new ArrayList<Record>();
		protected Record centroid = null;
		protected double sse = -1;
		protected double SC = 0;
		protected double[] a;
		protected double[] b;
		/** 
		 * Find a coeff of this cluster
		 */
		protected void findA(){
			a = new double [records.size()];
			if (records.size() > 1){
				for (int i = 0; i < a.length; i++){
					for (int j = 0; j < records.size(); j++){
						if (i != j){
							a[i] += Math.abs(records.get(i).distance(records.get(j)));
						}
					}
					a[i] /= (records.size() -1);
					//System.out.println("A" + i + "=" + a[i]);
				}
			}
		}
		/**
		 * Find b coeff of this cluster
		 * @param c Cluster[]
		 */
		protected void findB(Cluster[] c){  
			b = new double [records.size()];
			double min = 10000;
			for (int i = 0; i < b.length; i++){
				//get record from this cluster
				Record r = records.get(i);
				for (int j = 0; j < c.length; j++){
					double bt = 0;
					Cluster cl = c[j];
					//loop through all clusters except the cluster with the current record
					if (!cl.records.contains (r)){
						//sum distances from record to all records in cluster
						for (int l = 0; l < cl.records.size();l++){
							bt += Math.abs(r.distance(cl.records.get(l)));
						}
						//average
						bt /= cl.records.size();
						//use b@i to nearest neighbour cluster
						if (bt < min){
							min = bt;
						}
					}
				}
				b[i] = min;
			}
		}
		/**
		 * Find sillhoette coeffient of the cluster
		 * @param c Cluster[]
		 */
		protected void findSC(Cluster[] c){
			findA();
			findB(c);
			//double [] s= new double[a.length];
			for (int i = 0; i < a.length; i++){
				if (b[i] == 0){
					SC = 0;
				}
				else{
					SC +=  (1-(a[i]/b[i]));
				}
			}
			SC/= a.length;
		}
		public Cluster (){
			
		}
		/** 
		 * find the centroid of the cluster
		 */
		public void findCentroid(){
			double[] location = new double[records.get(0).values.length];
			//sum up each coordinate and average
			for (int j = 0; j < location.length; j++){
				for (int i = 0; i < records.size(); i++){
					location[j] += records.get(i).values[j];
				}
				location [j] /= records.size();
			}
			centroid = new Record (null, location, null);
		}
		/**
		 * find standard error by finding the ditance from each record to centroid
		 */
		public void findSSE(){
			sse = 0;
			if (centroid == null){
				findCentroid();
			}
			for (int i = 0; i < records.size(); i++){
				Record r = records.get(i);
				sse += Math.pow(r.distance(centroid), 2);
			}
		}
		public boolean isEmpty(){
			if (records.size() == 0){
				return true;
			}
			return false;
		}
		/**
		 * Print cluster's records ids
		 */
		@Override
		public String toString(){
			String s = "{" + records.get(0).id;
			for (int i = 1; i < records.size(); i++){
				s+= "," + records.get(i).id + " " + sse;
			}
			s+= "}";
			return s;
		}
	}
	
	/**
	 * Find Silhouette Coefficient of the cluster list
	 * @param c
	 */
	private static void silhouetteCoefficient (Cluster [] c){
		double silhouetteCoeff = 0;
		//sum the Silhouette Coefficient of each cluster
		for (int i = 0; i < c.length; i++){
			c[i].findSC(c);
			silhouetteCoeff += c[i].SC;
		}
		//divide by the number of clusters
		silhouetteCoeff /= c.length;
		System.out.println("Silhouette Coefficient: " + silhouetteCoeff);
	}
	
	/**
	 * Find Rand Statistic of records that have a class
	 * @param clusters Cluster[]
	 * @param records ArrayList<Record>
	 */
	private static void rand (Cluster[] clusters, ArrayList<Record> records){
		double f00 = 0; //same class, same cluster
		int f11 = 0; //different class, different cluster
		double T = records.size() * (records.size() -1) /2;
		//find number of pairs in same class and same cluster
		//loop through cluster
		for (int i = 0; i < clusters.length; i++){
			Cluster c = clusters[i];
			//compare all values to see if they belong to the same class
			for (int j = 0; j < c.records.size(); j++){ //m * m
				for (int l = 0; l < c.records.size(); l++){
					if (l != j){ //m(m-1)
						if (c.records.get(j).c.equals(c.records.get(l).c)){
							f11 ++;
						}
					}
				}
			}
		}
		f11 /=2; //(j,l) = (l,j)
		//find the number of pairs in different classes and different clusters
		for (int i = 0; i < records.size(); i++){
			Record a = records.get(i);
			for (int j = 0; j < records.size(); j++){
				Record b = records.get(j);
				for (int l = 0; l < clusters.length; l++){
					if (!a.c .equals(b.c)){
						//in different classes
						if (clusters[l].records.contains(a) && !clusters[l].records.contains(b)){
							//and in different clusters
							f00 ++;
						}
					}
				}
			}
		}
		f00 /= 2; //(j,l) = (l,j)
		//System.out.println(f00 + " " + f11 + " " + T);
		double rand = f00 + f11;
		rand/=T;
		System.out.println("Rand Statistic: " + rand);
	}
	
	/**
	 * K means algorithms
	 * @param records ArrayList<Record>
	 * @param k number of clusters (int)
	 * @return Cluster[] of k clusters in the records
	 */
	private static Cluster[] kmeans (ArrayList<Record> records, int k){
		Cluster [] clusters = new Cluster [k];
		Random rand = new Random();
		boolean [] recordUsed = new boolean [records.size()];
		int x = 0;
		//generate k random centroids
		for (int i = 0; i < k; i ++){
			//don't repeat records
			do {
				//select k random values
				x = rand.nextInt(records.size() -1);
			}while (recordUsed[x]);
			//get the record at the random index x
			Record r = records.get(x);
			// mark that record as used 
			recordUsed [x] = true;
			//make that record the centroid of the cluster
			clusters[i] = new Cluster();
			clusters[i].centroid = r;
		}
		boolean centroidChange = true;
		//loop until centroid stops changing
		while (centroidChange){
			double min = 0;
			//loop through all records
			for (int i = 0; i < records.size(); i++){
				Record r = records.get(i);
				min = clusters[0].centroid.distance(r);
				int cluster = 0;
				//assign to the cluster that's centroid is the min distance
				for (int j = 1; j < clusters.length; j++){
					double distance = clusters[j].centroid.distance(r);
					if (distance < min){
						min = distance;
						cluster = j;
					}
				}
				clusters[cluster].records.add(r);
			}
			centroidChange = false;
			for (int i = 0; i < clusters.length; i++){
				//note the current centroid
				Record centroid = clusters[i].centroid;
				//find the new centroid
				clusters[i].findCentroid();
				//check if the centroid is still changing
				if (!clusters[i].centroid.equals(centroid)){
					centroidChange = true;
				}
			}
			if (centroidChange){
				//as long as it is changing, clear the clusters of their records
				for (int i = 0; i < k; i++){
					clusters[i].records.clear();
				}
			}
		}
		return clusters;
	}
	
	/**
	 * Bisecting K means algorithm
	 * @param records ArrayList<Record>
	 * @param k number of clusters
	 * @return ArrayList<Cluster> with k clusters from the records
	 */
	private static ArrayList<Cluster> bisectingkmeans (ArrayList<Record> records, int k){
		//create k clusters
		ArrayList<Cluster> clusters = new ArrayList<Cluster>();
		//initialize the first cluster to contain all the records
		Cluster c = new Cluster();
		for (int i = 0; i < records.size(); i++){
			c.records.add(records.get(i)); 
		}
		c.findCentroid(); //find the centroid of the cluster
		c.findSSE(); //find the SSE of the cluster
		clusters.add(c); //add the cluster to the list of clusters
		//keep adding clusters until reaching k
		while (clusters.size () < k){
			double max = 0;
			int x = 0;
			//find the cluster with the max sse
			for (int i = 0; i < clusters.size(); i++){
				if (clusters.get(i) == null){
					break;
				}
				if (clusters.get(i).sse == -1){
					clusters.get(i).findSSE();
				}
				if (clusters.get(i).sse > max){
					max = clusters.get(i).sse;
					x = i;
				}
			}
			c = clusters.get(x);
			double minSSE = c.sse;
			Cluster bestClusters [] = {c, c};
			ArrayList<Cluster[]> allBisected= new ArrayList<Cluster[]>();
			for (int i = 0; i < 10; i++){
				//split the cluster into k clusters 
				if (c.records.size() >= 2){
					Cluster[] bisected = kmeans (c.records, 2);
					double sse = 0;
					for (int a = 0; a < 2; a++){
						bisected[a].findSSE();
						sse += bisected[a].sse;
					}
					sse/=2.0;
					if (sse < minSSE){
						minSSE = sse;
						for (int a = 0; a < 2; a++){
							bestClusters[a] = bisected[a];
						}
					}
				}
			}
			//remove the cluster that was just split from the list
			clusters.remove (x);
			//add the two new clusters
			for (int f = 0; f < 2; f++){
				clusters.add (bestClusters[f]);
			}
			
		}
		return clusters;
	}
	
	
	/**
	 * Normalizes records to values between 0 and 1
	 * @param records
	 */
	private static void normalize(ArrayList<Record> records){
		double[] max = new double [records.get(0).values.length];
		double[] min = new double [records.get(0).values.length];
		//initialize min values
		for (int i = 0; i < min.length; i++){
			min[i] = records.get(0).values[i];
		}
		//find min and max values
		for (int i = 0; i < records.size(); i++){
			for (int j = 0; j < min.length; j++){
				double value = records.get(i).values[j];
				if (value > max[j]){
					max[j] = value;
				}
				if (value < min[j]){
					min[j] = value;
				}
			}
		}
		//normalize each record
		for (int i = 0; i < records.size(); i++){
			for (int j = 0; j < min.length; j++){
				double value = records.get(i).values[j];
				records.get(i).values[j] = (value - min[j])/(max[j]- min[j]);
			}
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
		Scanner namesFile = new Scanner (new File (args[0]));
		namesFile.nextLine(); //throw out the first line
		boolean hasClass = false;
		boolean normalize = false;
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
		
		Scanner recordsFile = new Scanner (new File (args[1]));
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
		// get number of clusters
		int k = Integer.parseInt(args[2]);
		//get boolean for normalizing
		normalize = Boolean.getBoolean(args[3]);
		if (normalize){
			normalize(records);
		}
		//can't have more clusters than records
		if (k <= records.size()){
			//computer k means
			Cluster[] clusters = kmeans (records, k);
			//compute bisecting k means
			ArrayList<Cluster> bisecting = bisectingkmeans (records, k);
			//print k means
			System.out.println("Kmeans:");
			for (int i = 0; i < k; i++){
				System.out.println(clusters[i].toString());
			}
			//print silhouette coefficient and rand statistic (if has class) of clusters
			silhouetteCoefficient(clusters);
			if (hasClass){
				rand(clusters, records);
			}
			Cluster[]  bisArray = new Cluster[bisecting.size()];
			//print bisecting k means and copy to an array
			System.out.println("\nBisecting Kmeans:");
			for (int i = 0; i < bisecting.size(); i++){
				System.out.println(bisecting.get(i).toString());
				bisArray [i] = bisecting.get(i);
			}
			//print silhouette coefficient and rand statistic (if has class) of clusters
			silhouetteCoefficient (bisArray);
			if (hasClass){
				rand (bisArray, records);
			}
		}
		else{
			System.out.println("k should be less than or equal to the number of records");
		}
	}

}
