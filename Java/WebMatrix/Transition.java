import java.util.Scanner;

public class Transition {

    private static final double LEAP2 = .10;
    private static final double LEAP1 = .90;

    public static void main (final String[] args) {

        Scanner stdIn = new Scanner (System.in);

        int n = stdIn.nextInt (); // number of pages
        int[][] counts = new int[n][n]; // counts[i][j] = # links from page i to
                                        // page j
        int[] outDegree = new int[n]; // outDegree[j] = # links from page i to
                                      // anywhere

        // Accumulate link counts.
        while (stdIn.hasNextInt ()) {
            int i = stdIn.nextInt ();
            int j = stdIn.nextInt ();
            outDegree[i]++;
            counts[i][j]++;
        }

        System.out.println (n + " " + n);

        // Print probability distribution for row i.
        for (int i = 0; i < n; i++) {

            // Print probability for column j.
            for (int j = 0; j < n; j++) {
                double p = LEAP1 * counts[i][j] / outDegree[i] + LEAP2 / n;
                System.out.printf ("%7.5f ", p);
            }
            System.out.println ();
        }
    }
}
