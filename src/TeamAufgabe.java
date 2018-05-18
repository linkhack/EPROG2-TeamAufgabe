import java.io.FileNotFoundException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class TeamAufgabe
{
    private static final int LOCATION_COUNT_QUERIES = 1000;
    private static final int AIRPORT_QUERIES = 8e;
    private static final int AIRPORT_QUERIES_EXCLUDING_LIST = 200;
    private static final double MIN_RADIUS = 10;
    private static final double MAX_RADIUS = 500;
    private static final int MIN_STATIONS = 1;
    private static final int MAX_STATIONS = 50;
    private static final String TIME_UNIT = "ms";

    //the main method needs about 1 min to execute completely
    public static void main(String[] args) {
        ArrayList<Location> locations = readLocationsFromFile();
        System.out.println(locations.size());

        //initialize data structures and add locations
        LocationArrayList list = new LocationArrayList();
	    LocationQuadTree quadTree = new LocationQuadTree(-100000.0f,100000.0f,100000.0f,-100000.0f);
	    Location2DTree tree2D = new Location2DTree();

	    fillDatastructure(locations, list);
        fillDatastructure(locations,tree2D);
        fillDatastructure(locations,quadTree);

        System.out.println();

        //test locationsInArea for all data structures
        testLocationCount(false, generateQueries(LOCATION_COUNT_QUERIES,locations),list, quadTree,tree2D);
        //test airportsWithNStations with a small query count (list needs quite long) for all data structures
        testLocationCount(true,generateQueries(AIRPORT_QUERIES,locations),list,quadTree,tree2D);
        //test airportsWithNStations of the more intelligent data structures with a higher query count
        testLocationCount(true,generateQueries(AIRPORT_QUERIES_EXCLUDING_LIST,locations),quadTree,tree2D);
    }

    public static ArrayList<Location> readLocationsFromFile()
    {
        ArrayList<Location> list = new ArrayList<>();
        try {
            FileParser locationFile = new FileParser("./data/junctions.csv");

            Location location;
            while((location=locationFile.nextLocation())!=null)
                list.add(location);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static void fillDatastructure(ArrayList<Location> locations, LocationQuery lq)
    {
        Instant start = Instant.now();

        for(Location location : locations)
            lq.add(location);

        Instant end = Instant.now();
        System.out.println("Initialization of "+lq.getClass().getName()+" needed "+Duration.between(start,end).toMillis()+"ms");
    }

    /**
     * Tests either LocationQuery::locationsInArea or LocationQuery::airportsWithNStations by calling it multiple times with different parameters on all given data structure.
     * After calling, some time statistics are printed out for each data structure.
     * The results for the queries are stored and compared between the different data structures and errors are printed out if they differ.
     * @param testAirportQuery, if true, LocationQuery::airportsWithNStations is tested, LocationQuery::locationsInArea otherwise
     * @param queries, parameters for the method calls
     * @param lqs, an array of data structures the queries are executed on
     */
    public static void testLocationCount(boolean testAirportQuery, QueryParams[] queries, LocationQuery... lqs)
    {
        //these values remain constant for the rest of the method
        int queryCount = queries.length;
        String methodName = testAirportQuery?"airportsWithNStations":"locationsInArea";

        //arrays to store time information and results for each query for each data structure
        Duration[][] allDurations = new Duration[lqs.length][queryCount];
        double[] avgDurationsMs = new double[lqs.length];
        int[][][] allResults = new int[lqs.length][queryCount][LocationType.values().length];

        System.out.println("=====Calling "+methodName+" "+queryCount+" times with random parameters on "+Arrays.toString(lqs)+"=====");

        for(int lqIndex = 0;lqIndex<lqs.length;++lqIndex)
        {
            //obtain data structure that is going to be tested
            LocationQuery lq = lqs[lqIndex];
            System.out.println("Testing "+lq.getClass().getName()+"...");

            //obtain arrays where time and results are stored for lq
            Duration[] durations = allDurations[lqIndex];
            int[][] results = allResults[lqIndex];
            //iterate over the parameters and perform a query for each set of parameters
            for(int x = 0;x<queryCount;++x)
            {
                QueryParams params = queries[x];

                Instant start;
                if(!testAirportQuery)
                {
                    start = Instant.now();
                    results[x] = lq.locationsInArea(params.x, params.y, params.radius);
                }
                else
                {
                    start = Instant.now();
                    results[x][0] = lq.airportsWithNStations(params.stationCount,params.radius);
                }

                durations[x] = Duration.between(start, Instant.now());

                //add duration
                avgDurationsMs[lqIndex]+=durations[x].toMillis();
            }

            //Generate resulting messages
            System.out.println("Finished, showing times:");
            System.out.println("Total time: "+avgDurationsMs[lqIndex]+TIME_UNIT);
            //avgDurationsMs[lqIndex] currently holds the total time of all queries
            //divide through query count
            avgDurationsMs[lqIndex]/=queryCount;
            System.out.println("Average time: "+avgDurationsMs[lqIndex]+TIME_UNIT);
            System.out.println("Min time: "+ Arrays.stream(durations).min(Duration::compareTo).get().toMillis()+TIME_UNIT);
            System.out.println("Max time: "+ Arrays.stream(durations).max(Duration::compareTo).get().toMillis()+TIME_UNIT);
            //the line below is taken and adapted from the following stackoverflow answer: https://stackoverflow.com/a/49215170
            System.out.println("Median time: "+Arrays.stream(durations).sorted().skip((queryCount-1)/2).limit(2-queryCount%2).mapToLong(Duration::toMillis).average().orElse(Double.NaN)+TIME_UNIT);
            System.out.println();
        }

        //compare Results to see if they are equal for all data structures
        for(int x = 0;x<queryCount;++x)
        {
            boolean showError = false;
            for(int lqIndex = 0;lqIndex<lqs.length-1;++lqIndex)
            {
                if(!Arrays.deepEquals(allResults[lqIndex],allResults[lqIndex+1]))
                {
                    showError = true;
                    break;
                }
            }

            if(showError)
            {
                System.out.println(methodName+" results differ with following parameters: "+queries[x]);
                System.out.println("Results for each data structure:");
                for(int lqIndex = 0;lqIndex<lqs.length;++lqIndex)
                    System.out.println(lqs[lqIndex].getClass().getName()+": "+Arrays.toString(allResults[lqIndex][x]));
            }
        }

        System.out.println("=====Test of "+methodName+" has finished=====");
    }

    /**
     * Constructs and returns an array of QueryParams objects with random values.
     * The attribute values are calculated the following way:
     * params.x & params.y: take a random location and add/subtract up to MIN_RADIUS from the coordinates, this makes sure that there is always at least one location nearby
     * all other values are generated using hard coded (static variables) values as upper and lower bound
     * @param count, amount of generated queries
     * @param locations, a list of all locations
     * @return an array of QueryParams objects with the attributes set to random values
     */
    public static QueryParams[] generateQueries(int count, ArrayList<Location> locations)
    {
        QueryParams[] result = new QueryParams[count];

        for(int x = 0;x<count;++x)
        {
            QueryParams params = new QueryParams();
            Location randomLoc = locations.get(ThreadLocalRandom.current().nextInt(locations.size()));
            params.x = randomLoc.getX()+ThreadLocalRandom.current().nextDouble(0,MIN_RADIUS)*2-MIN_RADIUS;
            params.y = randomLoc.getY()+ThreadLocalRandom.current().nextDouble(0,MIN_RADIUS)*2-MIN_RADIUS;
            params.radius = ThreadLocalRandom.current().nextDouble(MIN_RADIUS,MAX_RADIUS);
            params.stationCount = ThreadLocalRandom.current().nextInt(MIN_STATIONS, MAX_STATIONS);

            result[x] = params;
        }

        return result;
    }
}

/**
 * This is a simple class to hold all parameters for the test calls, performed in the main method.
 * This holds the parameters for the locationsInArea method as well as the airportsWithNStations method, since I'm currently not into generics and method references to make the test method more generic.
 * But for just two different test method, this seems fine
 */
class QueryParams
{
    public double x;
    public double y;
    public double radius;
    public int stationCount;

    //Generated
    @Override
    public String toString() {
        return "LocationCountQueryParams{" +
                "x=" + x +
                ", y=" + y +
                ", radius=" + radius +
                ", stationCount=" + stationCount +
                '}';
    }
}
