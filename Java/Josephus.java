/*
 * Author: Karley Herschelman, kherschelman2012@my.fit.edu
 * Course: CSE 1002, Section 01/02, Fall 2013
 * Project: josephus
 */
import java.util.List;
import java.util.ListIterator;

public class Josephus {

    private static final int TEN = 10;

    @SuppressWarnings("unchecked")
    public static void main(final String[] args) throws ClassNotFoundException,
            InstantiationException, IllegalAccessException {
        final int soldiers = Integer.parseInt(args[0]);
        final int count = Integer.parseInt(args[1]);

        final Class<?> clazz = Class.forName(args[2]);
        java.util.List<Integer> list = (java.util.List<Integer>) clazz
                .newInstance();

        final StopWatch sw = new StopWatch(true);
        int lastSoldier = 0;

        for (int i = 0; i < TEN; i++) { // repeats 10 times
            // (re)fills list 1 to N soldiers
            for (int j = 1; j <= soldiers; j++) {
                list.add(j);
            }
            // starts stopwatch
            sw.start();
            // removes soldiers, returns final soldier
            lastSoldier = josephus(list, count);
            // stops stopwatch
            sw.stop();
            // removes last soldier, making list empty
            list = (java.util.List<Integer>) clazz.newInstance();
        }
        final double averageTime = sw.getAverageTime();
        System.out.println("Last Soldier: " + lastSoldier);
        System.out.println("Average Running Time: " + averageTime);
    }

    static int josephus(final List<Integer> data, final int count) {
        int i = 0;
        int index = 0;
        ListIterator<Integer> list = data.listIterator();
        while (data.size() > 1) {
            i += count - 1;
            // creates a circle effect
            if (i >= data.size()) {
                i %= data.size();
            }
            while (index >= i) {
                list.previous();
                index--;
            }
            while (index <= i) {
                list.next();
                index++;
            }
            list.remove();
            System.out.println(data.size());
        }
        return data.size();
    }
}
