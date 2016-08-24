import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class HW3 {
	protected static class Rule{
		ArrayList<String> antecedent;
		String consequent;
		protected Rule(){
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
			names[i] = attributeNames.get(0);
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
		double minsup = Double.parseDouble(args[2]);
		double mincof = Double.parseDouble(args[3]);
	}		
	
	public static void printRules (ArrayList <Rule> rules){
		for (int i = 0; i < rules.size(); i++){
			System.out.println(rules.get(i).toString());
		}	
	}
	
	public static void frequentItemsetGeneration (){
		
	}
}
