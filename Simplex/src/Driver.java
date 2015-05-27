public class Driver {
    public static void main(String[] args) {
        String s1 = "-1 1 <= 11";
        String s2 = "1 1 <= 27";
        String s3 = "2 5 <= 90";
        int nVars = 2;
        LinearProgram lp = new LinearProgram(nVars);
        String o = "max. 4 6";
        lp.addObjective(o);
        lp.addConstraint(s1);
        lp.addConstraint(s2);
        lp.addConstraint(s3);
        // System.out.println(lp.toString() + "\n\n\n");
        lp.convertToStandardForm();
       // System.out.println(lp.getNumOfConstraints() + " " + lp.getNumOfVariables() + "  " + lp.numOfSlack);
        System.out.println(lp.toString());
        Simplex simplex = new Simplex(lp);
        SimplexSolution solution = simplex.solveSimplex(true);
        //System.out.println(solution.toString());
        System.out.println("\nWithout debugging output \n");
        solution = simplex.solveSimplex(false);
        //System.out.println(solution.toString());
    }
}
