import java.util.Random;
import java.util.Scanner;

public class VerticalPercolation {

    private static final double SIZE_HALF = .5;
    private static Scanner stdIn = new Scanner (System.in);

    // given an N-by-N matrix of open sites, return an N-by-N matrix
    // of sites reachable from the top via a vertical path of open sites
    public static boolean[][] flow (final boolean[][] open) {
        final int n = open.length;
        final boolean[][] full = new boolean[n][n];

        // identify full sites in row 0
        for (int j = 0; j < n; j++) {
            full[0][j] = open[0][j];
        }

        // update remaining rows
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < n; j++) {
                full[i][j] = open[i][j] && full[i - 1][j];
            }
        }

        return full;
    }

    // does the system percolate?
    public static boolean percolates (final boolean[][] open) {
        final int n = open.length;
        final boolean[][] full = flow (open);
        for (int j = 0; j < n; j++) {
            if (full[n - 1][j]) {
                return true;
            }
        }
        return false;
    }

    // draw the N-by-N boolean matrix to standard draw
    public static void show (final boolean[][] a, final boolean which) {
        final int n = a.length;
        StdDraw.setXscale (-1, n);
        StdDraw.setYscale (-1, n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (a[i][j] == which) {
                    StdDraw.filledSquare (j, n - i - 1, SIZE_HALF);
                }
            }
        }
    }

    // return a random N-by-N boolean matrix, where each entry is
    // true with probability p
    public static boolean[][] random (final int n, final double p, final Random rnd) {
        final boolean[][] a = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                a[i][j] = rnd.nextDouble () < p;
            }
        }
        return a;
    }

    public static boolean readBoolean () {
        final String s = stdIn.next ();
        if (s.equalsIgnoreCase ("true")) {
            return true;
        }
        if (s.equalsIgnoreCase ("false")) {
            return false;
        }
        if (s.equals ("1")) {
            return true;
        }
        if (s.equals ("0")) {
            return false;
        }
        throw new java.util.InputMismatchException ();
    }

    public static boolean[][] readBoolean2D () {
        final int m = stdIn.nextInt ();
        final int n = stdIn.nextInt ();
        final boolean[][] a = new boolean[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                a[i][j] = readBoolean ();
            }
        }
        return a;
    }

    public static void print (final boolean[][] a) {
        final int m = a.length;
        final int n = a[0].length;
        System.out.println (m + " " + n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (a[i][j]) {
                    System.out.print ("1 ");
                } else {
                    System.out.print ("0 ");
                }
            }
            System.out.println ();
        }
    }

    // test client
    public static void main (final String[] args) {

        final boolean[][] open = readBoolean2D ();
        print (flow (open));
        System.out.println (percolates (open));
    }

}
