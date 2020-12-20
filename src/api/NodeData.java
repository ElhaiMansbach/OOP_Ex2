package api;

import gameClient.util.Point3D;

public class NodeData implements node_data, java.io.Serializable {
    static int keyCounter;
    int key;
    private double weight;
    private geo_location location;
    private String info;
    private int tag;

    /**
     * constructor
     */
    public NodeData(int key, Point3D location, double weight, String info, int tag) {
        this.key = key;
        this.location = location;
        this.weight = weight;
        this.info = info;
        this.tag = tag;
    }

    /**
     * copy constructor
     */
    public NodeData(NodeData n) {
        this.setKey(n.getKey());
        info = n.getInfo();
        tag = n.getTag();
        this.location = n.getLocation();
        this.weight = n.getWeight();
    }

    /**
     * default constructor
     */
    public NodeData() {
        this.key = keyCounter++;
        this.weight = 0;
        this.info = "";
        this.tag = 0;
        this.location = new Point3D(0, 0, 0);
    }

    /**
     * constructor
     */
    public NodeData(int key) {
        this.key = key;
        this.info = info;
        this.tag = tag;
        this.weight = 0;
        this.location = new Point3D(0, 0, 0);
    }

    public NodeData(int key, Point3D pos) {
        this.key = key;
        this.info = "";
        this.tag = 0;
        this.weight = 0;
        this.location = pos;
    }

    /**
     * Returns the key (id) associated with this node.
     *
     * @return
     */
    @Override
    public int getKey() {
        return key;
    }

    /**
     * Returns the location of this node, if
     * none return null.
     *
     * @return
     */
    @Override
    public geo_location getLocation() {
        return location;
    }

    /**
     * Allows changing this node's location.
     *
     * @param p - new new location  (position) of this node.
     */
    @Override
    public void setLocation(geo_location p) {
        this.location = p;
    }

    /**
     * Returns the weight associated with this node.
     *
     * @return
     */
    @Override
    public double getWeight() {
        return weight;
    }

    /**
     * Allows changing this node's weight.
     *
     * @param w - the new weight
     */
    @Override
    public void setWeight(double w) {
        this.weight = w;
    }

    /**
     * Returns the remark (meta data) associated with this node.
     *
     * @return
     */
    @Override
    public String getInfo() {
        return info;
    }

    /**
     * Allows changing the remark (meta data) associated with this node.
     *
     * @param s
     */
    @Override
    public void setInfo(String s) {
        this.info = s;
    }

    /**
     * Temporal data (aka color: e,g, white, gray, black)
     * which can be used be algorithms
     *
     * @return
     */
    @Override
    public int getTag() {
        return tag;
    }

    /**
     * Allows setting the "tag" value for temporal marking an node - common
     * practice for marking by algorithms.
     *
     * @param t - the new value of the tag
     */
    @Override
    public void setTag(int t) {
        this.tag = t;
    }

    /**
     * Allow setting the node_data's key
     *
     * @param key
     */
    private void setKey(int key) {
        this.key = key;
    }

    public String toString() {
        String s = "key:" + key + " weight:" + weight;
        return s;
    }
}