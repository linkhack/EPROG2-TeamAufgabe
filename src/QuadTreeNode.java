/**
 * Implements the nodes for the Quad tree. Ignores null values.
 * Saves up to 8 locations in one leaf, if more then 8 would be added into a leaf, the leaf gets split into
 * its children. If one box has a smaller dimension than 0.001, it wont get split anymore and value just saves more
 * Locations.
 */
public class QuadTreeNode {
    private Location value;

    //children
    private QuadTreeNode topLeft;
    private QuadTreeNode topRight;
    private QuadTreeNode bottomLeft;
    private QuadTreeNode bottomRight;

    //bounding box
    private float topLeftx;
    private float topLefty;
    private float bottomRightx;
    private float bottomRighty;

    //flag. Is true if node is leaf;
    private boolean leafFlag;

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

    /**
     * Adds given location
     * @param loc
     */
    public void add(Location loc) {
        if (loc == null) {
            return;
        }
        if (!inBoundry(loc)) {
            return;
        }
        if (leafFlag) {
            //split current node into 4 regions and pass value to correct subregion
            //x-split
            splitNode(value);
            leafFlag = false;
        }
        splitNode(loc);
        return;
    }

    /**
     * Check if location lies within boundry
     * @param loc location to be tested
     * @return true if location lies within boundry of quad.
     */
    private boolean inBoundry(Location loc) {
        return (topLeftx <= loc.getX()
                && topLefty >= loc.getY()
                && bottomRightx >= loc.getX()
                && bottomRighty <= loc.getY());
    }

    private void splitNode(Location loc) {
        if(Math.abs(topLeftx-bottomRightx)<0.001||Math.abs(topLefty-bottomRighty)<0.001){
            System.out.println("Too near");
            System.out.println(value);
            System.out.println(loc);
            return;
        }
        float xSplit = (topLeftx + bottomRightx) / 2;
        float ySplit = (topLefty + bottomRighty) / 2;
        boolean isTop = ySplit <= loc.getY();
        if (xSplit >= loc.getX()) { //left
            if (isTop) { //top
                if (topLeft == null) {
                    topLeft = new QuadTreeNode(loc, topLeftx, topLefty, xSplit, ySplit);
                } else {
                    topLeft.add(loc);
                }
            } else {//bottom
                if (bottomLeft == null) {
                    bottomLeft = new QuadTreeNode(loc, topLeftx, ySplit, xSplit, bottomRighty);
                } else {
                    bottomLeft.add(loc);
                }
            }
        } else { //right
            if (isTop) { //top
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
