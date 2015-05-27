public class Driver {

    public static void main(String[] args) {
        int[] jobCompensation = { 22, 8, 20, 15, 11, 15, 14 };
        String[] jobNames = { "C++ tutoring", "mobile app #1", "mobile app #2",
                "Website in PHP", "Java EE project", "Python GUI", "Unix admin" };
        int[] jobTimes = { 14, 7, 11, 9, 10, 13, 12 };
        int timeload = 0;
        timeload = 17;
        WorkPlanner spring2014 = new WorkPlanner(jobTimes, jobCompensation,
                jobNames);
        System.out.println("Testcase #1: timeload=" + timeload + " hours");
        WorkPlannerSolution wpsolution = spring2014.findBestJobs(timeload);
        System.out.println(wpsolution.toString());
        System.out.println("-------------------------");
        timeload = 34;
        System.out.println("Testcase #2: timeload=" + timeload + " hours");
        spring2014 = new WorkPlanner(jobTimes, jobCompensation, jobNames);
        wpsolution = spring2014.findBestJobs(timeload);
        System.out.println(wpsolution.toString());
        System.out.println("-------------------------");
        timeload = 40;
        System.out.println("Testcase #3: timeload=" + timeload + " hours");
        spring2014 = new WorkPlanner(jobTimes, jobCompensation, jobNames);
        wpsolution = spring2014.findBestJobs(timeload);
        System.out.println(wpsolution.toString());
        System.out.println("-------------------------");
        timeload = 50;

        System.out.println("Testcase #4: timeload=" + timeload + " hours");
        spring2014 = new WorkPlanner(jobTimes, jobCompensation, jobNames);
        wpsolution = spring2014.findBestJobs(timeload);
        System.out.println(wpsolution.toString());
        System.out.println("-------------------------");
        timeload = 60;
        System.out.println("Testcase #5: timeload=" + timeload + " hours");
        spring2014 = new WorkPlanner(jobTimes, jobCompensation, jobNames);
        wpsolution = spring2014.findBestJobs(timeload);
        System.out.println(wpsolution.toString());
        System.out.println("-------------------------");
        timeload = 70;
        System.out.println("Testcase #6: timeload=" + timeload + " hours");
        spring2014 = new WorkPlanner(jobTimes, jobCompensation, jobNames);
        wpsolution = spring2014.findBestJobs(timeload);
        System.out.println(wpsolution.toString());
        System.out.println("-------------------------");
    }

}
