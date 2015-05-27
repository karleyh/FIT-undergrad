/*
 * Author: Karley Herschelman, kherschelman2012@my.fit.edu
 * Course: CSE 1002, Section 01/02, Fall 2013
 * Project: percolation
 */
import java.util.Random;

public class Estimate2 {

    private static final Random RNG = new Random(Long.getLong("seed",
            System.nanoTime()));

    // do M trials and return fraction that percolate
    public static double eval (final int n, final double p, final int m,
            final Random rnd) {
        int count = 0;
        int countDirected = 0;
        for (int k = 0; k < m; k++) {
            final boolean[][] open = Percolation.random(n, p, rnd);
            if (Percolation.percolates(open)) {
                count++;
            }
            if (PercolationDirected.percolates(open)) { // counts the number
                                                        // that percolates
                                                        // without going up
                countDirected++;
            }
        }
        return (double) (count - countDirected) / m; // returns difference as a
                                                     // percent
    }

    public static void main (final String[] args) {
        final int n = Integer.parseInt(args[0]);
        final double p = Double.parseDouble(args[1]);
        final int m = Integer.parseInt(args[2]);

        final double q = eval(n, p, m, RNG);
        System.out.println(q);
    }
}
