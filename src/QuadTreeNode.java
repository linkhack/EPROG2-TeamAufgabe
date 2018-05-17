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
    private final double topLeftx;
    private final double topLefty;
    private final double bottomRightx;
    private final double bottomRighty;

    //flag. Is true if node is leaf;
    private boolean leafFlag=true;

    public QuadTreeNode(double topLeftx, double topLefty, double bottomRightx, double bottomRighty) {
        this.topLeftx = topLeftx;
        this.topLefty = topLefty;
        this.bottomRightx = bottomRightx;
        this.bottomRighty = bottomRighty;
    }

    public QuadTreeNode(Location value, double topLeftx, double topLefty, double bottomRightx, double bottomRighty) {
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
        double xSplit = (topLeftx + bottomRightx) / 2;
        double ySplit = (topLefty + bottomRighty) / 2;
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

    public void locationsInArea(int[] result, double x, double y, double radius) {
        if(!leafFlag) {
            double xSplit = (topLeftx + bottomRightx) / 2;
            double ySplit = (topLefty + bottomRighty) / 2;
            boolean left = (x - radius <= xSplit);
            boolean right = (x + radius > xSplit);
            boolean top = (y + radius >= ySplit);
            boolean bottom = (y - radius) < ySplit;
            if(top&&left&&(topLeft!=null)) topLeft.locationsInArea(result, x, y, radius);
            if(top&&right&&(topRight!=null)) topRight.locationsInArea(result, x, y, radius);
            if(bottom&&left&&(bottomLeft!=null)) bottomLeft.locationsInArea(result, x, y, radius);
            if(bottom&&right&&(bottomRight!=null)) bottomRight.locationsInArea(result, x, y, radius);
        }else {
            int[] leafResult = values.locationsInArea(x, y, radius);
            result[0] += leafResult[0];
            result[1] += leafResult[1];
        }
    }

    public int airportsWithNStations(LocationQuadTree tree, int stationCount, double radius) {
        int sum = 0;
        if(leafFlag){
            for(int i = 0; i<values.size(); ++i){
                Location l = values.get(i);
                if(l.getLocationType()==LocationType.AIRPORT
                        &&tree.locationsInArea(l.getX(),l.getY(),radius)[LocationType.TRAINSTATION.ordinal()]>=stationCount){
                    ++sum;
                }
            }
        }else{
            if(topLeft!=null) sum+=topLeft.airportsWithNStations(tree, stationCount, radius);
            if(topRight!=null) sum+= topRight.airportsWithNStations(tree,stationCount,radius);
            if(bottomLeft!=null) sum+= bottomLeft.airportsWithNStations(tree, stationCount, radius);
            if(bottomRight!=null) sum+= bottomRight.airportsWithNStations(tree, stationCount, radius);
        }
        return sum;
    }
}
