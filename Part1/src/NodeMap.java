

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class NodeMap {

    //Number of columns in X direction
    private int cols_x;

    //Number of columns in Y direction
    private int cols_y;

    // X and Y start and goal positions
    private int startX;
    private int startY;
    private int goalX;
    private int goalY;

    //2D array with the nodes of the map
    private Node[][] nodes;

    //Makes a map based on input.
   // generates the proper node type based on the input map

    public NodeMap(String[][] inputMap) {

        this.cols_x = inputMap[0].length;
        this.cols_y = inputMap.length;

        this.nodes = new Node[this.cols_y][this.cols_x];

        for(int y = 0; y < this.cols_y; y++) {

            for(int x = 0; x < this.cols_x; x++) {

                boolean isStart = inputMap[y][x].equals("A");
                boolean isGoal = inputMap[y][x].equals("B");
                Node newNode = new Node(x,y, inputMap[y][x].equals(".") || inputMap[y][x].equals("A") || inputMap[y][x].equals("B"));
                newNode.setStartNode(isStart);
                newNode.setGoalNode(isGoal);

                nodes[y][x] = newNode;

                if (isStart) {

                    this.startX = x;
                    this.startY = y;
                }
                if(isGoal) {

                    this.goalX = x;
                    this.goalY = y;
                }
            }
        }
    }


    //Draws a graphic map to visualize the shortest path
    public void drawMap(Graphics graphics, List<Node> path) {

        for(int y = 0; y < this.cols_y; y++) {

            for(int x = 0; x < this.cols_x; x++) {

                boolean isPath = false;
                if(!nodes[y][x].isWalkable()) {

                    graphics.setColor(Color.LIGHT_GRAY);
                    graphics.fillRect(x * 32, y*32, 32, 32);

                } else if(path != null && path.contains(new Node(x, y, true))) {

                    isPath = true;
                    if(this.nodes[y][x].isStartNode()) {

                        graphics.setColor(Color.RED);

                    } else if(this.nodes[y][x].isGoalNode()) {

                        graphics.setColor(Color.GREEN);

                    } else {

                        graphics.setColor(Color.white);

                    }

                }
                else {

                    graphics.setColor(Color.white);
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


                        } else if(isOpen != null){

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

    //Prints the map in the console
    public void printMap(List<Node> path) {

        for(int y = 0; y < this.cols_y; y++) {

            for(int x = 0; x < this.cols_x; x++) {


                if(!this.nodes[y][x].isWalkable()) {

                    System.out.print(" #");

                } else if(path.contains(new Node(x, y, true))) {

                    if(this.nodes[y][x].isStartNode()) {

                        System.out.print(" A");
                    } else if(this.nodes[y][x].isGoalNode()) {

                        System.out.print(" B");
                    } else {

                        System.out.print(" @");
                    }

                }
                else {

                    System.out.print(" *");
                }
            }
            System.out.println("\n");
        }
    }

    //Returns a node on a specific x and y index

    public Node getNode(int x, int y) {

        if(x >= 0 && x < this.cols_x && y >= 0 && y < this.cols_y) {

            return this.nodes[y][x];
        }
        return null;
    }

    //This is the implementation of the A* algorithm. The function takes the node list of this map, and returns a list of the nodes containing the shortest path

    public List<Node> aStarFindPath() {

        if((this.startX == this.goalX) && (this.startY == this.goalY)) {

            return new LinkedList<Node>();
        }

        List<Node> openList = new LinkedList<Node>();
        List<Node> closedList = new LinkedList<Node>();

        openList.add(this.nodes[this.startY][this.startX]);
        this.nodes[this.startY][this.startX].setIsOpen(true);

        while(true) {

            Node current = this.getLowestFNodeInList(openList);
            openList.remove(current);
            closedList.add(current);
            this.nodes[current.getY()][current.getX()].setIsOpen(false);

            if(current.getX() == this.goalX && current.getY() == this.goalY) {

                return this.getTraversedPath(this.nodes[this.startY][this.startX], current);
            }

            List<Node> adjacentNodes = this.getAdjacentNodes(current, closedList);

            for(Node adjacentNode: adjacentNodes) {

                if(!openList.contains(adjacentNode)) {

                    adjacentNode.setParent(current);
                    adjacentNode.setHvalue(this.nodes[this.goalY][this.goalY]);
                    adjacentNode.setGvalue(current);
                    openList.add(adjacentNode);
                    this.nodes[adjacentNode.getY()][adjacentNode.getX()].setIsOpen(true);

                } else if(adjacentNode.getGvalue() > adjacentNode.calcGValue(current)) {

                    adjacentNode.setParent(current);
                    adjacentNode.setGvalue(current);
                }
            }

            if(openList.isEmpty()) {

                return new LinkedList<Node>();
            }
        }
    }

    //This is the implementation of the BFS algorithm. The function takes the node list of this map, and returns a list of the nodes containing the shortest path

    public List<Node> findPathBFS() {


        if((this.startX == this.goalX) && (this.startY == this.goalY)) {

            return new LinkedList<Node>();
        }

        ArrayList<Node> openList = new ArrayList<Node>();
        ArrayList<Node> closedList = new ArrayList<Node>();

        openList.add(this.nodes[this.startY][this.startX]);
        this.nodes[this.startY][this.startX].setIsOpen(true);

        while(true) {

            Node current = openList.get(0);
            openList.remove(current);
            closedList.add(current);
            this.nodes[current.getY()][current.getX()].setIsOpen(false);

            if(current.getX() == this.goalX && current.getY() == this.goalY) {

                return this.getTraversedPath(this.nodes[this.startY][this.startX], current);
            }

            List<Node> adjacentNodes = this.getAdjacentNodes(current, closedList);

            for(Node adjacentNode: adjacentNodes) {

                if(!openList.contains(adjacentNode)) {

                    adjacentNode.setParent(current);
                    adjacentNode.setHvalue(this.nodes[this.goalY][this.goalY]);
                    adjacentNode.setGvalue(current);
                    openList.add(adjacentNode);
                    this.nodes[adjacentNode.getY()][adjacentNode.getX()].setIsOpen(true);

                } else if(adjacentNode.getGvalue() > adjacentNode.calcGValue(current)) {

                    adjacentNode.setParent(current);
                    adjacentNode.setGvalue(current);
                }
            }

            if(openList.isEmpty()) {

                return new LinkedList<Node>();
            }
        }

    }

    //This is the implementation of the Dijkstra algorithm. The function takes the node list of this map, and returns a list of the nodes containing the shortest path


    public List<Node> findPathDijkstra() {


        if((this.startX == this.goalX) && (this.startY == this.goalY)) {

            return new LinkedList<Node>();
        }

        ArrayList<Node> openList = new ArrayList<Node>();
        ArrayList<Node> closedList = new ArrayList<Node>();

        openList.add(this.nodes[this.startY][this.startX]);
        this.nodes[this.startY][this.startX].setIsOpen(true);

        while(true) {

            Node current = this.getLowestGNodeInList(openList);
            openList.remove(current);
            closedList.add(current);
            this.nodes[current.getY()][current.getX()].setIsOpen(false);

            if(current.getX() == this.goalX && current.getY() == this.goalY) {

                return this.getTraversedPath(this.nodes[this.startY][this.startX], current);
            }

            List<Node> adjacentNodes = this.getAdjacentNodes(current, closedList);

            for(Node adjacentNode: adjacentNodes) {

                if(!openList.contains(adjacentNode)) {

                    adjacentNode.setParent(current);
                    adjacentNode.setHvalue(this.nodes[this.goalY][this.goalY]);
                    adjacentNode.setGvalue(current);
                    openList.add(adjacentNode);
                    this.nodes[adjacentNode.getY()][adjacentNode.getX()].setIsOpen(true);

                } else if(adjacentNode.getGvalue() > adjacentNode.calcGValue(current)) {

                    adjacentNode.setParent(current);
                    adjacentNode.setGvalue(current);
                }
            }

            if(openList.isEmpty()) {

                return new LinkedList<Node>();
            }
        }

    }

    //When the A* algorithm finds the goal node, the algorihms stops. This function gets called. The traverse starts at the "youngest" child node in the path, and traverses all the way to the "oldes" Parent
    //Returns the path when done traversing

    private List<Node> getTraversedPath(Node startNode, Node goalNode) {

        LinkedList<Node> path = new LinkedList<Node>();

        boolean done = false;
        Node node = goalNode;
        while(!done) {

            path.addFirst(node);
            node = node.getParent();

            if(node.equals(startNode)) {

                path.add(startNode);
                done = true;
            }

        }

        return path;
    }

    //Returns the node with the lowest F value in a given list. This list will be the "openList"
    private Node getLowestFNodeInList(List<Node> nodeList) {

        Node lowestFNode = nodeList.get(0);
        for(Node node: nodeList) {

            if(node.getFvalue() < lowestFNode.getFvalue()) {

                lowestFNode = node;
            }
        }
        return lowestFNode;
    }

    private Node getLowestGNodeInList(List<Node> nodeList) {

        Node lowestGNode = nodeList.get(0);
        for(Node node: nodeList) {

            if(node.getGvalue() < lowestGNode.getGvalue()) {

                lowestGNode = node;
            }
        }
        return lowestGNode;

    }

    //Returns all the adjacent nodes if not in the closedList
    private List<Node> getAdjacentNodes(Node node, List<Node> closedList) {

        List<Node> adjacentNodes = new LinkedList<Node>();

        if(node.getX() > 0) {

            this.addAdjacentNodeIfExists(adjacentNodes, node, -1, 0, closedList);
        }
        if(node.getX() < this.cols_x) {

            this.addAdjacentNodeIfExists(adjacentNodes, node, 1, 0, closedList);
        }
        if(node.getY() > 0) {
            this.addAdjacentNodeIfExists(adjacentNodes, node, 0, -1, closedList);
        }
        if(node.getY() < this.cols_y) {
            this.addAdjacentNodeIfExists(adjacentNodes, node, 0, 1, closedList);
        }
        return adjacentNodes;

    }

    //Help fuction to get adjacent nodes
    private void addAdjacentNodeIfExists(List<Node> adjacentNodes,Node node, int xDir, int yDir, List<Node> closedList) {

        Node adjacentNode = this.getNode(node.getX() + xDir, node.getY() + yDir);
        if(adjacentNode != null && adjacentNode.isWalkable() && !closedList.contains(adjacentNode)) {

            adjacentNodes.add(adjacentNode);
        }
    }
}