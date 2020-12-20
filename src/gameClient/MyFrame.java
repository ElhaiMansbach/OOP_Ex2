
package gameClient;

import api.directed_weighted_graph;

import javax.swing.*;
import java.awt.*;

/**
 * This class represents a very simple GUI class to present a
 * game on a graph.
 */
public class MyFrame extends JFrame {
    private Arena _ar = new Arena();

    public MyFrame(directed_weighted_graph g) {
        super();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int height = (int) (dimension.height * 0.6);
        int width = (int) (dimension.width * 0.6);
        this.setSize(width, height);
        this.setVisible(true);
    }

    public void update(Arena ar) {
        this._ar = ar;
    }
}