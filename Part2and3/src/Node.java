public class Node {


    protected boolean is_startNode;
    protected boolean is_goalNode;

    protected int pos_x;


    protected int pos_y;


    protected boolean is_walkable;


    protected Node parent_node;


    protected int g_value;


    protected int h_value;


    public Node(int x, int y, boolean is_walkable) {

        this.pos_x = x;
        this.pos_y = y;
        this.is_walkable = is_walkable;
    }

    public boolean isStartNode() {

        return this.is_startNode;
    }

    public boolean isGoalNode() {

        return this.is_goalNode;
    }

    public void setStartNode(boolean isStart) {

        this.is_startNode = isStart;
    }

    public void setGoalNode(boolean isGoal) {

        this.is_goalNode = isGoal;
    }


    public int getX() {

        return this.pos_x;
    }

    public int getY() {

        return this.pos_y;
    }

    public void setY(int y) {

        this.pos_y = y;
    }

    public boolean isWalkable() {

        return this.is_walkable;
    }

    public void setWalkable(boolean is_walkable) {

        this.is_walkable = is_walkable;
    }

    public Node getParent() {

        return this.parent_node;
    }


    public void setParent(Node parent_node) {

        this.parent_node = parent_node;
    }

    public int getFvalue() {

        return this.g_value + this.h_value;
    }

    public int getGvalue() {

        return this.g_value;
    }

    public int getHvalue() {

        return this.h_value;
    }

    @Override
    public boolean equals(Object object) {

        if(object == null) {
            return false;
        }
        if(!(object instanceof Node)) {
            return false;
        }
        if(object == this) {
            return true;
        }

        Node node = (Node)object;
        return node.getX() == this.getX() && node.getY() == this.getY() && node.isWalkable() == this.isWalkable();

    }

}
