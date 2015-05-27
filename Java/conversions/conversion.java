import java.util.Scanner;

public class conversion {

    public static void main(String[] args) {
        final Scanner sc = new Scanner(System.in);
        while (true) {
            String input = sc.next();
            int c = input.indexOf("C");
            int r = input.indexOf("R");
            int row = Integer.parseInt(input.substring(r, c - 1));
            int column = Integer
                    .parseInt(input.substring(c, input.length() - 1));
            if (row == 0 && column == 0) {
                break;
            }
            System.out.println(row + " " + column);
        }
        // System.out.println(conversion(r, 0));
    }

    public static int conversion(int r, int conversion) {
        if (r < 26) {
            return conversion;
        }
        return conversion(r % 26, conversion += r);
    }
}
