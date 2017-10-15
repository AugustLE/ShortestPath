import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Main {

    private static final int WIDTH = 4340;
    private static final int HEIGHT = 3080;

    static int MAP_DIMENSION = 25;
    static int PATH_DIMENSION = 8;
    public static int OPEN_DIMENSION = 5;

    //Turn on/off drawing of open and closed nodes
    static boolean DRAW_OPEN_CLOSED = false;

    // 0 = A*, 1 = BFS, 2 = Dijkstra
    static int USE_ALGORITHM = 0;
    private static int USE_MAP = 2;

    private static ArrayList<String[][]> maps;

    public static void initMaps() throws IOException {

        maps = new ArrayList<String[][]>();
        String[][] map2_1 = createInputMap("boards/board-2-1.txt");
        String[][] map2_2 = createInputMap("boards/board-2-2.txt");
        String[][] map2_3 = createInputMap("boards/board-2-3.txt");
        String[][] map2_4 = createInputMap("boards/board-2-4.txt");
        String[][] map2_5 = createInputMap("boards/board-2-5.txt");

        maps.add(map2_1);
        maps.add(map2_2);
        maps.add(map2_3);
        maps.add(map2_4);
        maps.add(map2_5);
    }

    public static void main(String[] args) throws IOException {

        initMaps();

        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setBackground(Color.BLACK);

        GraphicsMap graphicMap = new GraphicsMap(maps.get(USE_MAP - 1));

        String[] names = {"A* Search", "BFS Search", "Dijkstra Search"};
        String name = names[USE_ALGORITHM];
        JFrame frame = new JFrame(name);
        frame.setDefaultCloseOperation(3);
        frame.setContentPane(graphicMap);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        graphicMap.render(g);
        Graphics gg = graphicMap.getGraphics();
        gg.drawImage(image, 0, 0, null);
        gg.dispose();

    }

    public static String[][] createInputMap(String path) throws IOException {

        int x = 0;
        int y = 0;
        ArrayList<ArrayList> inputMap_ArrayList = new ArrayList<ArrayList>();


        for (String line : Files.readAllLines(Paths.get(path))) {

            //System.out.print(line);
            //System.out.print("\n");
            ArrayList<String> xList = new ArrayList<String>();
            for (String part : line.split("")) {

                xList.add(part);
                //System.out.print(part);
                //System.out.print("\n");
                x++;
            }
            // ...
            inputMap_ArrayList.add(xList);

            y++;
            x = 0;

        }
        String[][] inputMap_1 = new String[inputMap_ArrayList.size()][inputMap_ArrayList.get(0).size()];

        for(y = 0; y < inputMap_ArrayList.size(); y++) {

            for(x = 0; x < inputMap_ArrayList.get(y).size(); x++) {

                String sign = (String)inputMap_ArrayList.get(y).get(x);
                inputMap_1[y][x] = sign;

            }
        }

        /*for(String[] linee: inputMap_1) {

            for(String str: linee) {

                System.out.print(str);
            }
            System.out.print("\n");
        }*/

        return inputMap_1;
    }


}