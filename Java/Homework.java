/*
 * Author: Karley Herschelman, kherschelman2012@my.fit.edu
 * Course: CSE 1002, Section 01/02, Fall 2013
 * Project: homework
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Homework {

    public static void main(String[] args) {
        final Scanner sc = new Scanner(System.in);
        final int trials = sc.nextInt();
        for (int i = 0; i < trials; i++) {
            final int problems = sc.nextInt();
            final int[] time = new int[problems];
            final int[] weight = new int[problems];
            for (int j = 0; j < problems; j++) {
                time[j] = sc.nextInt();
            }
            for (int j = 0; j < problems; j++) {
                weight[j] = sc.nextInt();
            }
            System.out.println(hw(time, weight));
        }
        sc.close();
    }

    public static int hw(int[] time, int[] weight) {
        // save the time/weight pairs into a list of Problem objects
        ArrayList<Problem> problems = new ArrayList<Problem>();
        for (int i = 0; i < time.length; i++) {
            problems.add(new Problem(time[i], weight[i]));
        }

        // sort the Problem objects such that they will yield the lowest sum of
        // weighted completion times
        Collections.sort(problems);

        // compute the sum of times using the sorted order
        int timesum = 0;
        int value = 0;
        for (int j = 0; j < time.length; j++) {
            // gets next problem in sorted list
            final Problem problem = problems.get(j);
            // adds to time
            timesum += problem.time;
            value += timesum * problem.weight;
        }

        // return the sum of weighted completion times
        return value;
    }

    public static class Problem implements Comparable<Problem> {
        public int time;
        public int weight;

        public Problem(int time, int weight) {
            this.time = time;
            this.weight = weight;
        }

        @Override
        public int compareTo(Problem other) {
            return this.time * other.weight - other.time * this.weight;
        }
    }

}
