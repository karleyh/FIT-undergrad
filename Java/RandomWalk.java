import java.util.Random;

public class RandomWalk {
    private static final Random RNG = new Random(Long.getLong("seed",
            System.nanoTime()));

    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]); // north/east
        int T = Integer.parseInt(args[1]); // attempts
        int xCoord = N / 2;
        int yCoord = N / 2;
        int[] x = new int[50];
        int[] y = new int[50];
        int count = 0; // one dead end
        int t = 0; // 4 dead ends at once
        for (int i = 0; i < 50; i++) {
            if (t == T) {
                break;
            }
            x[i] = xCoord; // place coordinate where dog moved to into the
                           // array. Note: this may be the same coordinate as
                           // previous, if dog fails to move.
            y[i] = yCoord;
            int XorY = RNG.nextInt(2); // X is 0
            int PorM = RNG.nextInt(2); // + is 0
            if (XorY == 0) {
                if (PorM == 0) {
                    xCoord++;
                }
                if (PorM == 1) {
                    xCoord--;
                }
            }
            if (XorY == 1) {
                if (PorM == 0) {
                    yCoord++;
                }
                if (PorM == 1) {
                    yCoord--;
                }
            }

            for (int j = 0; j < 50; j++) {
                if ((x[j] == xCoord) && (y[j] == yCoord)) { // undo the move
                    if (XorY == 0) {
                        if (PorM == 0) {
                            xCoord--;
                        }
                        if (PorM == 1) {
                            xCoord++;
                        }
                    }
                    if (XorY == 1) {
                        if (PorM == 0) {
                            yCoord--;
                        }
                        if (PorM == 1) {
                            yCoord++;
                        }
                    }
                    count++; // counts number of times dead ends
                } else {
                    count = 0; // resets dead end count when dog makes a valid
                               // move
                }

            }
            if ((xCoord >= N) || (xCoord <= 0) || (yCoord >= N) // when dog
                                                                // exits,
                                                                // loop ends
                    || (yCoord <= 0)) {
                for (int a = 0; a < 50; a++) {
                    System.out.println(x[a] + "," + y[a]);
                }
                break;
            }
            if (count >= 4) { // if dog dead ends in all four directions, it
                              // retreats and this attempt is noted
                for (int k = 0; k < 50; k++) {
                    x[k] = 0;
                    y[k] = 0;
                }
                t++;
                break;
            }
            // System.out.println(xCoord + "," + yCoord);
        }
    }

}
