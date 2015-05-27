// WorkPlanner.java
public class WorkPlanner {
    private int[] time_; // time each job takes
    private int[] money_; // how much the job pays
    private String[] names_;

    public WorkPlanner(int[] time, int[] money, String[] names) {
        // constructor
        time_ = new int[time.length];
        money_ = new int[money.length];
        names_ = new String[names.length];
        for (int i = 0; i < time.length; i++) {
            time_[i] = time[i];
            money_[i] = money[i];
            names_[i] = names[i];
        }
    }

    public WorkPlannerSolution findBestJobs(int T) {
        int totalMoney;
        int totalTime;
        String[] bestNames;
        int max = 0;
        int maxIndex = 0;
        int tempMax;
        for (int i = 0; i < time_.length; i++) {
            for (int j = 0; j < T; j++){
                if (money_[i] <= j){
                    
                }
            }
            T -= time_[i];
            if (T >= 0) {
                tempMax = findBestJobs(T) + time_[i];
                if (tempMax > max){
                    max = tempMax;
                    maxIndex = i;
                }
            }
        }
        
        bestNames = names[maxIndex];
        return new WorkPlannerSolution(totalMoney, totalTime, bestNames);
        // find best subset of projects so that amount of money is maximum while
        // total time is <=T
    }
}
