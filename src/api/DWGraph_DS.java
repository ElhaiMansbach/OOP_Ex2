package api;

import java.util.Collection;
import java.util.HashMap;

public class DWGraph_DS implements directed_weighted_graph, java.io.Serializable {
    private HashMap<Integer, node_data> nodes;
    private HashMap<Integer, HashMap<Integer, edge_data>> edges;
    int countEdges;
    int mc;


    /**
     * Default constructor.
     */
    public DWGraph_DS() {
        this.mc = 0;
        this.countEdges = 0;
        this.nodes = new HashMap<Integer, node_data>();
        this.edges = new HashMap<Integer, HashMap<Integer, edge_data>>();
    }

    /**
     * Builds a new graph from a given graph.
     *
     * @param g represents the given graph.
     */
    public DWGraph_DS(DWGraph_DS g) {
        this.mc = g.getMC();
        this.countEdges = g.edgeSize();
        this.nodes = (HashMap<Integer, node_data>) g.getNodes();
        this.edges = (HashMap<Integer, HashMap<Integer, edge_data>>) g.getEdges();
    }

    /**
     * returns the node_data by the node_id,
     *
     * @param key - the node_id
     * @return the node_data by the node_id, null if none.
     */
    @Override
    public node_data getNode(int key) {
        return nodes.get(key);
    }

    /**
     * returns the data of the edge (src,dest), null if none.
     * Note: this method should run in O(1) time.
     *
     * @param src
     * @param dest
     * @return
     */
    @Override
    public edge_data getEdge(int src, int dest) {
        if (edges.get(src) != null && edges.get(src).containsKey(dest))
            return edges.get(src).get(dest);
        else
            return null;
    }

    /**
     * adds a new node to the graph with the given node_data.
     * Note: this method should run in O(1) time.
     *
     * @param n
     */
    @Override
    public void addNode(node_data n) {
        try {
            if (nodes.get(n.getKey()) != null)
                throw new RuntimeException("ERR: The node is already exist in the graph");
            nodes.put(n.getKey(), n);
            mc++;
        } catch (Exception e) {
        }
    }

    /**
     * Connects an edge with weight w between node src to node dest.
     * * Note: this method should run in O(1) time.
     *
     * @param src  - the source of the edge.
     * @param dest - the destination of the edge.
     * @param w    - positive weight representing the cost (aka time, price, etc) between src-->dest.
     */
    @Override
    public void connect(int src, int dest, double w) {
        try {
            if (w <= 0)
                throw new RuntimeException("ERR: The weight should be positive");
            if (src == dest)
                throw new RuntimeException("ERR: The nodes should be different");
            if (!nodes.containsKey(src) || !nodes.containsKey(dest))
                throw new RuntimeException("ERR: The node/s does not exist");
            if (getEdge(src, dest) != null && getEdge(src, dest).getWeight() == w) { //The edge is exist
                return;
            }
            if (getEdge(src, dest) != null && getEdge(src, dest).getWeight() != w) { //The edge is exist with different weight
                mc++;
            }
            if (getEdge(src, dest) == null) { //There is no edge between the nodes
                mc++;
                countEdges++;
            }
            EdgeData e = new EdgeData(src, dest, w);
            if (!edges.containsKey(src)) { //If the src node is not exist
                HashMap<Integer, edge_data> newEdge = new HashMap<Integer, edge_data>(); //Empty hashmap
                edges.put(src, newEdge);
            }
            edges.get(src).put(dest, e);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * This method returns a pointer (shallow copy) for the
     * collection representing all the nodes in the graph.
     * Note: this method should run in O(1) time.
     *
     * @return Collection<node_data>
     */
    @Override
    public Collection<node_data> getV() {
        return nodes.values();
    }

    /**
     * This method returns a pointer (shallow copy) for the
     * collection representing all the edges getting out of
     * the given node (all the edges starting (source) at the given node).
     * Note: this method should run in O(k) time, k being the collection size.
     *
     * @return Collection<edge_data>
     */
    @Override
    public Collection<edge_data> getE(int node_id) {
        if (edges.containsKey(node_id)) //The node is exist
            return edges.get(node_id).values();
        return null;
    }

    /**
     * Deletes the node (with the given ID) from the graph -
     * and removes all edges which starts or ends at this node.
     * This method should run in O(k), V.degree=k, as all the edges should be removed.
     *
     * @param key
     * @return the data of the removed node (null if none).
     */
    @Override
    public node_data removeNode(int key) {
        if (nodes.containsKey(key)) { //If the node is exist
            if (edges.containsKey(key)) { //If there is an edge with this node
                for (Integer node : nodes.keySet()) {
                    // The key is yhe dest node
                    if (key != node && edges.containsKey(node) && edges.get(node).containsKey(key)) {
                        edges.get(node).remove(key);
                        countEdges--;
                        mc++;
                    }
                    if (key == node && edges.containsKey(key)) { // The key is the src node
                        int sumOfEdges = edges.get(key).size(); // How many edges has been removed
                        mc += sumOfEdges;
                        countEdges -= sumOfEdges;
                        edges.remove(key);
                    }
                }
            }
            mc++;
            node_data nodeRemoved = nodes.remove(key);
            return nodeRemoved;
        }
        return null;
    }

    /**
     * Deletes the edge from the graph,
     * Note: this method should run in O(1) time.
     *
     * @param src
     * @param dest
     * @return the data of the removed edge (null if none).
     */
    @Override
    public edge_data removeEdge(int src, int dest) {
        if (src != dest && edges.containsKey(src) && edges.get(src).containsKey(dest)) { //The edge is exist
            mc++;
            countEdges--;
            return edges.get(src).remove(dest);
        }
        return null;
    }

    /**
     * Returns the number of vertices (nodes) in the graph.
     * Note: this method should run in O(1) time.
     *
     * @return
     */
    @Override
    public int nodeSize() {
        return nodes.size();
    }

    /**
     * Returns the number of edges (assume directional graph).
     * Note: this method should run in O(1) time.
     *
     * @return
     */
    @Override
    public int edgeSize() {
        return countEdges;
    }

    /**
     * Returns the Mode Count - for testing changes in the graph.
     *
     * @return
     */
    @Override
    public int getMC() {
        return mc;
    }

    /**
     * Nodes getter.
     *
     * @return the hashmap that contains all the nodes.
     */
    public HashMap<Integer, node_data> getNodes() {
        return this.nodes;
    }

    /**
     * Edges getter.
     *
     * @return the hashmap that contains all the edges.
     */
    public HashMap<Integer, HashMap<Integer, edge_data>> getEdges() {
        return this.edges;
    }
}