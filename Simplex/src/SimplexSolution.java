/*
 * Author : Karley Herschelman, kherschelman2012@my.fit.edu
 * Course : CSE 2010, Section E1, Spring 2014
 * Project: lab 9, Simplex method part 2
 */
import java.util.Arrays;

// SimplexSolution.java
/**
 * formats solution
 * 
 * @author Karley
 */
public class SimplexSolution {
	private double[] x;
	private double objective;

	/**
	 * @param x_
	 *            solution array
	 * @param objective_
	 *            value in bottom right of tableau
	 */
	public SimplexSolution(double[] x_, double objective_) {
		this.x = x_;
		this.objective = objective_;
	}

	/**
	 * returns string of objective and solution for linear program
	 **/
	@Override
	public String toString() {
		return "Objective: " + objective + "\n" + "Solution: "
				+ Arrays.toString(x);
		// return String representation of the solution and objective value
	}
}
