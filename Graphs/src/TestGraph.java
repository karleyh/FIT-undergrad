import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class TestGraph {

    Graph g;

    @Before
    public void setUp() throws Exception {
        g = new Graph(10);

        g.addEdge(0, 1);
        g.addEdge(0, 2);
        g.addEdge(0, 7);

        g.addEdge(1, 4);

        g.addEdge(2, 5);
        g.addEdge(2, 6);

        g.addEdge(6, 3);
        g.addEdge(6, 9);
        g.addEdge(8, 4);
        g.addEdge(8, 9);

        g.addEdge(9, 7);

    }

    @Test
    public void testIsBipartite() {

        Graph g1, g2;
        g1 = new Graph(10);

        g1.addEdge(0, 1);
        g1.addEdge(0, 2);
        g1.addEdge(0, 7);
        g1.addEdge(0, 4);

        g1.addEdge(1, 4);

        g1.addEdge(2, 5);
        g1.addEdge(2, 6);

        g1.addEdge(6, 3);
        g1.addEdge(6, 9);
        g1.addEdge(8, 4);
        g1.addEdge(8, 9);

        g1.addEdge(9, 7);

        g2 = new Graph(9);

        // set 1 : 0 1 4 7 9
        // set 2 : 2 3 5 6

        g2.addEdge(0, 2);
        g2.addEdge(0, 6);
        g2.addEdge(0, 5);

        g2.addEdge(1, 2);

        g2.addEdge(3, 1);
        g2.addEdge(3, 8);

        g2.addEdge(5, 4);
        g2.addEdge(5, 7);

        g2.addEdge(6, 4);
        System.out.println("Bipartite test.");
        System.out.println("=================");

        boolean b1 = g1.isBipartite();
        boolean b2 = g2.isBipartite();

        System.out.println("First graph: " + b1);
        System.out.println("Second graph: " + b2);

        assertEquals(b1, false);
        assertEquals(b2, true);

        System.out.println("Pass");
    }

    @Test
    public void testAverageDistance() {
        System.out.println("Average diameter test.");
        System.out.println("=================");
        double avg = g.averageDistance();

        System.out.println("Average diameter: " + avg);
        assertEquals(avg, 2.24444, 0.00001);

        System.out.println("Pass");
    }

    @Test
    public void testCalculateDiameter() {
        System.out.println("Diameter test.");
        System.out.println("=================");
        int diameter = g.calculateDiameter();
        System.out.println("Diameter: " + diameter);
        assertEquals(diameter, 4);

        System.out.println("Pass");
    }

    @Test
    public void testComplement() {

        int E1 = this.g.getE();
        int V1 = this.g.getV();

        // same as graph g
        Graph g2 = new Graph(10);

        g2.addEdge(0, 1);
        g2.addEdge(0, 2);
        g2.addEdge(0, 7);

        g2.addEdge(1, 4);

        g2.addEdge(2, 5);
        g2.addEdge(2, 6);

        g2.addEdge(6, 3);
        g2.addEdge(6, 9);
        g2.addEdge(8, 4);
        g2.addEdge(8, 9);

        g2.addEdge(9, 7);

        g2.complement();

        ArrayList<Integer>[] adj1 = g.getAdjacency();
        ArrayList<Integer>[] adj2 = g2.getAdjacency();

        boolean isComplementCorrect = true;

        // for every vertex
        for (int i = 0; i < adj1.length; i++) {
            ArrayList<Integer> l1 = adj1[i];
            ArrayList<Integer> l2 = adj2[i];

            for (Integer integer : l2) {
                if (l1.contains(integer)) {
                    isComplementCorrect = false;
                }
            }

            for (Integer integer : l1) {
                if (l2.contains(integer)) {
                    isComplementCorrect = false;
                }
            }
        }

        assertEquals(isComplementCorrect, true);

    }

    @Test
    public void testIsTree() {

        System.out.println("Tree test.");
        System.out.println("=================");
        // fix source node
        int source = 0;

        // note that graphs resulting fro dfs, bfs algorithms should be trees
        // regardless of original graph
        Graph bfs = g.bfs(source);
        Graph dfs = g.dfs(source);

        boolean b1 = g.isTree();
        boolean b2 = bfs.isTree();
        boolean b3 = dfs.isTree();

        System.out.println("First graph: " + b1);
        System.out.println("First graph: " + b2);
        System.out.println("First graph: " + b3);

        assertEquals(b1, false);
        assertEquals(b2, true);
        assertEquals(b3, true);

        System.out.println("Pass");
    }

    @Test
    public void testMaxClique() {
        System.out.println("Max Clique test.");
        System.out.println("=================");
        Clique c1 = g.findMaxClique();
        System.out.println(c1);

        assertEquals(c1.getCliqueVertices().size(), 2);

        g.addEdge(6, 7);
        Clique c2 = g.findMaxClique();
        System.out.println(c2);

        assertEquals(c2.getCliqueVertices().size(), 3);

        System.out.println("Pass");
    }

}
