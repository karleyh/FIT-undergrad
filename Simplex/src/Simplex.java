/*
 * Author : Karley Herschelman, kherschelman2012@my.fit.edu
 * Course : CSE 2010, Section E1, Spring 2014
 * Project: lab 9, Simplex method part 2
 */
import java.util.ArrayList;
import java.util.Arrays;

// Simplex.java
/**
 * solves linear program using simplex method
 * @author Karley
 *
 */
public class Simplex {
	private LinearProgram lp;
	public double[][] tableau;
	int[] basic;
	double[] solution;
	static int numVariablesInObjective;
	int enteringColumn;
	int departingRow;
	double pivot;

	/**
	 * constructor. Linear program to be passed in this constructor is assumed
	 * to be in a standard form already.
	 * 
	 * @param lp_
	 *            linear program
	 */
	public Simplex(LinearProgram lp_) {
		lp = lp_;
		basic = new int[lp.numOfSlack];
		solution = new double[lp.getNumOfVariables() + lp.numOfSlack];
		tableau = new double[lp.getNumOfConstraints() + 1][lp
				.getNumOfVariables() + lp.numOfSlack + 1];

		// fill up tableau with constraints
		ArrayList<Constraint> constraints = lp.getConstraints();
		for (int row = 0; row < lp.getNumOfConstraints(); row++) {
			Constraint constraint = constraints.get(row);
			// original variables
			for (int column = 0; column < lp.getNumOfVariables(); column++) {
				tableau[row][column] = constraint.getC(column);
			}
			int i = 0;
			// slack variables
			for (int column = lp.getNumOfVariables(); column < (lp
					.getNumOfVariables() + lp.numOfSlack); column++) {
				tableau[row][column] = constraint.slack.get(i);
				basic[i] = column;
				i++;
			}
			// b column
			tableau[row][lp.getNumOfVariables() + lp.numOfSlack] = constraint
					.getB();
		}

		// add objective to the last row
		Objective objective = lp.getObj();
		numVariablesInObjective = objective.n;
		for (int column = 0; column < numVariablesInObjective; column++) {
			tableau[lp.getNumOfConstraints()][column] = objective.getC(column)
					* -1;
		}

	}

	/**
	 * implements simplex method
	 * 
	 * @param debug
	 *            when true, intermediate steps are shown
	 * @return a simplex solution
	 */
	public SimplexSolution solveSimplex(boolean debug) {
		if (lp.getNumOfVariables() + lp.numOfSlack < lp.getNumOfConstraints()) {
			System.out.println("No Solution!");
		}
		int i = 1;
		boolean neg = containsNeg();
		while (neg) {
			// entering column = index of min value in last row
			enteringColumn = minIndex(tableau[lp.getNumOfConstraints()]);

			// departing row = smallest value (>=0) of last column value/
			// corresponding entering column value
			double[] divided = divideLastByPivotColumn();
			departingRow = posMinIndex(divided);

			pivot = tableau[departingRow][enteringColumn];

			// reset solution
			for (int k = 0; k < solution.length; k++) {
				solution[k] = 0;
			}
			// fill solution
			for (int k = 0; k < lp.numOfSlack; k++) {
				solution[basic[k]] = tableau[k][lp.getNumOfVariables()
						+ lp.numOfSlack];
			}

			if (debug) {
				// output intermediate steps
				printTableau(i);
				i++;
			}
			// does the tableau just printed contain a negative?
			neg = containsNeg();
			if (!neg) {
				break;
			}
			// set index of the entering column to replace index in basic of the
			// departing row
			basic[departingRow] = enteringColumn;
			// set pivot to one and all other entries in the column to 0
			pivot();
		}
		// upon exiting loop, return results
		return new SimplexSolution(solution,
				tableau[lp.getNumOfConstraints()][lp.getNumOfVariables()
						+ lp.numOfSlack]);
	}

	private void pivot() {
		// make pivot one by dividing departing row by pivot
		for (int column = 0; column <= lp.getNumOfVariables() + lp.numOfSlack; column++) {
			tableau[departingRow][column] /= pivot;
		}

		// make the pivot column zeros
		double value; // to make values 0
		double columnValue = 0; // value * value in column,pivot row
		for (int row = 0; row <= lp.getNumOfConstraints(); row++) {
			// skip the pivot row
			if (row == departingRow) {
				row++;
			}
			// use negative of value to make 0
			value = tableau[row][enteringColumn] * -1;
			// add value to the number to make 0, this works because pivot is 1
			for (int column = 0; column <= lp.getNumOfVariables()
					+ lp.numOfSlack; column++) {
				columnValue = value * tableau[departingRow][column];
				tableau[row][column] += columnValue;

			}
		}

	}

	// divide the last column by the pivot column to find the departing row
	private double[] divideLastByPivotColumn() {
		double[] divided = new double[lp.getNumOfConstraints()];
		for (int j = 0; j < lp.getNumOfConstraints(); j++) {
			if (tableau[j][enteringColumn] != 0) {
				divided[j] = (tableau[j][lp.getNumOfVariables() + lp.numOfSlack])
						/ tableau[j][enteringColumn];
			}
		}
		return divided;
	}

	// find the index where the value is min in the array (just positive numbers
	// >=0)
	private int posMinIndex(double[] array) {
		int index = 0;
		int k = 0;
		// find the first instance of a non negative value in the array
		while (array[k] < 0 && k < array.length) {
			k++;
		}
		double min = array[k];
		// find the min in the array that is not negative
		for (int i = k + 1; i < array.length; i++) {
			if (array[i] < min && array[i] >= 0) {
				min = array[i];
				index = i;
			}

		}
		return index;
	}

	// find the index where the value is min in the array (positive or negative)
	private int minIndex(double[] array) {
		int index = 0;
		double min = array[0];
		for (int i = 1; i < array.length; i++) {
			if (array[i] < min) {
				min = array[i];
				index = i;
			}

		}
		return index;
	}

	// check if the last objective row contains negatives
	private boolean containsNeg() {
		for (int column = 0; column < lp.getNumOfVariables() + lp.numOfSlack; column++) {
			if (tableau[lp.getNumOfConstraints()][column] < 0) {
				return true;
			}
		}
		return false;
	}

	// print intermediate steps
	private void printTableau(int iteration) {
		// itteration number
		System.out.println("Iteration #" + iteration + " simplex tableau");
		// tableau
		for (int row = 0; row < tableau.length; row++) {
			for (int column = 0; column < tableau[0].length; column++) {
				System.out.printf("%-8.2f", tableau[row][column]);
			}
			System.out.printf("%n");
		}

		// basic variables
		System.out.println("Basic Variables:");
		for (int i = 0; i < lp.numOfSlack; i++) {
			System.out.print(basic[i] + " ");
		}
		System.out.println("");

		// solution
		System.out.println("Solution to linear system:");
		System.out.println(Arrays.toString(solution));

		// entering and departing
		System.out.println("Entering: " + enteringColumn + ", departing: "
				+ basic[departingRow]);
	}
}
