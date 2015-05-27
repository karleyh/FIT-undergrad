/*
 * Author : Karley Herschelman, kherschelman2012@my.fit.edu
 * Author : Josh Lear, jlear2013@my.fit.edu
 * Course : CSE 2010, Section E1, Spring 2014
 * Project: lab 14, Graphs II
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

// Graph.java
/**
 * @author kherschelman2012 store the graph G=(V,E)
 */
public class Graph {
    private int V; // number of vertices
    private int E; // number of edges
    /**
     * for every vertex there is a list of other vertices adjacent to it
     * (adjacency list representation of a graph)
     */
    private ArrayList<Integer>[] adj;

    // my floyd warshall matrix

    private int[][] warshMat;

    private void generateFloyd() {
        warshMat = new int[V][V];

        // set all matrix values to infinity
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                warshMat[i][j] = Integer.MAX_VALUE / 3;
            }
        }

        // set values that are directly connected to 1
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < adj[i].size(); j++) {
                warshMat[i][adj[i].get(j)] = 1;
            }
        }

        // check for a shorter path
        for (int k = 0; k < V; k++) {
            for (int i = 0; i < V; i++) {
                for (int j = 0; j < V; j++) {
                    if (warshMat[i][j] > warshMat[i][k] + warshMat[k][j]) {
                        warshMat[i][j] = warshMat[i][k] + warshMat[j][k];
                    }
                }
            }
        }

        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                //yuayyyy
                if (i == j) {
                    warshMat[i][j] = 0;
                }
            }
        }

    }

    /**
     * getters will be used for grading
     * 
     * @return the adjacency array
     */
    public ArrayList<Integer>[] getAdjacency() {
        return this.adj;
    }

    /**
     * checks if the graph is bipartite
     * @return true if bipartite.
     */
    public boolean isBipartite() {
        // if u and v ever contain the same node, this cant be bipartite
        ArrayList<Integer> u = new ArrayList<Integer>();
        ArrayList<Integer> v = new ArrayList<Integer>();
        boolean isU = false;
        // false if u contains the ith node, true if v contains the I'th node
        for (int i = 0; i < V; i++) {
            if (!v.contains(i) || (v.size() == 0)) {
                isU = true;
            } else if ((!u.contains(i)) || v.size() == 0) {
                isU = false;
            } else {
                return false;
            }
            for (int j = 0; j < adj[i].size(); j++) {
                if (isU) {
                  if(j==0)  {u.add(i);}
                    if (!u.contains(adj[i].get(j))) {
                        v.add(adj[i].get(j));
                    } else {
                        return false;
                    }
                } else {
                    if(j == 0){v.add(i);}
                    if (!v.contains(adj[i].get(j))) {
                        u.add(adj[i].get(j));
                    } else {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * check if the graph is connected
     * @return true if the graph is disconnected
     */
    public boolean isDisconnected() {
        for (int i = 0; i < V; i++) {
            if (adj[i].size() == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * calculate the average distance between the vertices
     * @return average distance
     */
    public double averageDistance() {
        generateFloyd();
        double sum = 0.0;
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                if (warshMat[i][j] != Integer.MAX_VALUE / 3) {
                    sum += warshMat[i][j];
                }
            }
        }
        return sum / (V * V - V);
    }

    /**
     * complement the graph
     */
    public void complement() {
        boolean[][] hasEdge = new boolean[V][V];
        //create and adjacency matrix
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < adj[i].size(); j++) {
                hasEdge[i][adj[i].get(j)] = true;              
            }
        }
        Graph temp = new Graph(V);
        
        //add edges where there are not edges in the matrix
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < adj[i].size(); j++) {
                if (!hasEdge[i][j]) {
                    temp.addEdge(i, j);
                }
            }
        }
        this.adj = temp.getAdjacency();
        this.E = temp.getE();
    }

    /**
     * @return true if the graph is a tree or false otherwise
     */
    public boolean isTree() {
        if (!isDisconnected() && (V - E == 1)) {
            return true;
        }
        return false;
    }

    /**
     * calculates the maximum diameter using the floyd matrix
     * @return diameter
     */
    public int calculateDiameter() {
        generateFloyd();
        int max = 0;
        for (int i = 0; i < V; i ++){
            for (int j = 0; j < V; j++){
                if (warshMat[i][j] > max){
                    max = warshMat[i][j];
                }
            }
        }
        return max;
        // return diameter of the graph
    }

    /**
     * return maximum Clique of a graph. Note that the clique but not be unique,
     * if that is the case return any one clique containing the most amount of
     * vertices
     * 
     * @return the clique with the most vertices, or null if there are not
     *         cliques
     */
    public Clique findMaxClique() {

        // find all cliques
        ArrayList<Clique> cliques = new ArrayList<Clique>();
        ArrayList<Integer> set;
        // go to each vertex
        for (int i = 0; i < V; i++) {
            Clique clique = new Clique(this);
            set = new ArrayList<Integer>();
            // how many sizes
            for (int k = 0; k <= adj[i].size(); k++) {
                if (k == 0) {

                    set.add(i);
                } else {
                    set.add(adj[i].get(k - 1));
                }
                // sets
                for (int j = k; j < adj[i].size(); j++) {
                    // add values
                    for (int l = 0; l < set.size(); l++) {
                        clique.setCliqueVertices(set.get(l));

                    }
                    // add new value
                    clique.setCliqueVertices(adj[i].get(j));
                    if (clique.isClique()) {

                        // if subset is a clique, add to array list of cliques
                        cliques.add(clique);

                    }
                    // reset clique
                    clique = new Clique(this);
                }
            }
        }

        // find max clique
        int max = 0;
        //if you read this put a smiley face on the report
        Clique maxClique = null;
        if (cliques.size() <= 0) {
            Clique clique = new Clique(this);
            clique.setCliqueVertices(0);
            return clique;
        }

        for (int i = 0; i < cliques.size(); i++) {
            if (cliques.get(i).getCliqueVertices().size() > max) {
                max = cliques.get(i).getCliqueVertices().size();
                maxClique = cliques.get(i);
            }
        }

        return maxClique;
    }

    /**
     * creates a graph using input from main
     * 
     * @param V
     *            number of vertices
     */
    @SuppressWarnings("unchecked")
    public Graph(int V) {
        // Initialize a graph with V vertices and 0 edges
        this.setV(V);
        adj = new ArrayList[V];
        for (int i = 0; i < V; i++) {
            adj[i] = new ArrayList<Integer>();
        }
    }

    /**
     * @param filename
     *            parses a file to create a graph
     */
    @SuppressWarnings("unchecked")
    public Graph(String filename) {
        try {
            Scanner file = new Scanner(new File(filename));
            this.setV(file.nextInt());
            file.nextLine();
            this.setE(0);
            adj = new ArrayList[getV()];
            for (int i = 0; i < getV(); i++) {
                adj[i] = new ArrayList<Integer>();
            }
            int k = 0;
            while (k < getV()) {
                String line = file.nextLine();
                String[] exp = line.split(new String("\\s+"));
                for (int i = 1; i < exp.length; i++) {
                    addEdge(Integer.parseInt(exp[i]), k);
                }
                k++;
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // parse graph from a txt file (this constructor will be used during
        // grading)
    }

    /**
     * creates a string to represent the graph
     * 
     * @return printable representation of a Graph
     */
    public String toString() {
        String s = getV() + " vertices, " + getE() + " edges  \n";
        for (int i = 0; i < getV(); i++) {
            s += i + ":";
            for (int j = 0; j < adj[i].size(); j++) {
                s += " " + adj[i].get(j);
            }
            s += "\n";
        }
        // return printable representation of a Graph
        return s;
    }

    /**
     * breadth first search
     * 
     * @param s
     *            starting node
     * @return new graph
     */
    @SuppressWarnings("unchecked")
    public Graph bfs(int s) {
        // new graph to return
        Graph ans = new Graph(getV());
        // has the value been visited
        boolean[] taken = new boolean[getV()];
        // queue to visit values in order
        Queue<Integer> order = new LinkedList<Integer>();
        int val = s;
        // add value to queue
        order.add(val);
        // while there are values in the queue
        while (!order.isEmpty()) {
            int i = order.remove();
            for (int j = 0; j < adj[i].size(); j++) {
                val = adj[i].get(j);
                if (!taken[val]) {
                    order.add(val);
                    // add the edge
                    ans.addEdge(val, i);
                    taken[val] = true;
                }
            }
            taken[i] = true;
        }
        return ans;
    }

    /**
     * depth first search
     * 
     * @param s
     *            starting node
     * @return new graph
     */
    public Graph dfs(int s) {
        // new graph to return
        Graph ans = new Graph(getV());
        // has the value been visited
        boolean[] taken = new boolean[getV()];
        // queue to visit values in order
        Stack<Integer> order = new Stack<Integer>();
        int val = s;
        // add value to queue
        order.push(val);

        // while there are values in the queue
        while (!order.isEmpty()) {
            int i = order.pop();
            taken[i] = true;
            for (int j = 0; j < adj[i].size(); j++) {
                val = adj[i].get(j);
                if (!taken[val]) {
                    order.push(val);
                    taken[val] = true;
                }
            }
            ans.addEdge(val, i);
            taken[i] = true;
        }

        return ans;
        // depth-first search from a single source s
    }

    /**
     * create an edge from the vertex v to the vertex w (edge is undirected) add
     * an undirected edge between two vertices
     * 
     * @param v
     *            vertex 1
     * @param w
     *            vertex 2
     */
    public void addEdge(int v, int w) {
        if (!adj[v].contains(w)) {
            setE(getE() + 1);
            adj[v].add(w);
            adj[w].add(v);
        }
    }

    /**
     * Override equals methods for graphs: compare number of edges, vertices and
     * the adjacency lists correspondence. Nothing to implement here ( will be
     * used for grading)
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Graph)) {
            return false;
        }
        Graph that = (Graph) other;
        boolean isAdjSame = true;
        // iterate over adjacency list to check if they are the same
        try {
            for (int i = 0; i < Math.max(this.adj.length, that.adj.length); i++) {
                // sort so that order doesn’t matter
                Collections.sort(this.adj[i]);
                Collections.sort(that.adj[i]);
                for (int j = 0; j < Math.max(this.adj[i].size(),
                        that.adj[i].size()); j++) {
                    if (this.adj[i].get(j) != that.adj[i].get(j)) {
                        isAdjSame = false;
                        // once at least one is found there is no need to
                        // continue
                        break;
                    }
                }
                if (!isAdjSame)
                    break;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            isAdjSame = false;
        }
        // if graphs are the same all should match
        return this.getV() == (that.getV()) && this.getE() == (that.getE())
                && isAdjSame;
    }

    /**
     * @return the E
     */
    public int getE() {
        return E;
    }

    /**
     * @param e
     *            the e to set
     */
    public void setE(int e) {
        E = e;
    }

    /**
     * @return the v
     */
    public int getV() {
        return V;
    }

    /**
     * @param v
     *            the v to set
     */
    public void setV(int v) {
        V = v;
    }
}
