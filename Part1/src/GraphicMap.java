import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GraphicMap extends JPanel {

    private NodeMap map;
    private List<Node> path;

    public GraphicMap(String[][] inputMap) {

        setPreferredSize(new Dimension(inputMap[0].length * Main.MAP_DIMENSION, inputMap.length * Main.MAP_DIMENSION));

        this.map = new NodeMap(inputMap);
        if(Main.USE_ALGORITHM == 0) {
            this.path = this.map.aStarFindPath();
        } else if(Main.USE_ALGORITHM == 1) {

            this.path = this.map.findPathBFS();
        } else if(Main.USE_ALGORITHM == 2) {

            this.path = this.map.findPathDijkstra();
        }

    }

    //Renders the graphic map
    public void render(Graphics2D gr) {

        map.drawMap(gr, path);
        gr.setColor(Color.GRAY);
        for (int x = 0; x < getWidth(); x += Main.MAP_DIMENSION)
        {
            gr.drawLine(x, 0, x, getHeight());
        }
        for (int y = 0; y < getHeight(); y += Main.MAP_DIMENSION)
        {
            gr.drawLine(0, y, getWidth(), y);
        }

        gr.setColor(Color.RED);
    }
}
