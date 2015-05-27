/*
*Author: Karley Herschelman, kherschelman2012@my.fit.edu
*Course: CSE 1002, Section 01/02, Fall 2013
*Project: bigdecimal
*/
import java.math.BigDecimal;
import java.util.Scanner;

public class Mortgage {

    public static void main (final String[] args) {
        final Scanner sc = new Scanner(System.in);
        BigDecimal loan = new BigDecimal(args[0]);
        final BigDecimal rate = new BigDecimal(args[1]);
        final BigDecimal zero = new BigDecimal("0.00");
        while (sc.hasNext()) {
            final String next = sc.next();
            if (next.equalsIgnoreCase("balance")) {
                System.out.print("Balance: " + loan.abs());
                final int comparable = loan.compareTo(zero);
                if (comparable == 1) {
                    System.out.println(" left");
                }
                if (comparable == -1) {
                    System.out.println(" over");
                }
            } else {
                final BigDecimal payment = new BigDecimal(next);
                final BigDecimal interest = rate.multiply(loan);
                loan = loan.add(interest);
                loan = loan.subtract(payment);
                loan = loan.setScale(2, BigDecimal.ROUND_HALF_UP);
            }
        }
        sc.close();
    }
}
