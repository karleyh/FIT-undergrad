/*
 * Author : Karley Herschelman, kherschelman2012@my.fit.edu
 * Course : CSE 2010, Section E1, Spring 2014
 * Project: lab 9, Simplex method part 2
 */
import java.util.ArrayList;

public class LinearProgram {
	private Objective obj; // object representing objective function
	private static Objective objFunc; // to be used for static function solve
	private ArrayList<Constraint> constraints = new ArrayList<Constraint>(); // constraints
	private int numOfConstraints; // number of constraints
	private int numOfVariables; // number of variables
	public int numOfSlack = 0;
	private static int pivotColumn;
	private static int pivotRow;
	private static double pivot;

	/**
	 * @return constraints
	 */
	public ArrayList<Constraint> getConstraints() {
		return this.constraints;
	}

	/**
	 * @return number of constraints
	 */
	public int getNumOfConstraints() {
		return this.numOfConstraints;
	}

	/**
	 * @return number of variables
	 */
	public int getNumOfVariables() {
		return this.numOfVariables;
	}

	/**
	 * @return objective
	 */
	public Objective getObj() {
		return this.obj;
	}

	/**
	 * create a linear program with n variables
	 * 
	 * @param n
	 *            number of variables
	 */
	public LinearProgram(int n) {
		numOfVariables = n;
	}

	/**
	 * creates a new objective from a string
	 * 
	 * @param strObjective
	 *            objective as a string
	 */
	public void addObjective(String strObjective) {
		obj = new Objective(strObjective);
		objFunc = obj;
	}

	/**
	 * add constraint parsed from a string
	 * 
	 * @param strConstraint
	 */
	public void addConstraint(String strConstraint) {
		constraints.add(new Constraint(strConstraint));
		numOfConstraints++;
	}

	// Gauss algorithm
	/**
	 * solves the linear system using guass algorithm
	 * 
	 * @param A
	 *            matrix
	 * @param b
	 *            Ax=b
	 * @return results
	 */
	public static double[] solveLinearSystem(double[][] A, double[] b) {

		int rowLength = A.length;
		int columnLength = A[0].length;

		for (int count = 0; count < rowLength; count++) {

			// find the pivot
			findPivot(A, columnLength, count);

			// swap row with pivot row
			swap(A, b, columnLength, count);

			// check that the pivot value is not zero
			// modeled of of wikipedia pseduo code
			if (pivot == 0) {
				throw new RuntimeException("Matrix is singular!");
			}

			// pivot within A and b
			elimination(A, b, columnLength);
		}

		double[] results = new double[columnLength];
		for (int row = columnLength - 1; row >= 0; row--) {
			// reset value
			double value = 0;
			for (int column = row + 1; column < columnLength; column++) {
				// multiply by results
				value += A[row][column] * results[column];
			}
			results[row] = (b[row] - value) / A[row][row];
		}
		return results;
	}

	private static void findPivot(double[][] A, int columnLength, int count) {
		// find pivot
		// pivot row occurs at highest absolute value in the current
		// column(count)
		pivotRow = count;
		pivotColumn = count;
		double max = A[pivotRow][pivotColumn];
		for (int row = count + 1; row < columnLength; row++) {
			if (Math.abs(A[row][count]) > max) {
				pivotRow = row;
				max = A[pivotRow][pivotColumn];
			}
		}
	}

	private static void swap(double[][] A, double[] b, int columnLength,
			int count) {
		// swap row with pivot row
		double tempB = b[count];
		b[count] = b[pivotRow];
		b[pivotRow] = tempB;
		double[] tempA = new double[columnLength];
		for (int i = 0; i < A[0].length; i++) {
			tempA[i] = A[count][i];
			A[count][i] = A[pivotRow][i];
			A[pivotRow][i] = tempA[i];
		}
		// after swap the new pivot row is the row at count
		pivotRow = count;
		pivot = A[pivotRow][pivotColumn];
	}

	private static void elimination(double[][] A, double[] b, int columnLength) {
		// cancels values
		for (int row = pivotRow + 1; row < columnLength; row++) {
			double value = A[row][pivotColumn] / pivot;
			b[row] -= (value * b[pivotRow]);
			for (int column = pivotRow; column < columnLength; column++) {
				A[row][column] -= (value * A[pivotRow][column]);
			}
		}
	}



	/**
	 * returns a string with Xn labels
	 * 
	 * @return String representation of the Linear program
	 **/
	@Override
	public String toString() {
		String str = "";
		// min.max string representation of objective with Xn
		str += obj.toString() + obj.StringRep() + "\n" + "s.t. ";
		for (int i = 0; i < numOfConstraints; i++) {
			if (i > 0) {
				// space to balance out the s.t and objective type
				str += "     ";
			}
			// string representation of each constraint
			Constraint cons = constraints.get(i);
			str += cons.StringRep() + "\n";
		}
		return str;
		//
	}

	/**
	 * convert given linear program into a standard form
	 */
	public void convertToStandardForm() {
		Constraint cons;
		// switch the objective type
		if (obj.toString().equals("min.")) {
			obj.setType("max.");
			// change the sign of all variables in objective
			for (int i = 0; i < numOfVariables; i++) {
				obj.setC(i, obj.getC(i) * -1);
			}
		}
		for (int i = 0; i < numOfConstraints; i++) {
			cons = constraints.get(i);
			//if (!cons.getType().equals("=")) {
				numOfSlack++;
				// add slack variables
				if (cons.getType().equals("=")) {
					cons.slack.add(0.0);
				}
				if (cons.getType().equals("<=")) {
					cons.slack.add(1.0);
				}
				if (cons.getType().equals(">=")) {
					cons.slack.add(-1.0);
				}
				// change all types to be equals
				cons.setType("=");

				// add zeros to other constraints
				for (int j = 0; j < numOfConstraints; j++) {
					if (i == j) {
						j++;
					}
					if (j >= numOfConstraints) {
						break;
					}
					cons = constraints.get(j);
					cons.slack.add(0.0);
				}
			//}
			// set all constraint type to equals
		}

	}
}
