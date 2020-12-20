package gameClient;

import api.edge_data;
import gameClient.util.Point3D;
import org.json.JSONObject;

public class CL_Pokemon implements Comparable<CL_Pokemon> {
    private edge_data _edge;
    private double _value;
    private int _type;
    private Point3D _pos;




    public CL_Pokemon(Point3D p, int t, double v, double s, edge_data e) {
        _type = t;
        _value = v;
        set_edge(e);
        _pos = p;
    }

    public static CL_Pokemon init_from_json(String json) {
        CL_Pokemon ans = null;
        try {
            JSONObject p = new JSONObject(json);
            int id = p.getInt("id");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ans;
    }

    public String toString() {
        return "F:{v=" + _value + ", t=" + _type + "}";
    }

    public edge_data get_edge() {
        return _edge;
    }

    public void set_edge(edge_data _edge) {
        this._edge = _edge;
    }

    public Point3D getLocation() {
        return _pos;
    }

    public int getType() {
        return _type;
    }

    public double getValue() {
        return _value;
    }


    /**
     * Compare to pokemons by value
     *
     * @param p represents the pokemon
     * @return
     */
    @Override
    public int compareTo(CL_Pokemon p) {
        Double val = this._value;
        return -val.compareTo(p._value);
    }
}
