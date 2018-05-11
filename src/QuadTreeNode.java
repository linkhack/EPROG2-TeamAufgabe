/**
 * Implements the nodes for the Quad tree. Ignores null values.
 * Saves up to 8 locations in one leaf, if more then 8 would be added into a leaf, the leaf gets split into
 * its children. If one box has a smaller dimension than 0.001, it wont get split anymore and value just saves more
 * Locations.
 */
public class QuadTreeNode {
    private LocationArrayList values = new LocationArrayList();

    //children
    private QuadTreeNode topLeft;
    private QuadTreeNode topRight;
    private QuadTreeNode bottomLeft;
    private QuadTreeNode bottomRight;

    //bounding box
    private final float topLeftx;
    private final float topLefty;
    private final float bottomRightx;
    private final float bottomRighty;

    //flag. Is true if node is leaf;
    private boolean leafFlag=true;

    public QuadTreeNode(float topLeftx, float topLefty, float bottomRightx, float bottomRighty) {
        this.topLeftx = topLeftx;
        this.topLefty = topLefty;
        this.bottomRightx = bottomRightx;
        this.bottomRighty = bottomRighty;
    }

    public QuadTreeNode(Location value, float topLeftx, float topLefty, float bottomRightx, float bottomRighty) {
        this(topLeftx, topLefty, bottomRightx, bottomRighty);
        values.add(value);
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
        //values is full
        if (leafFlag&&values.size()>=8&&Math.abs(topLeftx-bottomRightx)>0.001&&Math.abs(topLefty-bottomRighty)>0.001) {
            //split current node into 4 regions and pass value to correct subregion
            //x-split
            for(int i=0; i<values.size();++i){
                splitNode(values.get(i));
            }
            splitNode(loc);
            leafFlag=false;
            values=null;
            return;
        }
        //values still has empty place
        if(leafFlag){
            values.add(loc);
        }else{
            splitNode(loc);
        }
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
