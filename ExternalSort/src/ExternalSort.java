import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

// ExternalSort.java
/**
 * @author Karley
 */
public class ExternalSort {
    private int chunkSize_; // size of the chunks
    // http://en.wikipedia.org/wiki/External_sorting
    private String directory_; // directory where all the temporary

    /**
     * @param directory
     * @param chunkSize
     */
    public ExternalSort(String directory, int chunkSize) {
        directory_ = directory;
        chunkSize_ = chunkSize;
        new File(directory_).mkdirs();
    }

    /**
     * @param inputFile
     * @param outputFile
     */
    public void completeSort(String inputFile, String outputFile) {
        try {

            int[] file = new int[chunkSize_];
            ArrayList<File> tempFileNames = new ArrayList<File>();
            Scanner sc = new Scanner(new File(inputFile));

            int num = 0; // number of temporary files
            while (sc.hasNextInt()) {

                String filename = directory_ + "file" + num + ".txt";
                File tempFile = new File(filename);
                tempFileNames.add(tempFile);
                BufferedWriter output = new BufferedWriter(new FileWriter(
                        tempFile));

                // write to array
                for (int i = 0; i < chunkSize_ && sc.hasNextInt(); i++) {
                    file[i] = sc.nextInt();
                    System.out.println(i);
                }
                // sort array
                mergeSort(file);
                // write array to file
                for (int i = 0; i < chunkSize_; i++) {
                    String line = new Integer(file[i]).toString();
                    output.write(line);
                    output.newLine();
                }

                int index = 0;
                for (int i = 0; tempFileNames.size() > 1; i++) {
                    if (i >= tempFileNames.size() - 1) {
                        i = 0;
                    }
                    File sorted = new File(directory_ + index + ".txt");
                    File left = tempFileNames.get(i);
                    i++;
                    File right = null;
                    if (i == tempFileNames.size()) {
                        i = 0;
                    }
                    if (tempFileNames.size() == 2) {
                        sorted = new File(outputFile);
                        left = tempFileNames.get(0);
                        right = tempFileNames.get(1);
                        merge(sorted, left, right);
                        for (int j = 0; j < tempFileNames.size(); j++) {
                            tempFileNames.get(i).delete();
                        }
                        break;
                    }
                    if (i < tempFileNames.size() && tempFileNames.size() > 2) {
                        right = tempFileNames.get(i);
                        merge(sorted, left, right);
                        tempFileNames.remove(left);
                        tempFileNames.remove(right);
                        tempFileNames.add(sorted);
                    }
                    else{
                        tempFileNames.add((l)
                    }
                    index ++;

                }

                /*
                 * File sorted = new File(outputFile);
                 * for (int i = 0; tempFileNames.size() > 1; i++) {
                 * if (i >= tempFileNames.size()) {
                 * i = 0;
                 * }
                 * File left = tempFileNames.get(i);
                 * i++;
                 * if (tempFileNames.size() > 1 && i < tempFileNames.size()) {
                 * File right = tempFileNames.get(i);
                 * merge(sorted, left, right);
                 * }
                 * left = sorted;
                 * tempFileNames.set(i - 1, left);
                 * tempFileNames.remove(i);
                 * }
                 */
                // merge files
                num++;
                output.close();
                // delete files
                for (int i = 0; i < num; i++) {
                    // tempFileNames.get(i).delete();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mergeSort(int[] sorted) {
        // divide until size 1
        if (sorted.length > 1) {
            int middle = sorted.length / 2;

            int[] left = Arrays.copyOfRange(sorted, 0, middle);
            int[] right = Arrays.copyOfRange(sorted, middle, sorted.length);

            mergeSort(left); // keep breaking up into pieces
            mergeSort(right);
            merge(sorted, left, right); // merge pieces together
        }
    }

    private void merge(int[] sorted, int[] left, int[] right) {
        int total = left.length + right.length;
        int totalIndex, leftIndex, rightIndex;
        totalIndex = leftIndex = rightIndex = 0;
        while (totalIndex < total) {
            if ((leftIndex < left.length) && (rightIndex < right.length)) {
                if (left[leftIndex] < right[rightIndex]) {
                    sorted[totalIndex] = left[leftIndex];
                    totalIndex++;
                    leftIndex++;
                } else {
                    sorted[totalIndex] = right[rightIndex];
                    totalIndex++;
                    rightIndex++;
                }
            } else {
                if (leftIndex >= left.length) {
                    while (rightIndex < right.length) {
                        sorted[totalIndex] = right[rightIndex];
                        totalIndex++;
                        rightIndex++;
                    }
                }
                if (rightIndex >= right.length) {
                    while (leftIndex < left.length) {
                        sorted[totalIndex] = left[leftIndex];
                        leftIndex++;
                        totalIndex++;
                    }
                }
            }
        }

    }

    private void mergeSort(File sorted) {

        try {
            Scanner input = new Scanner(sorted);

            if (input.hasNextInt()) {

                // write to array
                for (int i = 0; i < chunkSize_ && input.hasNextInt(); i++) {
                    file[i] = input.nextInt();
                    System.out.println(i);
                }
                // sort array
                mergeSort(file);
            }

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void merge(File sorted, File left, File right) {

        try {
            Scanner l = new Scanner(left);
            Scanner r = new Scanner(right);
            BufferedWriter output = new BufferedWriter(new FileWriter(sorted));
            // int total = left.length + right.length;
            // int totalIndex, leftIndex, rightIndex;
            int leftValue = l.nextInt();
            int rightValue = r.nextInt();
            // totalIndex = leftIndex = rightIndex = 0;
            while (l.hasNext() || r.hasNext()) {
                if ((l.hasNext()) && (r.hasNext())) {

                    if (leftValue < rightValue) {
                        output.write(leftValue);
                        // totalIndex++;
                        leftValue = l.nextInt();
                    } else {
                        output.write(rightValue);
                        // totalIndex++;
                        rightValue = r.nextInt();
                    }
                } else {
                    if (!l.hasNext()) {
                        while (r.hasNext()) {

                            output.write(rightValue);

                            // totalIndex++;
                            rightValue = r.nextInt();
                        }
                    }
                    if (!r.hasNext()) {
                        while (l.hasNext()) {

                            output.write(leftValue);

                            leftValue = l.nextInt();
                            // totalIndex++;
                        }
                    }
                }
            }
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
