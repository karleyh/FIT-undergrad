import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;


public class HW3 {
	protected static class Rule{
		ArrayList<String> antecedent;
		String consequent;
		protected Rule(String consequent){
			this.consequent = consequent;
			antecedent = new ArrayList<String>();	
		}
		@Override
		public String toString(){
			String s = "{"+ antecedent.get(0);
			for (int i = 1 ; i < antecedent.size(); i++){
				s += ", " + antecedent.get(i);
			}
			s += "} -> {" + consequent + "}";
			return s;
		}
	}
	protected static class ItemSet{   
		ArrayList<String> items;
		int [] itemIndexes;
		double support;
		//boolean[] subsetOf;
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

			support = matches / names.length;
		}
	   @Override
	   public boolean equals(Object v) {
		   ItemSet i = (ItemSet) v;
		   if (this.items.equals(i.items)){
			   return true;
		   }
		  return false;
	   }
	}
	private static double mincof = 0;
	private static double minsup = 0;
	public static void main (String args[]) throws FileNotFoundException{
		ArrayList<String> attributeNames = new ArrayList<String>();
		ArrayList<boolean[]> recordsList = new ArrayList<boolean[]>();
		Scanner namesFile = new Scanner (new File (args[0]));
		while (namesFile.hasNextLine()){
			attributeNames.add(namesFile.next());
			namesFile.next();
			namesFile.next();
		}
		namesFile.close();
		String[] names = new String[attributeNames.size()];
		for (int i = 0; i < names.length; i++){
			names[i] = attributeNames.get(i);
		}
		Scanner recordsFile = new Scanner (new File (args[1]));
		while (recordsFile.hasNextLine()){
			String[] line = recordsFile.nextLine().split(" ");
			boolean[] bline = new boolean[line.length];
			for (int i = 0; i < line.length; i++){
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
		boolean[][] records = new boolean[recordsList.size()][attributeNames.size()];
		for (int i = 0; i < records.length; i++){
			records[i] = recordsList.get(i);
		}
		minsup = Double.parseDouble(args[2]);
		mincof = Double.parseDouble(args[3]);
		frequentItemSetGeneration (names, records);
	}		
	
	public static void printRules (ArrayList <Rule> rules){
		for (int i = 0; i < rules.size(); i++){
			System.out.println(rules.get(i).toString());
		}	
	}
	
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
	
	private static ArrayList<ArrayList<ItemSet>> frequentItemSetGeneration (String [] names, boolean[][] records){
		System.out.println("Algorithm 6.1");
		ArrayList<ItemSet> fk = new ArrayList<ItemSet>();
		<ArrayList<ItemSet> freq = new <ArrayList<ItemSet>();
		//find all frequent 1-itemsets
		for (int i = 0; i < names.length; i++){
			ArrayList<String> set = new ArrayList<String>();
			set.add(names[i]);
			ItemSet itemset = new ItemSet (names, records, set);
			if (itemset.support >= minsup){
				fk.add((itemset));
			}
			freq.add (fk);
		}
		System.out.println("k = 1");
		System.out.println("Count before pruning: " + names.length);
		System.out.println("Count after pruning: " + fk.size());
		for (int j = 0; j < fk.size(); j++){
			for (int k = 0; k < fk.get(j).items.size(); k++){
				System.out.print(fk.get(j).items.get(k) + " ");
			}
			System.out.println("");
		}
		//remove values that don't meet minsup
		//generate candidate itemsets of size i+1
		for (int i = 1; i < names.length; i++){
			//generate using Fk-1 x Fk-1
			if (fk.size() <= 1){
				System.out.println("Count after pruning: " + fk.size());
				System.out.println("STOP MERGING");
				break;
			}
			//get frequent item sets
			fk = fk1xfk1 (names, records, fk, i+1);
			//print Fk
			System.out.println("Count after pruning: " + fk.size());
			for (int j = 0; j < fk.size(); j++){
				//add itemset to combined list
				freq.add (fk.get(j));
				for (int k = 0; k < fk.get(j).items.size(); k++){
					//print items in itemset
					System.out.print(fk.get(j).items.get(k) + " ");
				}
				System.out.println("");
			}
			//union in a list
			ag2(freq);
		}
		return freq;
	}
	private static void a62 (ArrayList<ArrayList<ItemSet>> freq){
		//skip itemsets of size 1
		//create initial rule list with consequent of size 1
		ArrayList<Rule> R1 = new ArrayList <Rule>();
		ItemSet f1;
		for (int i = 1; i < freq.size(); i++){
			for (int j = 0; j < freq.get(i).size(); i++){
				f1 = freq.get(i).get(j);
				for (int k = 0; k <  f1.items.size(); k ++){
					Rule r = new Rule (f1.items.get(k));
					for (int l = 0; l < f1.items.size(); l++)
						if (l != k)
							r.antecedent.add(f1.items.get(l));
				}
			}
		}
	}
	
	private static ArrayList<Rule> aprioriMerge (ArrayList<Rule> rules){
		ArrayList<Rule> newRules = new ArrayList<Rule>();
		 //merge together like values in rules to form a new rule
		return newRules;
	}
}