/*
 * Author:  Karley Herschelman, kherschelman2012@my.fit.edu
 * Course:  CSE 4510, Section 01, Spring 2016
 * Project: hw3, Apriori Rule Generation
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class HW3 {
	/**
	 * 
	 * @author Karley
	 * Stores information about an itemset
	 */
	private static class ItemSet{
		protected ArrayList <String> items;
		protected double support;
		/**
		 * ItemSet Contructor
		 * @param names list of attribute names
		 * @param records list of boolean records
		 * @param itemSet String list of items to become itemset
		 * calculates the support of the itemset and stores it with the list
		 */
		protected ItemSet (String [] names, boolean[][] records, ArrayList<String> itemSet){
			items = itemSet;
			//find the indexes for the items in the set
			int [] itemIndexes = new int [itemSet.size()];
			for (int i = 0; i < itemSet.size(); i++){
				for (int j = 0; j < names.length; j++){
					if (itemSet.get(i).equals(names[j])){
						itemIndexes[i] = j;
						break;
					}
				}
			}
			//subsetOf = new boolean [records.length];
			//check how many records match the itemset
			double matches = 0;
			for (int i = 0; i < records.length; i++){
				boolean match = true;
				for (int j = 0; j < itemIndexes.length; j++){
					if (!records[i][itemIndexes[j]]){
						match = false;
					}
				}
				if (match){
					//subsetOf [i] = true;
					matches += 1;
				}
			}
			support = matches / records.length;
		}
		//used for contains
		@Override
	   public boolean equals(Object v) {
		   ItemSet i = (ItemSet) v;
		   if (this.items.equals(i.items)){
			   return true;
		   }
		  return false;
	   }
	}
	
	/**
	 * @author Karley
	 * Stores Rules
	 */
	private static class Rule{
		protected ItemSet consequent;
		protected ItemSet antecedent;
		protected Rule (){
		}
		@Override
		public String toString(){
			if (antecedent.items.size() > 0 && consequent.items.size() > 0){
				String s = "{"+ antecedent.items.get(0);
				for (int i = 1 ; i < antecedent.items.size(); i++){
					s += ", " + antecedent.items.get(i);
				}
				s += "} -> {" + consequent.items.get(0);
				for (int i = 1 ; i < consequent.items.size(); i++){
					s += ", " + consequent.items.get(i);
				}
				s+= "}";
				return s;
			}
			return null;
		}
	}
	
	private static double mincof = 0; //minimum confidence
	private static double minsup = 0; //minimum support
	/**
	 * gets input and begins execution
	 * @param args namesFile, recordsFile, minsup, minconf
	 * @throws FileNotFoundException
	 */
	public static void main (String args[]) throws FileNotFoundException{
		ArrayList<String> attributeNames = new ArrayList<String>();
		ArrayList<boolean[]> recordsList = new ArrayList<boolean[]>();
		Scanner namesFile = new Scanner (new File (args[0]));
		//read names file
		while (namesFile.hasNextLine()){
			attributeNames.add(namesFile.next());
			//they are all boolean, so dump that part
			namesFile.next();
			namesFile.next();
		}
		namesFile.close();
		//move to an immutable array
		String[] names = new String[attributeNames.size()];
		for (int i = 0; i < names.length; i++){
			names[i] = attributeNames.get(i);
		}
		Scanner recordsFile = new Scanner (new File (args[1]));
		//read records file
		while (recordsFile.hasNextLine()){
			String[] line = recordsFile.nextLine().split(" ");
			boolean[] bline = new boolean[line.length];
			for (int i = 0; i < line.length; i++){
				//parse data to true or false values
				if (line[i].equals ("0")){
					bline[i] = false;
				}
				else{
					bline[i] = true;
				}
			}
			recordsList.add (bline);
		}
		recordsFile.close();
		//copy to an immutable array
		boolean[][] records = new boolean[recordsList.size()][attributeNames.size()];
		for (int i = 0; i < records.length; i++){
			records[i] = recordsList.get(i);
		}
		//get minsup and minconf from args
		minsup = Double.parseDouble(args[2]);
		mincof = Double.parseDouble(args[3]);
		//start executing
		apriori (names, records);
	}	

	/**
	 * Algorithms 6.1
	 * Finds frequent itemsets
	 * @param names list of attribute names
	 * @param records list of boolean attributes
	 */
	private static void apriori (String [] names, boolean[][] records){
		ArrayList<ItemSet> fk = new ArrayList<ItemSet>();
		ArrayList<ArrayList<ItemSet>> freq = new ArrayList<ArrayList<ItemSet>> ();
		//find all frequent 1-itemsets
		for (int i = 0; i < names.length; i++){
			ArrayList<String> set = new ArrayList<String>();
			set.add(names[i]);
			ItemSet itemset = new ItemSet (names, records, set);
			if (itemset.support >= minsup){
				fk.add((itemset));
			}
		}
		System.out.println("k = 1");
		System.out.println("Count before pruning: " + names.length);
		System.out.println("Count after pruning: " + fk.size());
		for (int j = 0; j < fk.size(); j++){
			//freq.add (fk.get(j));
			for (int k = 0; k < fk.get(j).items.size(); k++){
				System.out.print(fk.get(j).items.get(k) + " ");
			}
			System.out.println("");
		}
		freq.add(fk);
		//remove values that don't meet minsup
		//generate candidate itemsets of size i+1
		for (int i = 1; i < names.length; i++){
			//generate using Fk-1 x Fk-1
			if (fk.size() <= 1){
				break;
			}
			//get frequent item sets
			fk = fk1xfk1 (names, records, fk, i+1);
			//print Fk
			System.out.println("Count after pruning: " + fk.size());
			for (int j = 0; j < fk.size(); j++){
				//add itemset to combined list
				//freq.add (fk.get(j));
				for (int k = 0; k < fk.get(j).items.size(); k++){
					//print items in itemset
					System.out.print(fk.get(j).items.get(k) + " ");
				}
				System.out.println("");
			}
			//union in a list
			freq.add(fk);
		}
		ag2(freq);
	}	
	
	/**
	 * Algorithm 6.2
	 * Generates rules with consequents of size 1 and calls recursive gen rules 
	 * @param freq list of frequent item sets
	 */
	private static void ag2(ArrayList<ArrayList<ItemSet>> freq) {
		//create initial rule list with consequent of size 1
		ItemSet f1;
		for (int i = 1; i < freq.size(); i++){
			for (int j = 0; j < freq.get(i).size(); j++){
				f1 = freq.get(i).get(j); //current itemset
				//each itemset has a cooresponding list of rules
				ArrayList<Rule> R1 = new ArrayList <Rule>();
				if (f1.items.size() > 1){
					for (int k = 0; k <  f1.items.size(); k ++){
						Rule r = new Rule ();
						ArrayList<String> c = new ArrayList<String>();
						ArrayList<String> a = new ArrayList<String>();
						c.add(f1.items.get(k));
						r.consequent = getItemSetFromList (freq, c);
						//System.out.println(r.consequent);
						for (int l = 0; l < f1.items.size(); l++){
							if (l != k)
								a.add(f1.items.get(l));
						}
						r.antecedent = getItemSetFromList (freq, a);
						R1.add (r);
					}
				}
				genRules (f1, R1, freq);
			}
		}
	}
	
	/**
	 * Algorithm 6.3
	 * Recursively generates rules by calling a function that merges rules
	 * @param set Itemset
	 * @param rules Rules list cooresponding to itemset
	 * @param freq list of all frequent itemsets
	 */
	private static void genRules (ItemSet set, ArrayList<Rule> rules, ArrayList<ArrayList<ItemSet>> freq){
		double conf;
		for (int i = 0; i < rules.size(); i++){
			//get next rule
			Rule r = rules.get(i);
			if (r.antecedent.support > 0){
				//calculate confidence (*/100 fixed rounding error?)
				conf = set.support * 100/r.antecedent.support;
				conf/=100;
			}
			else{conf = 0;}
			//if confidence is large enough
			if (conf >= mincof){
				//print
				System.out.println(r.toString());
			}
			else{
				//remove from list of rules
				rules.remove(r);
			}
		}
		//keep recursively calling while consequent is smaller than itemset
		if (rules.size() > 0)
			if (set.items.size() > rules.get(0).consequent.items.size() + 1)
				genRules (set, mergeRules(rules, freq), freq);
		return;
	}
	
	/**
	 * Merges rules together
	 * @param rules list of rules with consequents of size k
	 * @param freq list of frequent itemsets
	 * @return list of rules with consequents of size k+1
	 */
	private static ArrayList<Rule> mergeRules(ArrayList<Rule> rules, ArrayList<ArrayList<ItemSet>> freq){
		ArrayList<Rule> newRules = new ArrayList<Rule>();
		for (int i = 0; i < rules.size(); i++){
			for (int j = 0; j < rules.size(); j++){
				if (i != j){
					Rule a = rules.get (i);
					Rule b = rules.get (j);
					ArrayList<String> c = new ArrayList<String>();
					ArrayList<String> n = new ArrayList<String>();
					//generate the antecedent
					//compare values of a and b
					for (int k = 0; k < a.antecedent.items.size(); k++){
						if (b.antecedent.items.contains(a.antecedent.items.get(k))){
							if (!n.contains(a.antecedent.items.get(k))){
								n.add (a.antecedent.items.get(k));
							}
						}
					}
					//compare values of b and a
					for (int k = 0; k < b.antecedent.items.size(); k++){
						if (a.antecedent.items.contains(b.antecedent.items.get(k))){
							if (!n.contains(b.antecedent.items.get(k))){
								n.add (b.antecedent.items.get(k));
							}
						}
					}
					//generate the consequent
					if (n.size() > 0){
						for (int k = 0; k < a.consequent.items.size(); k++){
							c.add (a.consequent.items.get(k));
							c.add (b.consequent.items.get(k));
						}
						if (c.size() > 0){
							Rule merge = new Rule();
							//sort the itemset alphabetically
							Collections.sort(n, new Comparator<String>() {
						        @Override
						        public int compare(String s1, String s2) {
						            return s1.compareToIgnoreCase(s2);
						        }
						    });
							//sort the itemset alphabetically
							Collections.sort(c, new Comparator<String>() {
						        @Override
						        public int compare(String s1, String s2) {
						            return s1.compareToIgnoreCase(s2);
						        }
						    });
							merge.antecedent = getItemSetFromList (freq, n);
							merge.consequent = getItemSetFromList (freq, c);
							if (merge.antecedent != null && merge.consequent != null)
								newRules.add(merge);
						}
					}
				}
			}
		}
		
		return newRules;
	}
	
	/**
	 * merge itemsets into larger ones using F(k-1) x F(k-1)
	 * @param names list of attribute names
	 * @param records list of boolean records
	 * @param Fk list of itemsets of size k
	 * @param size size of itemsets
	 * @return list of itemsets of size k+1
	 */
	private static ArrayList<ItemSet> fk1xfk1 (String [] names, boolean[][] records, ArrayList<ItemSet> Fk, int size){
	//frequent itemsets 1 size larger than Fk
	ArrayList<ItemSet> Fk1 = new ArrayList<ItemSet>();
	ArrayList<String> itemSet = new ArrayList<String>();
	int before = 0;
	//generate the itemsets
	for (int i = 0; i < Fk.size(); i++){
		for (int j = 0; j < Fk.size(); j++){
			if (i != j){
				//get two smaller itemsets
				ArrayList<String> a = Fk.get(i).items;
				ArrayList<String> b = Fk.get(j).items;
				//merge the smaller itemsets
				itemSet = new ArrayList<String>();
				for (int k = 0; k < a.size(); k++){
					//add unique values from the two sets
					if (!itemSet.contains (a.get(k))){
						itemSet.add(a.get(k));
					}
					if (!itemSet.contains (b.get(k))){
						itemSet.add(b.get(k));
					}
					if (itemSet.size() > a.size()+1){
						break;
					}
				}
				if (itemSet.size() == (a.size()+1)){
					//sort the itemset alphabetically
					Collections.sort(itemSet, new Comparator<String>() {
				        @Override
				        public int compare(String s1, String s2) {
				            return s1.compareToIgnoreCase(s2);
				        }
				    });
					//check if the itemset is not already in the list
					ItemSet itemset = new ItemSet (names, records, itemSet);
					if (!Fk1.contains(itemset)){
						//check that the itemset has minimum support
						before ++;
						if (itemset.support >= minsup){
							Fk1.add (itemset);
						}
					}	
				}
			}
		}
	}
	System.out.println("k = " + size);
	System.out.println("Count before pruning: " + before);
	//return list of frequent itemsets of this size
	return Fk1;
	}
	
	/**
	 * Gets itemset from the list of frequent itemsets that has the same list of items as the arraylist
	 * @param freq list of itemsets
	 * @param a arraylist of strings representing items
	 * @return matching Itemset of null if itemset not found
	 */
	private static ItemSet getItemSetFromList (ArrayList<ArrayList<ItemSet>> freq, ArrayList<String> a){
		int row = a.size() -1; //each row of freq is the size of the itemset -1
		for (int i = 0; i < freq.get(row).size(); i++){
			if (freq.get(row).get(i).items.equals (a)){
				return freq.get(row).get(i);
			}
		}
		return null;
	}
}