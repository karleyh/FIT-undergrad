import java.util.Random;

public class Estimate {

    private static final Random RNG = new Random (
            Long.getLong ("seed", System.nanoTime()));
    
    // do M trials and return fraction that percolate
    public static double eval (final int n, final double p, final int m, final Random rnd) {
        int count = 0;
        for (int k = 0; k < m; k++) {
            final boolean[][] open = Percolation.random (n, p, rnd);
            if (Percolation.percolates (open)) {
                count++;
            }
        }
        return (double) count / m;
    }

    public static void main (final String[] args) {
        final int n = Integer.parseInt (args[0]);
        final double p = Double.parseDouble (args[1]);
        final int m = Integer.parseInt (args[2]);

        final double q = eval (n, p, m, RNG);
        System.out.println (q);
    }
}
