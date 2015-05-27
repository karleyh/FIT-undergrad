/*
*Author: Karley Herschelman, kherschelman2012@my.fit.edu
*Course: CSE 1002, Section 01/02, Fall 2013
*Project: Knitting
*/

import java.util.Scanner;

public class Knitting {

    public static void main (final String[] args) {

        final Scanner scanner = new Scanner (System.in);
        while (scanner.hasNextInt()) {
            final int firstRow = scanner.nextInt();
            final int rows = scanner.nextInt();
            final int rowsPattern = scanner.nextInt();
            if ((firstRow == 0) && (rows == 0) && (rowsPattern == 0)) {
                System.exit(0);
            }
            final int[] pattern = new int[rowsPattern];
            int rowSum = firstRow;
            int total = firstRow;

            for (int i = 0; i < rowsPattern; i++) {
                pattern[i] = scanner.nextInt();
            }

            for (int j = 0; j < (rows - 1); j++) {
                int i = j;
                if (i >= rowsPattern) {
                    i = i % rowsPattern;
                }
                rowSum += pattern[i];
                total += rowSum;
            }
            System.out.println(total);

        }

    }

}
