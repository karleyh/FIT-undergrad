/*
 * Author : Karley Herschelman, kherschelman2012@my.fit.edu
 * Course : CSE 2010, Section E1, Spring 2014
 * Project: lab 9, Simplex method part 2
 */
import java.util.ArrayList;

/**
 * @author Karley storage object for a constraint
 */
public class Constraint {
    private String type; // ">=" or "<=" or "="
    private double[] c; // coefficients
    private double b; // constant
    private int n;

    /**
     * getter for c array at an index
     * 
     * @param i
     *            index
     * @return value at index
     */
    public double getC(int i) {
        return c[i];
    }
    
    public double getB (){
        return b;
    }

    /**
     * Array List of slack values
     */
    public ArrayList<Double> slack = new ArrayList<Double>();

    /**
     * create constraint with n variables
     * 
     * @param n
     *            number of variables
     */

    /**
     * getter for type
     * 
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * setter for type
     * 
     * @param type
     *            used to set type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * create a constraint with n variables
     * 
     * @param n
     *            number of variables
     */
    public Constraint(int n) {
        this.n = n;
        c = new double[n];
    }

    private static double parse(String input) {
        // parse a double, determining if it is positive or negative
        if (input.charAt(0) == '-') {
            return Double.parseDouble(input.substring(1, input.length())) * -1;
        }
        return Double.parseDouble(input);
    }

    /**
     * parse constraint from a string
     * 
     * @param strConstraint
     */
    public Constraint(String strConstraint) {
        // split input string to an array
        String[] input = strConstraint.split(new String("\\s+"));
        // number of variables given in input
        c = new double[input.length - 2];
        n = c.length;
        int i = 0;
        while (!input[i].equals(">=") && !input[i].equals("=")
                && !input[i].equals("<=")) {
            // add variables to constraint array
            c[i] = parse(input[i]);
            i++;
        }
        type = input[i];
        // constant variables must equal
        b = parse(input[i + 1]);
    }

    /**
     * @return string of sum of Ci*X_i from 1 to n =(<= or >=) b
     */
    public String StringRep() {
        String str = "";
        for (int i = 0; i < n; i++) {
            if (c[i] >= 0 && i > 0) {
                str += "+";
            }

            str += c[i] + "x_" + (i + 1);
        }
        for (int i = 0; i < slack.size(); i++) {
            n++;
            if (slack.get(i) >= 0) {
                str += "+";
            }
            str += slack.get(i) + "x_" + n;

        }
        str += type + b;

        return str;
    }

    /**
     * String representation of a constraint
     * 
     * @returns type: =, >=, <=
     **/
    @Override
    public String toString() {
        return type;
    }
}
