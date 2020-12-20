package api;

import com.google.gson.*;
import gameClient.util.Point3D;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class DWGraph_Algo implements dw_graph_algorithms {
    directed_weighted_graph g;

    /**
     * Default constructor.
     */
    public DWGraph_Algo() {
        // TODO Auto-generated constructor stub
    }

    /**
     * Constructor
     *
     * @param graph represents the given graph
     */
    public DWGraph_Algo(directed_weighted_graph graph) {
        this.g = graph;
    }

    /**
     * Init the graph on which this set of algorithms operates on.
     *
     * @param g
     */
    @Override
    public void init(directed_weighted_graph g) {
        this.g = g;
    }

    /**
     * Return the underlying graph of which this class works.
     *
     * @return
     */
    @Override
    public directed_weighted_graph getGraph() {
        return g;
    }

    /**
     * Compute a deep copy of this weighted graph.
     *
     * @return
     */
    @Override
    public directed_weighted_graph copy() {
        DWGraph_DS gc = new DWGraph_DS();
        try {
            for (node_data n : g.getV()) { //Add all nodes
                gc.addNode(n);
            }
            for (node_data n : g.getV()) {
                for (edge_data e : g.getE(n.getKey()))
                    gc.connect(e.getSrc(), e.getDest(), e.getWeight());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        gc.mc = g.getMC();
        return gc;
    }

    /**
     * Returns true if and only if (iff) there is a valid path from each node to each
     * other node. NOTE: assume directional graph (all n*(n-1) ordered pairs).
     *
     * @return
     */
    @Override
    public boolean isConnected() {
        if (g.getV() == null || g.getV().size() == 1) return true; //There are no nodes or just one node
        for (node_data n : g.getV()) //Initialize all nodes to be unvisited
            n.setTag(0);
        boolean first = true;
        for (node_data n : g.getV()) { //Run BFS only one time (on the first node)
            if (!first) break;
            first = false;
            if (!bfs(g, n.getKey())) //BFS traversal doesn't visit all nodes.
                return false;
        }
        directed_weighted_graph tran_g = getTranspose(g); //Create a reversed graph
        for (node_data n : tran_g.getV()) { //Run bfs only one time (on the first node)
            return bfs(tran_g, n.getKey());
        }
        return false;
    }

    /**
     * returns the length of the shortest path between src to dest
     * Note: if no such path --> returns -1
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        try {
            for (Iterator<node_data> it = g.getV().iterator(); it.hasNext(); ) {
                node_data n = it.next();
                n.setWeight(Double.POSITIVE_INFINITY);
                n.setInfo("");
                n.setTag(0);
            }
            g.getNode(src).setWeight(0);
            Queue<node_data> Q = new LinkedList<node_data>();
            Q.add(g.getNode(src));
            for (Iterator<node_data> it = g.getV().iterator(); it.hasNext(); ) {
                node_data v = it.next();
                if (v.getKey() != src)
                    Q.add(v);
            }
            while (!Q.isEmpty()) {
                int u = findMinNode(Q);
                g.getNode(u).setTag(1);
                for (Iterator<edge_data> edgeIt = g.getE(u).iterator(); edgeIt.hasNext(); ) {
                    edge_data e = edgeIt.next();
                    node_data e_dest = g.getNode(e.getDest());
                    if (e_dest.getTag() == 0 && e_dest.getWeight() > g.getNode(u).getWeight() + e.getWeight()) {
                        e_dest.setWeight(g.getNode(u).getWeight() + e.getWeight());
                        e_dest.setInfo("" + u);
                    }
                }
            }
            for (Iterator<node_data> nodeIt = g.getV().iterator(); nodeIt.hasNext(); ) {
                nodeIt.next().setTag(0);
            }
        } catch (Exception e) {
        }

        if (g.getNode(dest).getWeight() == Double.POSITIVE_INFINITY) //The node hasn't been reached
            return -1;
        return g.getNode(dest).getWeight();
    }

    /**
     * returns the the shortest path between src to dest - as an ordered List of nodes:
     * src--> n1-->n2-->...dest
     * see: https://en.wikipedia.org/wiki/Shortest_path_problem
     * Note if no such path --> returns null;
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public List<node_data> shortestPath(int src, int dest) {
        List<node_data> path = new ArrayList<node_data>();
        double dist = shortestPathDist(src, dest);
        if (dist != -1) { //There is a path
            node_data v = g.getNode(dest);
            path.add(v);
            while (!v.getInfo().isEmpty()) {
                int prev_node = Integer.parseInt(v.getInfo());
                path.add(g.getNode(prev_node));
                v.setInfo("");
                v = g.getNode(prev_node);
            }
            List<node_data> reversed_path = new ArrayList<node_data>();
            while (path.size() != 0)
                reversed_path.add(path.remove(path.size() - 1));
            return reversed_path;
        }
        return null;
    }

    /**
     * Saves this weighted (directed) graph to the given
     * file name - in JSON format
     *
     * @param file - the file name (may include a relative path).
     * @return true - iff the file was successfully saved
     */
    @Override
    public boolean save(String file) {
        JsonObject json = new JsonObject();
        JsonArray jArrayOfEdges = new JsonArray();
        JsonArray jArrayOfNodes = new JsonArray();
        json.add("Edges", jArrayOfEdges);
        json.add("Nodes", jArrayOfNodes);
        for (node_data n : g.getV()) {
            JsonObject jsonNode = new JsonObject();
            jsonNode.addProperty("pos", "" + n.getLocation().x() + "," + n.getLocation().y() + "," + n.getLocation().z());
            jsonNode.addProperty("id", n.getKey());
            jArrayOfNodes.add(jsonNode);
            for (edge_data e : g.getE(n.getKey())) {
                JsonObject jsonEdge = new JsonObject();
                jsonEdge.addProperty("src", e.getSrc());
                jsonEdge.addProperty("w", e.getWeight());
                jsonEdge.addProperty("dest", e.getDest());
                jArrayOfEdges.add(jsonEdge);
            }
        }
        try {
            Gson g = new Gson();
            File f = new File(file);
            FileWriter myWriter = new FileWriter(f);
            myWriter.write(g.toJson(json));
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * This method load a graph to this graph algorithm.
     * if the file was successfully loaded - the underlying graph
     * of this class will be changed (to the loaded one), in case the
     * graph was not loaded the original graph should remain "as is".
     *
     * @param file - file name of JSON file
     * @return true - iff the graph was successfully loaded.
     */
    @Override
    public boolean load(String file) {
        try {
            DWGraph_DS gr = new DWGraph_DS();
            String jsonGraph = new String(Files.readAllBytes(Paths.get(file))); // Read the json content
            JsonObject json_obj = JsonParser.parseString(jsonGraph).getAsJsonObject();
            JsonArray jsonNodes = json_obj.getAsJsonArray("Nodes");  //Convert the json array to nodes
            for (JsonElement n : jsonNodes) {
                JsonObject node = (JsonObject) n;
                NodeData newNode = new NodeData(node.get("id").getAsInt()); //Create new node with given id
                String pos = node.get("pos").getAsString(); //Take the position string from the json file
                int firstIndex = pos.indexOf(",");
                int lastIndex = pos.lastIndexOf(",");
                double x = Double.parseDouble(pos.substring(0, firstIndex));
                double y = Double.parseDouble(pos.substring(firstIndex + 1, lastIndex));
                double z = Double.parseDouble(pos.substring(lastIndex + 1));
                newNode.setLocation(new Point3D(x, y, z)); //Set the given location to the node
                gr.addNode(newNode); //Add the node to the graph
            }
            JsonArray Jedges = json_obj.getAsJsonArray("Edges"); //Convert the edges json array to edges
            for (JsonElement edge : Jedges) {
                JsonObject temp = (JsonObject) edge; //Extract the data of the edge json object
                int src = temp.get("src").getAsInt();
                int dest = temp.get("dest").getAsInt();
                double weight = temp.get("w").getAsDouble();
                gr.connect(src, dest, weight); //Add the edges
            }
            init(gr);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Graph getter.
     *
     * @return the graph in this class.
     */
    public directed_weighted_graph getG() {
        return g;
    }

    /**
     * Bfs utility function- does breadth traversal across the graph and marks the vertices it visited.
     *
     * @param key the id of the starting node.
     */
    private boolean bfs(directed_weighted_graph graph, int key) {
        Queue<Integer> queue = new LinkedList<>();
        int element; //Save the current key node dequeue from the queue
        graph.getNode(key).setTag(1); //The node is visited
        queue.add(key);
        while (!queue.isEmpty()) {
            element = queue.remove();
            if (graph.getE(element) == null) return false; //The node has no edges to move on
            for (edge_data e : graph.getE(element)) { //The neighbors of the current node
                node_data n = graph.getNode(e.getDest());
                if (n.getTag() == 0) { //Have not visited
                    queue.add(n.getKey());
                    n.setTag(1);
                }
            }
        }
        for (node_data n : graph.getV()) { //Check if there is unvisited node
            if (n.getTag() == 0)
                return false;
        }
        return true;
    }

    /**
     * Transposes the original graph.
     *
     * @return transpose of this graph
     */
    private directed_weighted_graph getTranspose(directed_weighted_graph gr) {
        directed_weighted_graph tran_g = new DWGraph_DS();

        //Add all the nodes
        for (Iterator<node_data> it = gr.getV().iterator(); it.hasNext(); ) {
            node_data v = it.next();
            v.setTag(0);
            tran_g.addNode(v);
        }
        //Add all the edges (but in opposite direction)
        for (Iterator<node_data> it = gr.getV().iterator(); it.hasNext(); ) {
            node_data v = it.next();
            if (gr.getE(v.getKey()) != null)
                for (Iterator<edge_data> edgeIt = gr.getE(v.getKey()).iterator(); edgeIt.hasNext(); ) {
                    edge_data e = edgeIt.next();
                    node_data e_dest = gr.getNode(e.getDest());
                    tran_g.connect(e_dest.getKey(), v.getKey(), e.getWeight());
                }
        }
        return tran_g;
    }

    /**
     * Finds the node with the minimum weight and removes it from the queue.
     *
     * @param q represents the queue with the unvisited nodes.
     * @return the node with the minimum weight
     */
    private int findMinNode(Queue<node_data> q) {
        double weight = Double.POSITIVE_INFINITY;
        int minNode = 0;
        node_data temp = null;
        for (Iterator<node_data> it = q.iterator(); it.hasNext(); ) {
            node_data node = it.next();
            if (node.getWeight() < weight) {
                weight = node.getWeight();
                minNode = node.getKey();
                temp = node;
            }
        }
        q.remove(temp);
        return minNode;
    }

    /**
     * Convert a string to a graph
     *
     * @param json The content of the json file that represents the graph
     * @return the converted graph
     */
    public directed_weighted_graph Json2Graph(String json) {
        dw_graph_algorithms ga = new DWGraph_Algo();
        try {
            String j = (new JSONObject(json)).toString(4);
            File file = new File("data/tempGraph" + ".json");
            FileWriter myWriter = new FileWriter(file);
            myWriter.write(j);
            myWriter.close();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        ga.load("data/tempGraph" + ".json");
        return ga.getGraph();
    }
}

