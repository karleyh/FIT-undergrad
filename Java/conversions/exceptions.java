import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class exceptions {

    public static void main(String[] args) throws FileNotFoundException {
        String file = args[0];
        Scanner sc = new Scanner(new File(file));
        String input = null;
        try {
            input = sc.next();
            int c = input.indexOf("C");
            int r = input.indexOf("R");
            int row = Integer.parseInt(input.substring(r, c));
            int column = Integer.parseInt(input.substring(c, input.length()));
            System.out.println(row + " " + column);
        } // catch (FileNotFoundException e) {
          // System.out.println("Cannot open file");

        // }
        finally {
            System.out.println(input);
            // conversion (r, 0);
        }

    }

}
