public class LocationQuadTree implements LocationQuery {
    QuadTreeNode root;


    public LocationQuadTree(float topLeftx, float topLefty, float bottomRightx, float bottomRighty){
        root = new QuadTreeNode(topLeftx,topLefty,bottomRightx,bottomRighty);
    }
    /**
     * Adds given location
     *
     * @param location
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
        return new int[0];
    }

    /**
     * @param stationCount
     * @param radius
     * @return the amount of airports, that have at least stationCount Train stations located in the given radius
     */
    @Override
    public int airportsWithNStations(int stationCount, float radius) {
        return 0;
    }
}
