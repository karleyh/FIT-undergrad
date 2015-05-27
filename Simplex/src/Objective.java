/*
 * Author : Karley Herschelman, kherschelman2012@my.fit.edu
 * Course : CSE 2010, Section E1, Spring 2014
 * Project: lab 9, Simplex method part 2
 */
import java.util.Arrays;

/**
 * @author Karley 
 * @category storage object for an objective
 */
public class Objective {
	private String type; // can be either "min.’ or ’max.’
	private double[] c; // coefficients of the objective function
	/**
	 * number of variables
	 */
	public int n;

	/**
	 * setter for type
	 * 
	 * @param type
	 *            new value of type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * setter for c at an index
	 * 
	 * @param i
	 *            index
	 * @param n
	 *            new value
	 */
	public void setC(int i, double n) {
		this.c[i] = n;
	}

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

	/**
	 * @param n
	 *            number of variables
	 */
	public Objective(int n) {
		this.n = n;
		c = new double[n];
	}

	private static double parse(String input) {
		// parse a double, determining negative or positive
		if (input.charAt(0) == '-') {
			return Double.parseDouble(input.substring(1, input.length())) * -1;
		}
		return Double.parseDouble(input);
	}

	/**
	 * parse objective from a string
	 * 
	 * @param strObjective
	 *            input string max or min. followed by coefficients
	 */
	public Objective(String strObjective) {
		String[] input = strObjective.split(new String("\\s+"));
		// max or min
		type = input[0];
		// coefficients
		c = new double[input.length - 1];
		n = c.length;
		for (int i = 1; i < input.length; i++) {
			c[i - 1] = parse(input[i]);
		}

	}

	/**
	 * @return string of sum of Ci*X_i from 1 to n
	 */
	public String StringRep() {
		String str = " ";
		for (int i = 0; i < c.length; i++) {
			if (c[i] >= 0 && i > 0) {
				str += "+";
			}
			// adds each value to the string along with the correct x_i
			str += c[i] + "x_" + (i + 1);
		}

		return str;
	}

	/**
	 * String representation of an objective
	 * 
	 * @return type: min., max.
	 **/
	@Override
	public String toString() {
		return type;
	}
}