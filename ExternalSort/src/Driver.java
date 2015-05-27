import java.io.FileNotFoundException;

public class Driver {

    // main method
    public static void main(String[] args) throws FileNotFoundException {
        ExternalSort externalsort = new ExternalSort("C:\\files\\temporary\\",
                10000);
        externalsort.completeSort("testcase1.txt",
                "C:\\files\\output1.txt");
    }

}
