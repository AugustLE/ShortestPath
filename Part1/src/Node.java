public class Node {


    private int MOVEMENT_COST = 10;
    private boolean is_startNode;
    private boolean is_goalNode;

    private int pos_x;

    private int pos_y;

    private boolean is_walkable;

    private Node parent_node;

    private int g_value;

    private int h_value;

    private Boolean isOpen;

    public Boolean isOpen() {
        return isOpen;
    }

    public void setIsOpen(boolean open) {
        isOpen = open;
    }

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


    public void setGvalue(Node parent_node) {

        this.g_value = (parent_node.getGvalue() + MOVEMENT_COST);
    }

    public int calcGValue(Node parent_node) {

        return (parent_node.getGvalue() + MOVEMENT_COST);
    }

    public void setHvalue(Node goal_node) {

        this.h_value = (Math.abs(this.getX() - goal_node.getX()) + Math.abs(this.getY() - goal_node.getY())) * MOVEMENT_COST;
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