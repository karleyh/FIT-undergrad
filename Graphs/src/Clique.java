/*
 * Author : Karley Herschelman, kherschelman2012@my.fit.edu
 * Author : Josh Lear, jlear2013@my.fit.edu
 * Course : CSE 2010, Section E1, Spring 2014
 * Project: lab 14, Graphs II
 */
import java.util.ArrayList;

// Clique.java
public class Clique {
    private Graph g; // since clique is a subgraph it has to depend on
    // existing graph
    private ArrayList<Integer> cliqueVertices; // vertices forming the

    // clique
    // getter, will be used for grading

    public ArrayList<Integer> getCliqueVertices() {
        return cliqueVertices;
    }

    public void setCliqueVertices(int v) {
        cliqueVertices.add(v);
    }

    public Clique(Graph graph) {
        g = graph;
        cliqueVertices = new ArrayList<Integer>();
    }

    // String representation of a clique. Nothing to implement here.
    public String toString() {
        String res = "Clique size: " + this.cliqueVertices.size() + "\n";
        res = res + "Vertices: " + this.cliqueVertices.toString() + "\n";
        return res;
    }

    /**
     * test if set is a clique
     * 
     * @param list
     *            subset of graph
     * @return true if set is a clique
     */
    public boolean isClique() {
        for (int i = 0; i < this.cliqueVertices.size(); i++) {
            for (int j = i; j < this.cliqueVertices.size(); j++) {
                // if any two nodes are not connected, is not a clique
                if (i != j) {
                    if (!g.getAdjacency()[cliqueVertices.get(i)]
                            .contains(cliqueVertices.get(j))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
