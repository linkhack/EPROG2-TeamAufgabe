public class QuadTreeNode {
    Location value;

    //children
    QuadTreeNode topLeft;
    QuadTreeNode topRight;
    QuadTreeNode bottomLeft;
    QuadTreeNode bottomRight;

    //bounding box
    float topLeftx;
    float topLefty;
    float bottomRightx;
    float bottomRighty;

    //flag. Is true if node is leaf;
    boolean leafFlag;

    public QuadTreeNode(float topLeftx, float topLefty, float bottomRightx, float bottomRighty) {
        this.topLeftx = topLeftx;
        this.topLefty = topLefty;
        this.bottomRightx = bottomRightx;
        this.bottomRighty = bottomRighty;
    }

    public QuadTreeNode(Location value, float topLeftx, float topLefty, float bottomRightx, float bottomRighty) {
        this(topLeftx, topLefty, bottomRightx, bottomRighty);
        this.value = value;
        leafFlag = true;
    }

    public Location getValue() {
        return value;
    }

    public boolean add(Location loc) {
        if (loc == null) {
            return false;
        }
        if (!inBoundry(loc)) {
            return false;
        }
        if (leafFlag) {
            //split current node into 4 regions and pass value to correct subregion
            //x-split
            splitNode(value);
            leafFlag = false;
        }
        splitNode(loc);
        return true;
    }

    public boolean inBoundry(Location loc) {
        return (topLeftx <= loc.getX()
                && topLefty <= loc.getY()
                && bottomRightx >= loc.getX()
                && bottomRighty >= loc.getY());
    }

    private void splitNode(Location loc) {
        float xSplit = (topLeftx + bottomRightx) / 2;
        float ySplit = (topLefty + bottomRighty) / 2;
        if (xSplit >= loc.getX()) { //left
            if (ySplit <= loc.getY()) { //top
                if (topLeft == null) {
                    topLeft = new QuadTreeNode(loc, topLeftx, topLefty, xSplit, ySplit);
                } else {
                    topLeft.add(loc);
                }
            } else {//bottom
                if (bottomRight == null) {
                    bottomLeft = new QuadTreeNode(loc, topLeftx, ySplit, xSplit, bottomRighty);
                } else {
                    bottomLeft.add(loc);
                }
            }
        } else { //right
            if (ySplit <= loc.getY()) { //top
                if (topRight == null) {
                    topRight = new QuadTreeNode(loc, xSplit, topLefty, bottomRightx, ySplit);
                } else {
                    topRight.add(loc);
                }
            } else {//bottom
                if (bottomRight == null) {
                    bottomRight = new QuadTreeNode(loc, xSplit, ySplit, bottomRightx, bottomRighty);
                } else {
                    bottomRight.add(loc);
                }
            }
        }
    }
}
