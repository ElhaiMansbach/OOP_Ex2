package tests;

import api.*;
import gameClient.util.Point3D;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import static org.junit.Assert.*;

class DWGraph_DSTest {

    private DWGraph_DS g;
    private Point3D p1, p2, p3, p4, p5, p6, p7;
    private node_data n1, n2, n3, n4, n5, n6, n7;

    @BeforeEach
    void setupGraphs() {

        g = new DWGraph_DS();

        p1 = new Point3D(-1, -1, 1);
        p2 = new Point3D(-1, 1, 4);
        p3 = new Point3D(0, 0, 0);
        p4 = new Point3D(8, 1, -1);
        p5 = new Point3D(-1, 1, -1);
        p6 = new Point3D(10, 5, 1);
        p7 = new Point3D(9, 0, 1);

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
        g.connect(n1.getKey(), n3.getKey(), 3);
        g.connect(n2.getKey(), n4.getKey(), 5);
        g.connect(n3.getKey(), n2.getKey(), 6);
        g.connect(n3.getKey(), n5.getKey(), 8);
        g.connect(n4.getKey(), n3.getKey(), 11);
        g.connect(n4.getKey(), n6.getKey(), 2);
        g.connect(n5.getKey(), n4.getKey(), 2);
        g.connect(n5.getKey(), n1.getKey(), 7);
        g.connect(n5.getKey(), n7.getKey(), 5);
        g.connect(n6.getKey(), n7.getKey(), 3);
        g.connect(n7.getKey(), n4.getKey(), 10);
    }

    /**
     * Tests constructor.
     */
    @Test
    void testDWGraph_DS_Constructor() {
        DWGraph_DS gTest = new DWGraph_DS(g);
        assertEquals("Checks the nodes", g.getNodes(), gTest.getNodes());
        assertEquals("Checks the edges", g.getEdges(), gTest.getEdges());
    }

    /**
     * Tests getNode(key) function.
     */
    @Test
    void getNode() {
        int key1 = n1.getKey();
        int key2 = n2.getKey();
        int key_false = 10;
        assertEquals("Check the returned node", g.getNode(key1), n1);
        assertEquals("Check the returned node", g.getNode(key2), n2);
        assertNull("The node is not in the graph", g.getNode(key_false));
    }

    /**
     * Tests getEdge(src,dest) function.
     */
    @Test
    void getEdge() {
        int src_false = 0, dst_false = 9;
        assertNotNull("The edge is exist", g.getEdge(n1.getKey(), n2.getKey()));
        assertNull("There is only an edge in the other direction", g.getEdge(n7.getKey(), n6.getKey()));
        assertNull("The source node is not in the graph", g.getEdge(src_false, n1.getKey()));
        assertNull("The destination node is not in the graph", g.getEdge(n1.getKey(), dst_false));
        edge_data ex_edge = new EdgeData(n2.getKey(), n4.getKey(), 5);
        assertEquals("Check the returned edge", g.getEdge(n2.getKey(), n4.getKey()), ex_edge);
    }

    /**
     * Tests addNode(node_data) function.
     */
    @Test
    void addNode() {
        assertEquals(g.getNode(n1.getKey()), n1);
        assertEquals(g.getNode(n2.getKey()), n2);
        int node_size = g.nodeSize();
        g.addNode(n7); //Try adding a node which already exists
        if (node_size != g.nodeSize()) {
            fail("ERR: shouldn't add an existing node");
        }
    }

    /**
     * Tests connect(src,dest,weight) function.
     */
    @Test
    void connect() {
        assertEquals(g.getEdge(n1.getKey(), n2.getKey()).getSrc(), n1.getKey());
        assertEquals(g.getEdge(n1.getKey(), n2.getKey()).getDest(), n2.getKey());
        int numOfEdges = g.edgeSize();
        g.connect(n1.getKey(), n2.getKey(), 4); //Edge exist
        g.connect(n1.getKey(), n4.getKey(), -1); //negative weight
        g.connect(n1.getKey(), n1.getKey(), 1); //same node
        g.connect(n1.getKey(), 10, 1);// the destination node is not exist
        assertEquals("ERR: Shouldn't add these edges", numOfEdges, g.edgeSize());
    }

    /**
     * Tests getV() function.
     */
    @Test
    void getV() {
        Collection<node_data> excepted = new ArrayList<>();
        excepted.add(n1);
        excepted.add(n2);
        excepted.add(n3);
        excepted.add(n4);
        excepted.add(n5);
        excepted.add(n6);
        excepted.add(n7);
        Collection<node_data> actual = g.getV();
        assertEquals(excepted.toString(), actual.toString());
    }

    /**
     * Tests getE() function.
     */
    @Test
    void getE() {
        assertNotNull(g.getE(n1.getKey()));
        node_data n8 = new NodeData();
        assertNull(g.getE(n8.getKey()));
        Collection<edge_data> excepted = new ArrayList<>();
        excepted.add(new EdgeData(n1.getKey(), n2.getKey(), 4, null, 0));
        excepted.add(new EdgeData(n1.getKey(), n3.getKey(), 3, null, 0));
        Collection<edge_data> actual = g.getE(n1.getKey());
        if (!excepted.toString().equals(actual.toString()))
            fail("The edges should be the same");
    }

    /**
     * Tests removeNode(key) function.
     */
    @Test
    void removeNode() {
        n1 = g.removeNode(n1.getKey());
        n2 = g.removeNode(n2.getKey());
        node_data n11 = g.removeNode(n1.getKey());
        assertNull("Already removed this node", n11);
        assertEquals("5 nodes should be left", 5, g.nodeSize());
        assertEquals("7 edge should be left", 7, g.edgeSize());
    }

    /**
     * Tests removeEdge(src,dest) function.
     */
    @Test
    void removeEdge() {
        g.removeEdge(n4.getKey(), n6.getKey());
        g.removeEdge(n5.getKey(), n4.getKey());
        g.removeEdge(n5.getKey(), n1.getKey());
        g.removeEdge(n5.getKey(), n7.getKey());
        g.removeEdge(n6.getKey(), n7.getKey());
        g.removeEdge(n7.getKey(), n4.getKey());
        assertEquals("6 edges should be left", g.edgeSize(), 6);
        edge_data e_null = g.removeEdge(n4.getKey(), n6.getKey());
        assertNull("The edge is already removed", e_null);
        n1 = g.removeNode(n1.getKey());
        edge_data e1_s = g.removeEdge(n1.getKey(), n2.getKey()); //There is no such src node
        edge_data e1_d = g.removeEdge(n5.getKey(), n1.getKey()); //There is no such dest node
        assertNull("There is no edge (the source node has been removed) ", e1_s);
        assertNull("There is no edge (the destination node has been removed) ", e1_d);
    }

    /**
     * Tests nodeSize() function.
     */
    @Test
    void nodeSize() {
        assertEquals(7, g.nodeSize());
        g.removeNode(n1.getKey());
        assertEquals(6, g.nodeSize());
        g.removeNode(n1.getKey());
        assertEquals("There is no change", 6, g.nodeSize());
        g.addNode(n1);
        assertEquals(7, g.nodeSize());
    }

    @Test
    void edgeSize() {
        assertEquals(12, g.edgeSize());
        g.removeEdge(n1.getKey(), n2.getKey());
        assertEquals(11, g.edgeSize());
        g.removeNode(n7.getKey()); //Should remove 3 edges
        assertEquals(8, g.edgeSize());

    }

    @Test
    void getMC() {
        int current_mc = g.getMC();
        g.removeEdge(n1.getKey(), n2.getKey());
        current_mc++;
        assertEquals(current_mc, g.getMC());
        g.connect(n1.getKey(), n2.getKey(), 2);
        current_mc++;
        assertEquals(current_mc, g.getMC());
        g.addNode(new NodeData(8, new Point3D(0, 0, 0), 0, null, 0));
        current_mc++;
        assertEquals(current_mc, g.getMC());
        g.removeNode(n3.getKey());
        current_mc += 5;
        assertEquals(current_mc, g.getMC()); //Remove one node and 4 edges.
    }

    @Test
    void getNodes() {
        assertNotNull(g.getNodes());
        assertEquals(7, g.getNodes().size());
        g.removeNode(n1.getKey());
        assertEquals(6, g.getNodes().size());
        g.removeNode(n1.getKey()); //The node has already been removed from the graph
        assertEquals(6, g.getNodes().size());
        g.removeNode(n2.getKey());
        assertEquals(5, g.getNodes().size());
        g.addNode(n1);
        assertEquals(6, g.getNodes().size());
    }

    @Test
    void getEdges() {
        HashMap<Integer, edge_data> e = new HashMap<>(); //A src node with many edges
        e.put(n7.getKey(), new EdgeData(n5.getKey(), n7.getKey(), 5));
        e.put(n1.getKey(), new EdgeData(n5.getKey(), n1.getKey(), 7));
        e.put(n4.getKey(), new EdgeData(n5.getKey(), n4.getKey(), 2));
        assertTrue(g.getEdges().containsValue(e));

        HashMap<Integer, edge_data> e1 = new HashMap<>(); //A src node with single edge
        e1.put(n4.getKey(), new EdgeData(n2.getKey(), n4.getKey(), 5));
        assertTrue(g.getEdges().containsValue(e1));

        HashMap<Integer, edge_data> e2 = new HashMap<>();
        edge_data ex_edge2 = new EdgeData(n7.getKey(), n6.getKey(), 5);
        e2.put(n4.getKey(), ex_edge2);
        assertFalse(g.getEdges().containsValue(e2)); //The edge is not exist in the graph
    }
}