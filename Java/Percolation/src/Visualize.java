import java.util.Random;

public class Visualize {
    private static final int ONE_SECOND_WAIT = 1000;
    private static final int SEED_ARGUMENT_LENGTH = 3;

    public static void main (final String[] args) {
        final int n = Integer.parseInt (args[0]);
        final double p = Double.parseDouble (args[1]);
        final int m = Integer.parseInt (args[2]);

        Random rnd;

        if (args.length > SEED_ARGUMENT_LENGTH) {
            rnd = new Random (Integer.parseInt (args[SEED_ARGUMENT_LENGTH]));
        } else {
            rnd = new Random ();
        }

        // repeatedly created N-by-N matrices and display them using standard
        // draw
        for (int i = 0; i < m; i++) {
            final boolean[][] open = Percolation.random (n, p, rnd);
            StdDraw.clear ();
            StdDraw.setPenColor (StdDraw.WHITE);
            Percolation.show (open, false);
            StdDraw.setPenColor (StdDraw.BLACK);
            final boolean[][] full = Percolation.flow (open);
            Percolation.show (full, true);
            StdDraw.show (ONE_SECOND_WAIT);
        }
    }
}
