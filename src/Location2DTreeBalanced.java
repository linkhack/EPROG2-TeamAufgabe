import java.util.Arrays;
import java.util.Comparator;

/**
 * This is a version of the Location2DTree that performs balancing when the height of the left and right subtree differ too much (value defined in static constant BALANCE_THRESHOLD)
 * For other details, see Location2DTree documentation.
 */
public class Location2DTreeBalanced implements LocationQuery
{
    //Coordinate suppliers for each dimension, see documentation on Location2DNode
    private static final CoordSupplier X_COORD = Location2DTreeBalanced::getXCoord;
    private static final CoordSupplier Y_COORD = Location2DTreeBalanced::getYCoord;
    private static double getXCoord(double x, double y) {return x;}
    private static double getYCoord(double x, double y) {return y;}

    /**
     * One node of the tree. Each node has a coordinate supplier, which is a method reference, that determines how the plane is split.
     */
    class Location2DNode
    {
        private static final int BALANCE_THRESHOLD = 1;

        private Location location;
        private Location2DNode left,right;
        private CoordSupplier coordSupplier;
        private int height;
        private int size = 1;

        /**
         * this constructor is only used for the root node, other nodes are created when adding locations to the tree
         * @param location
         */
        public Location2DNode(Location location)
        {
            this(location, Y_COORD);
        }

        /**
         * Used when rebalancing tree
         * @param coordSupplier
         */
        private Location2DNode(CoordSupplier coordSupplier)
        {
            this.coordSupplier = coordSupplier;
        }

        /**
         * Used for adding nodes.
         * @param location
         * @param coordSupplier
         */
        private Location2DNode(Location location, CoordSupplier coordSupplier)
        {
            this(coordSupplier);
            this.location = location;
        }


        /**
         * Adds the given location to the tree, by performing a recursive search for the node.
         * @param newLoc
         */
        public void add(Location newLoc)
        {
            double coord = getCoord();
            double otherCoord = getCoord(newLoc);

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

           updateHeightAndSize();

            //see if subtree needs to be balanced
            if(Math.abs(getBalance())>BALANCE_THRESHOLD)
                rebalance();
        }

        public void rebalance()
        {
            //construct an array of locations of this subtree
            Location[] locations = new Location[size];
            fillArray(locations,0);
            rebalance(locations,0,size);
        }

        /**
         * Fills given array with the locations of this subtree inOrder.
         * firstIndex is the first index, which is filled by this method.
         * @param locations
         * @param firstIndex
         */
        private void fillArray(Location[] locations,int firstIndex)
        {
            if(left!=null)
                left.fillArray(locations,firstIndex);
            locations[firstIndex+getSize(left)] = location;
            if(right!=null)
                right.fillArray(locations,firstIndex+getSize(left)+1);
        }

        /**
         * reconstructs a balanced tree using a given range of the given location array
         * @param locations, array of locations
         * @param start, inclusive index
         * @param end, exclusive index
         */
        private void rebalance(Location[] locations, int start, int end)
        {
            //sort given range of the array, according to the coordinate the coordinate supplier returns
            Arrays.sort(locations,start,end, Comparator.comparingDouble(this::getCoord));

            //find median & set location of node
            int median = (end-start)/2+start;
            location = locations[median];

            //rebalance left tree
            //if there are no more nodes for the left tree, set left to null to clean up
            if(median-start==0)
                left=null;
            else
            {
                if(left==null)
                    left = new Location2DNode(invertSupplier());
                left.rebalance(locations,start,median);
            }

            //rebalance right tree
            if(end-(median+1)==0)
                right=null;
            else
            {
                if(right==null)
                    right = new Location2DNode(invertSupplier());
                right.rebalance(locations,median+1,end);
            }

            //update height and size
            updateHeightAndSize();
        }

        /**
         * increments the correct array entry (determined by location type), if this node is in the given circle.
         * also calls this method recursively for the subtrees, if the circle is inside of the subplanes
         * @param count
         * @param x
         * @param y
         * @param radius
         */
        public void locationsInArea(int[] count, double x, double y, double radius)
        {
            double coord = getCoord();
            double centerCoord = coordSupplier.get(x,y);

            boolean checkLeft = centerCoord-radius<=coord;
            boolean checkRight = centerCoord+radius>=coord;

            if(checkLeft)
            {
                if(checkRight)
                {
                    //if the circle is on both sides, check if this location is on the circle
                    if(location.getDistanceSquared(x,y)<=radius*radius)
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
        public int airportsWithNStations(Location2DTreeBalanced tree, int stationCount, double radius)
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

        public int getBalance()
        {
            return height(right)-height(left);
        }

        //called by recursive methods rebalance and add
        private void updateHeightAndSize()
        {
            height = Math.max(height(left),height(right))+1;
            size = getSize(left)+getSize(right)+1;
        }

        /**
         * @return the coordinate for this node
         */
        private double getCoord()
        {
            return getCoord(location);
        }

        /**
         * @param location
         * @return the coordinate of given location, that is important for this node (x or y)
         */
        private double getCoord(Location location)
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
    public int[] locationsInArea(double x, double y, double radius)
    {
        int[] result = new int[2];
        if(root!=null)
            root.locationsInArea(result,x,y,radius);
        return result;
    }

    @Override
    public int airportsWithNStations(int stationCount, double radius)
    {
        if(root!=null)
            return root.airportsWithNStations(this,stationCount,radius);
        return 0;
    }

    /**
     * Helper method that returns 0 if node==null, node.height otherwise
     * @param node
     * @return 0 if node==null, node.height otherwise
     */
    private static int height(Location2DNode node)
    {
        return node!=null?node.height:0;
    }

    private static int getSize(Location2DNode node)
    {
        return node!=null?node.size:0;
    }
}
