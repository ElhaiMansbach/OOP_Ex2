package tests;

import api.*;
import gameClient.util.Point3D;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DWGraph_AlgoTest {

    DWGraph_DS g = new DWGraph_DS();
    DWGraph_Algo ga = new DWGraph_Algo();

    @BeforeEach
    void setupGraphs() {
        ga.init(g);

        Point3D p1, p2, p3, p4, p5, p6, p7;
        node_data n1, n2, n3, n4, n5, n6, n7;


        p1 = new Point3D(-10, -10, 0);
        p2 = new Point3D(-10, 10, 0);
        p3 = new Point3D(40, 0, 0);
        p4 = new Point3D(80, 10, 0);
        p5 = new Point3D(80, -10, 0);
        p6 = new Point3D(90, 30, 0);
        p7 = new Point3D(100, 0, 0);


        n1 = new NodeData(1, p1, 0, null, 0);
        n2 = new NodeData(2, p2, 0, null, 0);
        n3 = new NodeData(3, p3, 0, null, 0);
        n4 = new NodeData(4, p4, 0, null, 0);
        n5 = new NodeData(5, p5, 0, null, 0);
        n6 = new NodeData(6, p6, 0, null, 0);
        n7 = new NodeData(7, p7, 0, null, 0);

        g.addNode(n1);
        g.addNode(n2);
        g.addNode(n3);
        g.addNode(n4);
        g.addNode(n5);
        g.addNode(n6);
        g.addNode(n7);

        g.connect(n1.getKey(), n2.getKey(), 4);
        g.connect(n2.getKey(), n1.getKey(), 4);
        g.connect(n1.getKey(), n3.getKey(), 3);
        g.connect(n2.getKey(), n4.getKey(), 5);
        g.connect(n3.getKey(), n2.getKey(), 1);
        g.connect(n3.getKey(), n5.getKey(), 8);
        g.connect(n4.getKey(), n3.getKey(), 11);
        g.connect(n4.getKey(), n6.getKey(), 2);
        g.connect(n5.getKey(), n4.getKey(), 2);
        g.connect(n5.getKey(), n1.getKey(), 7);
        g.connect(n5.getKey(), n7.getKey(), 5);
        g.connect(n6.getKey(), n7.getKey(), 3);
        g.connect(n7.getKey(), n4.getKey(), 10);
    }


    @Test
    void init() {
        ga.init(g);
        assertNotNull(ga.getGraph());
    }

    @Test
    void getGraph() {
        assertNotNull(ga);
        assertEquals(ga.getGraph(), g);
        g.removeNode(1);
        assertEquals(ga.getG(), g); //The graph in ga should  be changed
    }

    @Test
    void copy() {
        directed_weighted_graph newG = ga.copy();
        assertEquals(newG.getMC(), g.getMC());
        assertEquals(newG.getV().size(), g.getV().size());
        for (int i = 1; i <= 7; i++)
            assertEquals(newG.getE(i).size(), g.getE(i).size());
    }

    /**
     * Test isConnected() function
     */
    @Test
    void isConnected() {
        // assertTrue(ga.isConnected(), "Error: failed to return true");
        edge_data e1 = g.removeEdge(5, 1);
        edge_data e2 = g.removeEdge(2, 1);
        assertFalse(ga.isConnected(), "Error: failed to return false");
        g.connect(e1.getSrc(), e1.getDest(), e1.getWeight());
        g.connect(e2.getSrc(), e2.getDest(), e2.getWeight());
        g.removeEdge(4, 6);
        assertFalse(ga.isConnected(), "Error: failed to return false");
    }

    @Test
    void shortestPathDist() {
        if (11 != ga.shortestPathDist(1, 6))
            fail("Failed! The shortest distance is 11");
        edge_data e1 = g.removeEdge(2, 4);
        if (15 != ga.shortestPathDist(1, 6))
            fail("Failed! The shortest distance now is 15");
        edge_data e2 = g.removeEdge(4, 6);
        if (-1 != ga.shortestPathDist(1, 6))
            fail("Failed! There is no path");
        g.connect(e1.getSrc(), e1.getDest(), e1.getWeight());
        g.connect(e2.getSrc(), e2.getDest(), e2.getWeight());
        if (10 != ga.shortestPathDist(2, 7))
            fail("Failed! The shortest distance is 10");
        g.connect(2, 6, 1);
        if (4 != ga.shortestPathDist(2, 7))
            fail("Failed! The shortest distance now is 4");
    }

    @Test
    void shortestPath() {
        List<node_data> ans = ga.shortestPath(1, 6);
        String ans_keys = "";
        for (node_data n : ans)
            ans_keys += n.getKey();
        if (!ans_keys.equals("1246") && !ans_keys.equals("13246"))
            fail("There is shorter path");
        edge_data e1 = g.removeEdge(2, 4);
        ans = ga.shortestPath(1, 6);
        ans_keys = "";
        for (node_data n : ans)
            ans_keys += n.getKey();
        if (!ans_keys.equals("13546"))
            fail("There is shorter path");
        edge_data e2 = g.removeEdge(4, 6);
        assertNull(ga.shortestPath(1, 6), "There is no path");
        g.connect(e1.getSrc(), e1.getDest(), e1.getWeight());
        g.connect(e2.getSrc(), e2.getDest(), e2.getWeight());
        ans = ga.shortestPath(2, 7);
        ans_keys = "";
        for (node_data n : ans)
            ans_keys += n.getKey();
        if (!ans_keys.equals("2467"))
            fail("There is shorter path");
        g.connect(2, 6, 1);
        ans = ga.shortestPath(2, 7);
        ans_keys = "";
        for (node_data n : ans)
            ans_keys += n.getKey();
        if (!ans_keys.equals("267"))
            fail("There is shorter path");
    }

    @Test
    void save() {
        try {
            ga.save("TestSave");
            DWGraph_Algo ga2 = new DWGraph_Algo();
            ga2.load("TestSave");
            assertTrue(ga.getG().getV().size() == ga2.getG().getV().size());
            assertTrue(ga.getG().getE(1).size() == ga2.getG().getE(1).size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void load() {
        {
            try {
                ga.save("TestLoad");
                DWGraph_Algo ga2 = new DWGraph_Algo();
                ga2.load("TestLoad");
                assertTrue(ga.getG().getV().size() == ga2.getG().getV().size());
                assertTrue(ga.getG().getE(1).size() == ga2.getG().getE(1).size());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    void Json2Graph() {
        DWGraph_Algo new_ga = new DWGraph_Algo();
        for (int i = 0; i < 6; i++) {
            String filename = "data/A" + i;
            try {
                String file = new String(Files.readAllBytes(Paths.get(filename)));
                new_ga.Json2Graph(file);
                assertNotEquals(g, new_ga.getGraph());
            } catch (IOException e) {
            }
        }
    }
}