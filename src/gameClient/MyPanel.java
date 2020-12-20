package gameClient;

import api.*;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.List;

public class MyPanel extends javax.swing.JPanel {
    private static Arena _ar = new Arena();
    private directed_weighted_graph graph;
    private gameClient.util.Range2Range _w2f;
    private Graphics2D g2D;
    private static Integer id, level;
    private Image agent, pokemon1, pokemon2, background;


    /**
     * Constructor (graph)
     * @param g represents the game graph
     */
    MyPanel(directed_weighted_graph g) {
        super();
        this.graph = g;
        //Gets all images from resources folder
        this.agent = new ImageIcon("./resources/pok.png").getImage();
        this.pokemon1 = new ImageIcon("./resources/ch.png").getImage();
        this.pokemon2 = new ImageIcon("./resources/s.png").getImage();
        this.background = new ImageIcon("./resources/forest.jpg").getImage();
    }

    /**
     * Update the arena during running
     * @param ar represents the arena
     */
    public void update(Arena ar) {
        this._ar = ar;
        updateFrame();
    }

    /**
     * Update the frame size and the graph.
     */
    private void updateFrame() {
        Range rx = new Range(20, this.getWidth() - 20);
        Range ry = new Range(this.getHeight() - 10, 150);
        Range2D frame = new Range2D(rx, ry);
        directed_weighted_graph g = _ar.getGraph();
        _w2f = Arena.w2f(g, frame);
    }

    /**
     * Paint all objects on the frame
     * @param g
     */
    public void paint(Graphics g) {
        int w = this.getWidth();
        int h = this.getHeight();
        g2D = (Graphics2D) g;
        g2D.clearRect(0, 0, w, h);
        g2D.drawImage(background, 0, 0, w, h, null);
        updateFrame();
        drawGraph(g2D);
        drawPokemons(g2D);
        drawAgants(g2D);
        drawInfo(g2D);
    }

    /**
     * Draw the information about the game (level, time left, current score) on the game board.
     * @param g
     */
    private void drawInfo(Graphics g) {
        int x0 = this.getWidth() / 70;
        int y0 = this.getHeight() / 20;
        g.setFont(new Font("Calibri ", Font.BOLD, 27));
        g.setColor(Color.BLACK);
        g.drawString("LEVEL: " + level, x0 * 16, y0);
        g.drawString("TIME: " + _ar.getGame().timeToEnd() / 1000, x0 * 36, y0);
        g.drawString("SCORE: " + getGrade(), x0 * 56, y0);
    }

    /**
     * Draw the graph on the game board
     * @param g
     */
    private void drawGraph(Graphics2D g) {
        directed_weighted_graph gg = _ar.getGraph();
        Iterator<node_data> iter = gg.getV().iterator();
        while (iter.hasNext()) {
            node_data n = iter.next();
            g.setColor(Color.decode("#040BEB"));
            drawNode(n, 5, g);
            Iterator<edge_data> itr = gg.getE(n.getKey()).iterator();
            while (itr.hasNext()) {
                edge_data e = itr.next();
                g.setColor(Color.decode("#022C01"));
                drawEdge(e, g);
            }
        }
    }

    /**
     * Draw the pokemons on the game board
     * @param g
     */
    private void drawPokemons(Graphics g) {
        List<CL_Pokemon> fs = _ar.getPokemons();
        if (fs != null) {
            Iterator<CL_Pokemon> itr = fs.iterator();
            while (itr.hasNext()) {
                CL_Pokemon f = itr.next();
                Point3D c = f.getLocation();
                int r = 10;
                if (c != null) {
                    geo_location fp = this._w2f.world2frame(c);
                    if (f.getType() < 0) {
                        g.drawImage(pokemon2, (int) fp.x() - r, (int) fp.y() - r, 4 * r, 4 * r, null);
                    } else
                        g.drawImage(pokemon1, (int) fp.x() - r, (int) fp.y() - r, 4 * r, 5 * r, null);
                }
            }
        }
    }

    /**
     * Draw the agents on the game board
     * @param g
     */
    private void drawAgants(Graphics g) {
        List<CL_Agent> rs = _ar.getAgents();
        int i = 0;
        while (rs != null && i < rs.size()) {
            geo_location c = rs.get(i).getLocation();
            int r = 8;
            i++;
            if (c != null) {
                geo_location fp = this._w2f.world2frame(c);
                g.drawImage(agent, (int) fp.x() - r, (int) fp.y() - r, 3 * r, 3 * r, null);
            }
        }
    }

    /**
         * Draw the nodes of the graph and their id on the game board
     * @param g
     */
    private void drawNode(node_data n, int r, Graphics g) {
        geo_location pos = n.getLocation();
        geo_location fp = this._w2f.world2frame(pos);
        g.setFont(new Font("Calibri ", Font.BOLD, 17));
        g.fillOval((int) fp.x() - r, (int) fp.y() - r, 2 * r, 2 * r);
        g.drawString("" + n.getKey(), (int) fp.x(), (int) fp.y() - 2 * r);
    }

    /**
     * Draw the edges of the graph on the game board
     * @param g
     */
    private void drawEdge(edge_data e, Graphics2D g) {
        directed_weighted_graph gg = _ar.getGraph();
        geo_location s = gg.getNode(e.getSrc()).getLocation();
        geo_location d = gg.getNode(e.getDest()).getLocation();
        geo_location s0 = this._w2f.world2frame(s);
        geo_location d0 = this._w2f.world2frame(d);
        g.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.drawLine((int) s0.x(), (int) s0.y(), (int) d0.x(), (int) d0.y());
    }

    /**
     * Show a panel which ask the user to insert id
     * @return the id of the user.
     */
    public static int idPanel() {
        Object m = JOptionPane.showInputDialog(null, "Enter your ID number",
                "ID", JOptionPane.INFORMATION_MESSAGE);
        if (m == null) //Pressed cancel or exit
            System.exit(0);
        while (!(m instanceof Integer) && m.toString().length() != 9) {
            JOptionPane.showMessageDialog(null, "Invalid ID- please try again",
                    "Error!", JOptionPane.ERROR_MESSAGE);
            m = JOptionPane.showInputDialog(null, "Enter your ID number",
                    "ID", JOptionPane.INFORMATION_MESSAGE);
            if (m == null) //Pressed cancel or exit
                System.exit(0);
        }
        id = Integer.parseInt(m.toString());
        return id;
    }

    /**
     * Show a panel which ask the user to pick a level to play
     * @return the chosen level
     */
    public static int levelPanel() {
        Integer[] options = new Integer[24];
        for (int i = 0; i < 24; i++) {
            options[i] = i;
        }
        level = (Integer) JOptionPane.showInputDialog(null, "Pick a level:",
                "Level", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (level == null) //pressed cancel or exit
            System.exit(0);
        return level;
    }

    /**
     * Show a panel in the end of the game with the user results
     */
    public static void resultsPanel() {
        JOptionPane.showMessageDialog(null, "Your grade is: " + getGrade() +
                "\nAmount of moves:" + getMoves(), "Results", JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Get the grade from the string that represents the game
     * @return current grade of the user.
     */
    public static int getGrade() {
        String res = _ar.getGame().toString();
        String[] gameResults = res.split(",");
        return Integer.parseInt(gameResults[3].split(":")[1]);
    }

    /**
     * Get the amount of moves from the string that represents the game
     * @return the current amount of moves the agents made.
     */
    public static int getMoves() {
        String res = _ar.getGame().toString();
        String[] gameResults = res.split(",");
        return Integer.parseInt(gameResults[2].split(":")[1]);
    }
}