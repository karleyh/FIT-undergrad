import java.util.Random;
import java.util.Scanner;

public class BooleanMatrix {

    private static Scanner stdIn = new Scanner (System.in);
    private static final Random RNG = new Random (
            Long.getLong ("seed", System.nanoTime()));

    // read matrix from standard input
    public static boolean[][] read () {
        final int n = stdIn.nextInt ();
        final boolean[][] a = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (stdIn.nextInt () != 0) {
                    a[i][j] = true;
                }
            }
        }
        return a;
    }

    // print matrix to standard output
    public static void print (final boolean[][] a) {
        final int n = a.length;
        System.out.println (n);
        for (int i = 0; i < n; i++) {
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

    // random N-by-N matrix, where each entry is true with probability p
    public static boolean[][] random (final int n, final double p, final Random rnd) {
        final boolean[][] a = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                a[i][j] = rnd.nextDouble () < p;
            }
        }
        return a;
    }

    // display the matrix using standard draw
    // depending on variable which, plot true or false entries in foreground
    // color
    public static void show (final boolean[][] a, final boolean which) {
        final int n = a.length;
        StdDraw.setXscale (0, n - 1);
        StdDraw.setYscale (0, n - 1);
        final double r = 0.5;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (a[i][j] == which) {
                    StdDraw.filledSquare (j, n - i - 1, r);
                }
            }
        }
    }

    // test client
    public static void main (final String[] args) {
        final int n = Integer.parseInt (args[0]);
        final double p = Double.parseDouble (args[1]);

        final boolean[][] a = random (n, p, RNG);
        print (a);
        show (a, true);
    }

}
