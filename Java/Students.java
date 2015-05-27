/*
 * Author: Karley Herschelman, kherschelman2012@my.fit.edu
 * Course: CSE 1002, Section 01/02, Fall 2013
 * Project: Students
 */
import java.util.Scanner;

public class Students {

    private static final int NINETY = 90;
    private static final int EIGHTY_NINE = 89;
    private static final int EIGHTY = 80;
    private static final int SEVENTY_NINE = 79;
    private static final int SIXTY_NINE = 69;
    private static final int SEVENTY = 70;
    private static final int SIXTY = 60;

    public static char letter (final int grade) {
        if (grade >= NINETY) {
            return 'A';
        }
        if ((grade >= EIGHTY) && (grade <= EIGHTY_NINE)) {
            return 'B';
        }
        if ((grade >= SEVENTY) && (grade <= SEVENTY_NINE)) {
            return 'C';
        }
        if ((grade >= SIXTY) && (grade <= SIXTY_NINE)) {
            return 'D';
        }
        if (grade < SIXTY) {
            return 'F';
        } else {
            return 0;
        }
    }

    public static void main (final String[] args) {

        final Scanner sc = new Scanner(System.in);
        String name = "";
        int grade;
        double total = 0.0;
        int count = 0;
        char letterGrade = 'A';
        System.out.printf("%-16s%5s        %-6s%n", "Name", "Grade", "Letter");
        while (sc.hasNext()) {
            if (sc.hasNextInt()) {
                grade = sc.nextInt();
                total += grade;
                letterGrade = letter(grade);
                System.out.printf("%-16s%5d        %-6s%n", name, grade,
                        letterGrade);
                name = "";
                count++;
            } else {
                name += sc.next() + " ";
            }

        }
        final double average = total / count;
        System.out.printf("%-16s%5.2f", "Average", average);
    }

}
