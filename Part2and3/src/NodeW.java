public class NodeW extends Node {

    private String nodeType;
    private int movementCost;
    private Boolean isOpen;


    public NodeW(int x, int y, boolean is_walkable, int movement_cost, String nodeType) {

        super(x,y,is_walkable);
        this.pos_x = x;
        this.pos_y = y;
        this.is_walkable = is_walkable;
        this.movementCost = movement_cost;
        this.nodeType = nodeType;
    }


    public void setHvalue(Node goal_node) {

        this.h_value = (Math.abs(this.getX() - goal_node.getX()) + Math.abs(this.getY() - goal_node.getY()));
    }
    public void setGvalue(Node parent_node) {

        this.g_value = (parent_node.getGvalue() + this.movementCost);
    }

    public int calcGValue(Node parent_node) {

        return (parent_node.getGvalue() + this.movementCost);
    }


    public Node getParent() {

        return this.parent_node;
    }

    public void setParent(NodeW parent_node) {

        this.parent_node = parent_node;
    }

    public int getMovementCost() {

        return this.movementCost;
    }

    public String getNodeType() {

        return this.nodeType;
    }

    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public Boolean isOpen() {

        return this.isOpen;
    }

    @Override
    public boolean equals(Object object) {

        if(object == null) {
            return false;
        }
        if(!(object instanceof NodeW)) {
            return false;
        }
        if(object == this) {
            return true;
        }

        NodeW node = (NodeW)object;
        return node.getX() == this.getX() && node.getY() == this.getY() && node.isWalkable() == this.isWalkable() && node.getNodeType().equals(this.getNodeType());

    }
}