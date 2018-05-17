public interface LocationQuery
{
    /**
     * Adds given location
     * @param location
     */
    void add(Location location);

    /**
     * @param x
     * @param y
     * @param radius
     * @return an array with 2 elements, where the n-th element is the occurrence of locations with the type of the n-th enum value, which are inside the given circle
     */
    int[] locationsInArea(double x, double y, double radius);

    /**
     * @param stationCount
     * @param radius
     * @return the amount of airports, that have at least stationCount Train stations located in the given radius
     */
    int airportsWithNStations(int stationCount, double radius);
}
