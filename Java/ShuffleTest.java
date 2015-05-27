import java.util.Random;
import java.util.Scanner;

public class ShuffleTest {
    private static final Random RNG = new Random(Long.getLong("seed",
            System.nanoTime()));

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int shuffle = Integer.parseInt(args[0]);
        int rowCount = Integer.parseInt(args[1]);
        int[] rows = new int[rowCount];
        int[][] results = new int[rowCount][rowCount];
        int[][] count = new int[rowCount][rowCount];

        for (int i = 0; i < rowCount; i++) {
            // for (int j = 0; j < rowCount; j++) {
            rows[i] = i;
            count[i][i] = 0;
            // }
        }
        for (int j = 0; j < rowCount; j++) {
            for (int i = 0; i < rowCount; i++) {
                int r = i + (RNG.nextInt(rowCount - i));
                int t = rows[r];
                rows[r] = rows[i];
                rows[i] = t;
                results[j][i] = rows[i];
                // System.out.println(results[j][i]);
            }

        }
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < rowCount; j++) {
                if (i == results[i][j]) {
                    count[i][j]++;
                }
            }
        }
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < rowCount; j++) {
                System.out.print(count[i][j]);
                if (j != rowCount){
                   System.out.print(" "); 
                }
            }
            if (i != rowCount) {
                System.out.println("");
            }
        }

    }

}
