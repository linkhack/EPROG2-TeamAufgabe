public interface LocationQuery
{
    /**
     * @param x
     * @param y
     * @param radius
     * @return an array with 2 elements, where the first element is the airport count and the second element is the Train Station count, which are inside the given circle
     */
    int[] locationsInArea(float x, float y, float radius);

    /**
     * @param stationCount
     * @param radius
     * @return the amount of airports, that have at least stationCount Train stations located in the given radius
     */
    int airportsWithNStations(int stationCount, float radius);
}
