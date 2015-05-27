public class WorkPlannerSolution {
    private int totalMoney_;
    private int totalTime_;
    private String[] names_;

    public WorkPlannerSolution(int totalMoney, int totalTime, String[] names) {
        totalMoney_ = totalMoney;
        totalTime_ = totalTime;
        names_ = new String[names.length];
        for (int i = 0; i < names.length; i++) {
            names_[i] = names[i];
        }
    }

    /**
     * string representation of the solution
     * 
     * @return string including time, value, and names
     */
    @Override
    public String toString() {
        String output = "";
        // output total time
        output += "Time: " + totalTime_ + "\n";
        // output total Money
        output += "Value: " + totalMoney_;
        // output the names of the jobs
        for (int i = 0; i < names_.length; i++) {
            output += "\n" + names_[i];
        }
        return output;
    }
}
