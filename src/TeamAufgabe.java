import java.io.FileNotFoundException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

public class TeamAufgabe {

    public static void main(String[] args) {
        ArrayList<Location> locations = readLocationsFromFile();
        
	    LocationArrayList list = new LocationArrayList();
	    LocationQuadTree quadTree = new LocationQuadTree(-100000.0f,100000.0f,100000.0f,-100000.0f);
	    Location2DTree tree2D = new Location2DTree();

	    fillDatastructure(locations, list);
	    testLocationCount(list);
	    testAirportsWithNStations(list);

        fillDatastructure(locations,tree2D);
        testLocationCount(tree2D);
        testAirportsWithNStations(tree2D);

	    //QuadTree test
        fillDatastructure(locations,quadTree);
        testLocationCount(quadTree);
        testAirportsWithNStations(quadTree);
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

    public static void testLocationCount(LocationQuery lq)
    {
        Instant start = Instant.now();

        int[] result = lq.locationsInArea(0,0,5000);

        Instant end = Instant.now();
        System.out.println("locationsInArea of "+lq.getClass().getName()+" needed "+Duration.between(start,end).toMillis()+"ms");
        System.out.print("Result: ");


        for(LocationType type : LocationType.values())
        {
            System.out.print("("+type.name()+": "+result[type.ordinal()]+"), ");
        }
        System.out.println();
    }

    public static void testAirportsWithNStations(LocationQuery lq)
    {
        Instant start = Instant.now();

        int result = lq.airportsWithNStations(10, 50);

        Instant end = Instant.now();
        System.out.println("airportsWithNStations of "+lq.getClass().getName()+" needed "+Duration.between(start,end).toMillis()+"ms");
        System.out.println("Result: "+result);
    }
}

