/**
 * LocationArrayList stores Locations in an array list in the same order in which they are added
 * If the array space runs out, the array is replaced by an array with twice the size of the original one
 * LocationArrayList ignores null-values
 */
public class LocationArrayList implements LocationQuery
{
    //this is the next free index in the array
    private int nextFree = 0;
    private Location[] locations = new Location[1<<3];

    public void add(Location location)
    {
        //ignore if null
        if(location==null)
            return;

        //expand if necessary
        if(nextFree==locations.length)
            expand();

        locations[nextFree++] = location;
    }

    private void expand()
    {
        //create array, twice as big
        Location[] newLocs = new Location[locations.length<<1];

        //copy elements
        for(int x = 0;x<locations.length;++x)
            newLocs[x] = locations[x];

        //assign new array to attribute
        locations = newLocs;
    }

    @Override
    public int[] locationsInArea(float x, float y, float radius) {
        int[] result = new int[2];
        for(int i = 0;i<nextFree;++i)
        {
            Location l = locations[i];
            if(l.getDistance(x,y)<=radius*radius)
                result[l.getLocationType().ordinal()]++;
        }
        return result;
    }

    @Override
    public int airportsWithNStations(int stationCount, float radius)
    {
        int amount = 0;
        for(int i = 0;i<nextFree;++i)
        {
            Location l = locations[i];
            //if l is an airport, call locationsInArea to see if there are enough train stations around
            if(l.getLocationType()==LocationType.AIRPORT && locationsInArea(l.getX(),l.getY(),radius)[LocationType.TRAINSTATION.ordinal()]>=stationCount)
                amount++;
        }
        return amount;
    }

    /**
     * Checks the number of Locations in List
     * @return number of Locations in ArrayList
     */
    public int size(){
        return nextFree;
    }

    /**
     * To access single elements in List
     * @param i index of location
     * @return Location at index i if 0<=i<nextFree else null
     */
    public Location get(int i){
        if(i<0||i>=nextFree){
            return null;
        }
        return locations[i];
    }
}
