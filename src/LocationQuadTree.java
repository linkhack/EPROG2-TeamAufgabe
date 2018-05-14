public class LocationQuadTree implements LocationQuery {
    private QuadTreeNode root;


    public LocationQuadTree(float topLeftx, float topLefty, float bottomRightx, float bottomRighty){
        root = new QuadTreeNode(topLeftx,topLefty,bottomRightx,bottomRighty);
    }
    /**
     * Adds given location.
     * Implementation in QuadTreeNode
     *
     * @param location Location to be added into QuadTree
     */
    @Override
    public void add(Location location) {
        if(root !=null){
            root.add(location);
        }else{
            root = new QuadTreeNode(location,-100000.0f,100000.0f,100000.0f,-100000.0f);
        }
    }

    /**
     * @param x
     * @param y
     * @param radius
     * @return an array with 2 elements, where the n-th element is the occurrence of locations with the type of the n-th enum value, which are inside the given circle
     */
    @Override
    public int[] locationsInArea(float x, float y, float radius) {
       int[] result = new int[2];
       if(root!=null) root.locationsInArea(result,x,y,radius);
       return result;
    }

    /**
     * @param stationCount
     * @param radius
     * @return the amount of airports, that have at least stationCount Train stations located in the given radius
     */
    @Override
    public int airportsWithNStations(int stationCount, float radius) {
        if (root!=null) return root.airportsWithNStations(this,stationCount,radius);
        return 0;
    }
}
