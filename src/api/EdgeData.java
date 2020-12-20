package api;

public class EdgeData implements edge_data, java.io.Serializable {
    private int src;
    private int dest;
    private double weight;
    private String info;
    private int tag;

    /**
     * copy constructor
     */
    public EdgeData(int src, int dest, double w, String s, int tag) {
        this.src = src;
        this.dest = dest;
        this.weight = w;
        this.info = s;
        this.tag = tag;
    }

    /**
     * constructor
     */
    public EdgeData(int src, int dest, double w) {
        this.src = src;
        this.dest = dest;
        this.weight = w;
        this.info = "";
        this.tag = 0;
    }

    /**
     * The id of the source node of this edge.
     * @return
     */
    @Override
    public int getSrc() {
        return src;
    }

    /**
     * The id of the destination node of this edge
     * @return
     */
    @Override
    public int getDest() {
        return dest;
    }

    /**
     * @return the weight of this edge (positive value).
     */
    @Override
    public double getWeight() {
        return weight;
    }

    /**
     * Returns the remark (meta data) associated with this edge.
     * @return
     */
    @Override
    public String getInfo() {
        return info;
    }

    /**
     * Allows changing the remark (meta data) associated with this edge.
     * @param s
     */
    @Override
    public void setInfo(String s) {
        this.info = s;
    }

    /**
     * Temporal data (aka color: e,g, white, gray, black)
     * which can be used be algorithms
     * @return
     */
    @Override
    public int getTag() {
        return tag;
    }

    /**
     * This method allows setting the "tag" value for temporal marking an edge - common
     * practice for marking by algorithms.
     * @param t - the new value of the tag
     */
    @Override
    public void setTag(int t) {
        this.tag = t;
    }

    public String toString() {
        String s = "src:"+src+" dest:"+dest+" weight:"+weight;
        return s;
    }

    public boolean equals(Object o){
        if(o instanceof EdgeData){
          EdgeData edge = (EdgeData)o;
            if(edge.getSrc() == this.src && edge.getDest() == this.dest &&
                    edge.getWeight() == this.weight && edge.getInfo() == this.info &&
                    edge.getTag() == this.getTag())
                return true;
        }
        return false;
    }
}

