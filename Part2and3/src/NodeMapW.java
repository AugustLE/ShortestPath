import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class NodeMapW {

    //Number of columns in X direction
    private int cols_x;

    //Number of columns in Y direction
    private int cols_y;

    // X and Y start and goal positions
    private int startX;
    private int startY;
    private int goalX;
    private int goalY;

    private HashMap<String, Integer> nodeCosts;
    private HashMap<String, Color> nodeColors;

    //2D array with the nodes of the map
    private NodeW[][] nodes;

    //Makes a map based on input.
    // generates the proper node type based on the input map

    public NodeMapW(String[][] inputMap) {

        this.nodeCosts = new HashMap<String, Integer>();
        this.nodeColors = new HashMap<String, Color>();
        nodeCosts.put("w", 100);
        nodeCosts.put("m", 50);
        nodeCosts.put("f", 10);
        nodeCosts.put("g", 5);
        nodeCosts.put("r", 1);
        nodeCosts.put("A", 0);
        nodeCosts.put("B", 0);

        nodeColors.put("w", new Color(30,144,255,200));
        nodeColors.put("m", new Color(125, 120, 126, 236));
        nodeColors.put("f", new Color(0,128,0,200));
        nodeColors.put("g", new Color(127,255,0,200));
        nodeColors.put("r", new Color(205,133,63,200));


        this.cols_x = inputMap[0].length;
        this.cols_y = inputMap.length;

        this.nodes = new NodeW[this.cols_y][this.cols_x];

        for(int y = 0; y < this.cols_y; y++) {

            for(int x = 0; x < this.cols_x; x++) {


                NodeW newNodeW = this.generateNode(x, y, inputMap);
                nodes[y][x] = newNodeW;

            }
        }
    }

    //Help function to generate a node. The function generates the proper node type based on the input map.
    private NodeW generateNode(int x, int y, String[][] inputMap) {

        String nType = inputMap[y][x];
        //System.out.print(nType);

        boolean isWalkable = nType.equals("w") || nType.equals("m") || nType.equals("f") || nType.equals("g")
                || nType.equals("r") || nType.equals("A") || nType.equals("B");
        boolean isStart = inputMap[y][x].equals("A");
        boolean isGoal = inputMap[y][x].equals("B");


        NodeW newNode = new NodeW(x, y , isWalkable, this.nodeCosts.get(nType), nType);

        newNode.setStartNode(isStart);
        newNode.setGoalNode(isGoal);

        if (isStart) {

            this.startX = x;
            this.startY = y;
        }
        if(isGoal) {

            this.goalX = x;
            this.goalY = y;
        }

        return newNode;

    }

    //Draws a graphic map to visualize the shortest path.
    public void drawMap(Graphics graphics, List<NodeW> path) {

        for(int y = 0; y < this.cols_y; y++) {

            for(int x = 0; x < this.cols_x; x++) {

                NodeW currentNode = this.nodes[y][x];
                boolean isPath = false;

                if(!nodes[y][x].isWalkable()) {

                    graphics.setColor(Color.BLACK);

                } else if(path != null && path.contains(new NodeW(x, y, currentNode.isWalkable(), currentNode.getMovementCost(), currentNode.getNodeType()))) {

                    isPath = true;
                    if(this.nodes[y][x].isStartNode()) {

                        graphics.setColor(Color.RED);
                    } else if(this.nodes[y][x].isGoalNode()) {

                        graphics.setColor(Color.GREEN);
                    } else {

                        graphics.setColor(this.nodeColors.get(this.nodes[y][x].getNodeType()));

                    }

                }
                else {

                    graphics.setColor(this.nodeColors.get(this.nodes[y][x].getNodeType()));
                }


                graphics.fillRect(x * Main.MAP_DIMENSION, y * Main.MAP_DIMENSION, Main.MAP_DIMENSION, Main.MAP_DIMENSION);

                if(isPath) {
                    graphics.setColor(Color.black);
                    graphics.fillRect(x * Main.MAP_DIMENSION, y * Main.MAP_DIMENSION, Main.PATH_DIMENSION, Main.PATH_DIMENSION);
                } else {

                    Boolean isOpen = nodes[y][x].isOpen();
                    if(Main.DRAW_OPEN_CLOSED) {

                        if(isOpen != null && isOpen) {

                            graphics.setColor(Color.BLACK);
                            //graphics.drawString("*", x * Main.MAP_DIMENSION - 10, y * Main.MAP_DIMENSION - 10);

                            graphics.drawLine(x * Main.MAP_DIMENSION , y * Main.MAP_DIMENSION + 5, x * Main.MAP_DIMENSION + 5, y * Main.MAP_DIMENSION);
                            graphics.drawLine(x * Main.MAP_DIMENSION, y * Main.MAP_DIMENSION , x * Main.MAP_DIMENSION + 5, y * Main.MAP_DIMENSION +5);
                            graphics.drawLine(x * Main.MAP_DIMENSION + 2, y * Main.MAP_DIMENSION , x * Main.MAP_DIMENSION +2, y * Main.MAP_DIMENSION +5);


                        } else if(isOpen != null) {

                            graphics.setColor(Color.black);
                            //graphics.fillRect(x * Main.MAP_DIMENSION, y * Main.MAP_DIMENSION, Main.OPEN_DIMENSION, Main.OPEN_DIMENSION);
                            graphics.drawLine(x * Main.MAP_DIMENSION , y * Main.MAP_DIMENSION + 10, x * Main.MAP_DIMENSION + 10, y * Main.MAP_DIMENSION);
                            graphics.drawLine(x * Main.MAP_DIMENSION, y * Main.MAP_DIMENSION , x * Main.MAP_DIMENSION + 9, y * Main.MAP_DIMENSION + 9);

                        }
                    }
                }

            }
        }
    }

    //Returns a node on a specific x and y index

    public NodeW getNodeW(int x, int y) {

        if(x >= 0 && x < this.cols_x && y >= 0 && y < this.cols_y) {

            return this.nodes[y][x];
        }
        return null;
    }

    //This is the implementation of the A* algorithm. The function takes the node list of this map, and returns a list of the nodes containing the shortest path

    public List<NodeW> findPathAstar() {

        if((this.startX == this.goalX) && (this.startY == this.goalY)) {

            return new LinkedList<NodeW>();
        }

        List<NodeW> openList = new LinkedList<NodeW>();
        List<NodeW> closedList = new LinkedList<NodeW>();

        openList.add(this.nodes[this.startY][this.startX]);
        this.nodes[this.startY][this.startX].setIsOpen(true);

        while(true) {

            NodeW current = this.getLowestFNodeWInList(openList);
            openList.remove(current);
            closedList.add(current);
            this.nodes[current.getY()][current.getX()].setIsOpen(false);

            if(current.getX() == this.goalX && current.getY() == this.goalY) {

                return this.getTraversedPath(this.nodes[this.startY][this.startX], current);
            }

            List<NodeW> adjacentNodeWs = this.getAdjacentNodeWs(current, closedList);

            for(NodeW adjacentNodeW: adjacentNodeWs) {

                if(!openList.contains(adjacentNodeW)) {

                    adjacentNodeW.setParent(current);
                    adjacentNodeW.setHvalue(this.nodes[this.goalY][this.goalY]);
                    adjacentNodeW.setGvalue(current);
                    openList.add(adjacentNodeW);
                    this.nodes[adjacentNodeW.getY()][adjacentNodeW.getX()].setIsOpen(true);

                } else if(adjacentNodeW.getGvalue() > adjacentNodeW.calcGValue(current)) {

                    adjacentNodeW.setParent(current);
                    adjacentNodeW.setGvalue(current);
                }
            }

            if(openList.isEmpty()) {

                return new LinkedList<NodeW>();
            }
        }
    }

    public List<NodeW> findPathBFS() {


        if((this.startX == this.goalX) && (this.startY == this.goalY)) {

            return new LinkedList<NodeW>();
        }

        ArrayList<NodeW> openList = new ArrayList<NodeW>();
        ArrayList<NodeW> closedList = new ArrayList<NodeW>();

        openList.add(this.nodes[this.startY][this.startX]);
        this.nodes[this.startY][this.startX].setIsOpen(true);

        while(true) {

            NodeW current = openList.get(0);
            openList.remove(current);
            closedList.add(current);
            this.nodes[current.getY()][current.getX()].setIsOpen(false);

            if(current.getX() == this.goalX && current.getY() == this.goalY) {

                return this.getTraversedPath(this.nodes[this.startY][this.startX], current);
            }

            List<NodeW> adjacentNodeWs = this.getAdjacentNodeWs(current, closedList);

            for(NodeW adjacentNodeW: adjacentNodeWs) {

                if(!openList.contains(adjacentNodeW)) {

                    adjacentNodeW.setParent(current);
                    adjacentNodeW.setHvalue(this.nodes[this.goalY][this.goalY]);
                    adjacentNodeW.setGvalue(current);
                    openList.add(adjacentNodeW);
                    this.nodes[adjacentNodeW.getY()][adjacentNodeW.getX()].setIsOpen(true);

                } else if(adjacentNodeW.getGvalue() > adjacentNodeW.calcGValue(current)) {

                    adjacentNodeW.setParent(current);
                    adjacentNodeW.setGvalue(current);
                }
            }

            if(openList.isEmpty()) {

                return new LinkedList<NodeW>();
            }
        }

    }

    public List<NodeW> findPathDijkstra() {


        if((this.startX == this.goalX) && (this.startY == this.goalY)) {

            return new LinkedList<NodeW>();
        }

        ArrayList<NodeW> openList = new ArrayList<NodeW>();
        ArrayList<NodeW> closedList = new ArrayList<NodeW>();

        openList.add(this.nodes[this.startY][this.startX]);
        this.nodes[this.startY][this.startX].setIsOpen(true);

        while(true) {

            NodeW current = this.getLowestGNodeInList(openList);
            openList.remove(current);
            closedList.add(current);
            this.nodes[current.getY()][current.getX()].setIsOpen(false);

            if(current.getX() == this.goalX && current.getY() == this.goalY) {

                return this.getTraversedPath(this.nodes[this.startY][this.startX], current);
            }

            List<NodeW> adjacentNodeWs = this.getAdjacentNodeWs(current, closedList);

            for(NodeW adjacentNodeW: adjacentNodeWs) {

                if(!openList.contains(adjacentNodeW)) {

                    adjacentNodeW.setParent(current);
                    adjacentNodeW.setHvalue(this.nodes[this.goalY][this.goalY]);
                    adjacentNodeW.setGvalue(current);
                    openList.add(adjacentNodeW);
                    this.nodes[adjacentNodeW.getY()][adjacentNodeW.getX()].setIsOpen(true);

                } else if(adjacentNodeW.getGvalue() > adjacentNodeW.calcGValue(current)) {

                    adjacentNodeW.setParent(current);
                    adjacentNodeW.setGvalue(current);
                }
            }

            if(openList.isEmpty()) {

                return new LinkedList<NodeW>();
            }
        }

    }

    //When the A* algorithm finds the goal node, the algorihms stops. This function gets called. The traverse starts at the "youngest" child node in the path, and traverses all the way to the "oldes" Parent
    //Returns the path when done traversing

    private List<NodeW> getTraversedPath(NodeW startNodeW, NodeW goalNodeW) {

        LinkedList<NodeW> path = new LinkedList<NodeW>();

        boolean done = false;
        NodeW node = goalNodeW;
        while(!done) {

            path.addFirst(node);
            node = (NodeW)node.getParent();

            if(node.equals(startNodeW)) {

                path.add(startNodeW);
                done = true;
            }

        }

        return path;
    }

    private NodeW getLowestGNodeInList(List<NodeW> nodeList) {

        NodeW lowestGNodeW = nodeList.get(0);
        for(NodeW node: nodeList) {

            if(node.getGvalue() < lowestGNodeW.getGvalue()) {

                lowestGNodeW = node;
            }
        }
        return lowestGNodeW;

    }

    //Returns the node with the lowest F value in a given list. This list will be the "openList"
    private NodeW getLowestFNodeWInList(List<NodeW> nodeList) {

        NodeW lowestFNodeW = nodeList.get(0);
        for(NodeW node: nodeList) {

            if(node.getFvalue() < lowestFNodeW.getFvalue()) {

                lowestFNodeW = node;
            }
        }
        return lowestFNodeW;
    }

    //Returns all the adjacent nodes if not in the closedList
    private List<NodeW> getAdjacentNodeWs(NodeW node, List<NodeW> closedList) {

        List<NodeW> adjacentNodeWs = new LinkedList<NodeW>();

        if(node.getX() > 0) {

            this.addAdjacentNodeWIfExists(adjacentNodeWs, node, -1, 0, closedList);
        }
        if(node.getX() < this.cols_x) {

            this.addAdjacentNodeWIfExists(adjacentNodeWs, node, 1, 0, closedList);
        }
        if(node.getY() > 0) {
            this.addAdjacentNodeWIfExists(adjacentNodeWs, node, 0, -1, closedList);
        }
        if(node.getY() < this.cols_y) {
            this.addAdjacentNodeWIfExists(adjacentNodeWs, node, 0, 1, closedList);
        }
        return adjacentNodeWs;

    }

    //Help fuction to get adjacent nodes
    private void addAdjacentNodeWIfExists(List<NodeW> adjacentNodeWs, NodeW node, int xDir, int yDir, List<NodeW> closedList) {

        NodeW adjacentNodeW = this.getNodeW(node.getX() + xDir, node.getY() + yDir);
        if(adjacentNodeW != null && adjacentNodeW.isWalkable() && !closedList.contains(adjacentNodeW)) {

            adjacentNodeWs.add(adjacentNodeW);
        }
    }
}