import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Excel {

    public static void main(String[] args) {
        for (int i = 0; i < args.length; i++) {
            try {
                Scanner sc = new Scanner(new File(args[i]));
                while (true) {
                    String input = sc.next();
                    int cIndex = input.indexOf("C");
                    int rIndex = input.indexOf("R");
                    int row = Integer.parseInt(input.substring(rIndex + 1,
                            cIndex));
                    int column = Integer.parseInt(input.substring(cIndex + 1,
                            input.length()));
                    if (column == 0 && row == 0) {
                        break;
                    }
                    System.out.println(toBase26(column) + "" + row);
                }
                sc.close();

            } catch (FileNotFoundException e) {
                System.out.println("Cannot open file");
            } catch (InputMismatchException e) {
                System.out.println("Invalid Input");
            }
        }
    }

    public static String toBase26(int number) {
        number = Math.abs(number);
        String converted = "";
        // Repeatedly divide the number by 26 and convert the
        // remainder into the appropriate letter.
        do {
            if (number == 0) {
                converted = "Z" + converted;
            } else {
                int remainder = number % 26;
                converted = (char) (remainder + '@') + converted;
                number = (number - remainder) / 26;
            }
        } while (number > 0);

        return converted;
    }

    public static String conversion(int column) {
        int c = column;
        String answer = "";
        while (c > 26) {
            c %= 26;
            answer = ascii(c, answer);
            c = (column - c) / 26;
        }
        // gets last number as letter and adds to string
        answer = ascii(c, answer);
        // bigger part is added to end so it should be reversed
        answer = new StringBuffer(answer).reverse().toString();
        return answer;
    }

    public static String ascii(int c, String answer) {
        if (c > 0) { // @ not a letter
            char ASCII = (char) (c + 64);
            answer += "" + ASCII;
        }
        return answer;
    }
}
