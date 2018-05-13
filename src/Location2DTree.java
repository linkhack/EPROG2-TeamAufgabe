/**
 * This class is a binary tree, where each node splits the euclidean plane into two subplanes along an axis-aligned line.
 * Whether this seperating line is vertical or horizontal, depends on the depth of the node. An even depth results in a horizontal line, and vertical otherwise.
 * Each subtree contains all locations of one of the two subplanes.
 * Equal elements are always put into the left subtree.
 *
 * Currently, no balancing measures are implemented
 */
public class Location2DTree implements LocationQuery
{
    //Coordinate suppliers for each dimension, see documentation on Location2DNode
    private static final CoordSupplier X_COORD = Location2DTree::getXCoord;
    private static final CoordSupplier Y_COORD = Location2DTree::getYCoord;
    private static float getXCoord(float x, float y) {return x;}
    private static float getYCoord(float x, float y) {return y;}

    /**
     * One node of the tree. Each node has a coordinate supplier, which is a method reference, that determines how the plane is split.
     */
    class Location2DNode
    {
        private Location location;
        private Location2DNode left,right;
        private CoordSupplier coordSupplier;

        /**
         * this constructor is only used for the root node, other nodes are created when adding locations to the tree
         * @param location
         */
        public Location2DNode(Location location)
        {
            this(location, Y_COORD);
        }

        /**
         * Used for adding nodes.
         * @param location
         * @param coordSupplier
         */
        private Location2DNode(Location location, CoordSupplier coordSupplier)
        {
            this.coordSupplier = coordSupplier;
            this.location = location;
        }


        /**
         * Adds the given location to the tree, by performing a recursive search for the node.
         * @param newLoc
         */
        public void add(Location newLoc)
        {
            float coord = getCoord();
            float otherCoord = getCoord(newLoc);

            if(otherCoord<=coord)
            {
                if(left==null)
                    left = new Location2DNode(newLoc, invertSupplier());
                else
                    left.add(newLoc);
            }
            else
            {
                if(right==null)
                    right = new Location2DNode(newLoc,invertSupplier());
                else
                    right.add(newLoc);
            }
        }

        /**
         * increments the correct array entry (determined by location type), if this node is in the given circle.
         * also calls this method recursively for the subtrees, if the circle is inside of the subplanes
         * @param count
         * @param x
         * @param y
         * @param radius
         */
        public void locationsInArea(int[] count, float x, float y, float radius)
        {
            float coord = getCoord();
            float centerCoord = coordSupplier.get(x,y);

            boolean checkLeft = centerCoord-radius<=coord;
            boolean checkRight = centerCoord+radius>=coord;

            if(checkLeft)
            {
                if(checkRight)
                {
                    //if the circle is on both sides, check if this location is on the circle
                    if(location.getDistance(x,y)<=radius)
                        count[location.getLocationType().ordinal()]++;
                }

                if(left!=null)
                    left.locationsInArea(count,x,y,radius);
            }

            if(checkRight && right!=null)
                right.locationsInArea(count,x,y,radius);
        }

        /**
         * @param tree, used for calling locationsInArea
         * @param stationCount
         * @param radius
         * @return he count of all airports with stationCount Trainstations in the given radius in this (sub-)tree
         */
        public int airportsWithNStations(Location2DTree tree, int stationCount, float radius)
        {
            int sum = 0;

            if(left!=null)
                sum+=left.airportsWithNStations(tree,stationCount,radius);

            if(location.getLocationType()==LocationType.AIRPORT && tree.locationsInArea(location.getX(),location.getY(),radius)[LocationType.TRAINSTATION.ordinal()]>=stationCount)
                ++sum;

            if(right!=null)
                sum+=right.airportsWithNStations(tree,stationCount,radius);

            return sum;
        }

        /**
         * @return the coordinate for this node
         */
        private float getCoord()
        {
            return getCoord(location);
        }

        /**
         * @param location
         * @return the coordinate of given location, that is important for this node (x or y)
         */
        private float getCoord(Location location)
        {
            return coordSupplier.get(location.getX(),location.getY());
        }

        /**
         * Used when constructing a new node as a subtree
         * @return the coordinate supplier, that supplies the other coordinate than the supplier of this node
         */
        private CoordSupplier invertSupplier()
        {
            return coordSupplier==X_COORD?Y_COORD:X_COORD;
        }
    }

    private Location2DNode root;

    @Override
    public void add(Location location)
    {
        if(root==null)
            root = new Location2DNode(location);
        else
            root.add(location);
    }

    @Override
    public int[] locationsInArea(float x, float y, float radius)
    {
        int[] result = new int[2];
        if(root!=null)
            root.locationsInArea(result,x,y,radius);
        return result;
    }

    @Override
    public int airportsWithNStations(int stationCount, float radius)
    {
        if(root!=null)
            return root.airportsWithNStations(this,stationCount,radius);
        return 0;
    }
}

interface CoordSupplier
{
    /**
     * @param x
     * @param y
     * @return either x or y
     */
    float get(float x, float y);
}
