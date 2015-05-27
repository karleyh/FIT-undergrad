/*
 * Author: Karley Herschelman, kherschelman2012@my.fit.edu
 * Course: CSE 1002, Section 01/02, Fall 2013
 * Project: molecularweight
 */
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class MolecularWeight {

    public static void main (final String[] args) throws IOException {

        final Scanner file = new Scanner(new File(args[0])); // to read the file
        final Scanner sc = new Scanner(System.in);
        file.useDelimiter("[\\s,]+");
        final ArrayList<Entry> symbolList = new ArrayList<Entry>();
        // skip first line
        file.nextLine();
        // skip first two values in the line that are not needed
        while (file.hasNext()) {
            for (int i = 0; i < 2; i++) {
                file.next();
            }
            // get two values needed
            final String symbol = file.next();
            final double weight = file.nextDouble();
            // skip over the rest of the line
            file.nextLine();
            // store two values in Entry
            final Entry element = new Entry(symbol, weight);
            symbolList.add(element);
        }

        weight(sc, symbolList);

        file.close();
        sc.close();

    }

    // calculates weight and outputs
    private static void weight (final Scanner sc,
            final ArrayList<Entry> symbolList) {
        double total = 0; // final weight
        String formula = ""; // final formula
        while (sc.hasNext()) {
            double nextWeight = 0;
            final String nextSymbol = sc.next();
            // finds end of formula, prints and resets values
            if (nextSymbol.equals(".")) {
                System.out.println("Molecular weight of" + formula + " = "
                        + total);
                total = 0;
                formula = "";
            } else {
                // adds symbol to the final formula if it is not a "."
                formula += " " + nextSymbol;
                // next Count is 1 by default (x is the same as 1x)
                int nextCount = 1;
                if (sc.hasNextInt()) {
                    // if the next value is an int, add that to the formula and
                    // change the count
                    nextCount = sc.nextInt();
                    formula += " " + nextCount;
                }
                for (int i = 0; i < symbolList.size(); i++) {
                    // match the symbol with the weight
                    final Entry symbol = symbolList.get(i);
                    if ((symbol.symbol).equals(nextSymbol)) {
                        nextWeight = symbol.weight;
                        break;
                    }
                }
                // find the total using the weight that was found and the count
                total += nextCount * nextWeight;
                // if the symbol was not found the whole input is invalid
                if (nextWeight == 0) {
                    System.out.println("Unknown Molecular equation");
                    sc.nextLine();
                    formula = "";
                }
            }
        }
    }

    // stores values needed
    static class Entry {
        public final String symbol;
        public final double weight;

        public Entry(final String symbol, final double weight) {
            this.symbol = symbol;
            this.weight = weight;
        }
    }

}
