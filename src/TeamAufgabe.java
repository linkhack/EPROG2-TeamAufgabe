import java.io.FileNotFoundException;
import java.time.Duration;
import java.time.Instant;

public class TeamAufgabe {

    public static void main(String[] args) {
	    LocationArrayList list = new LocationArrayList();
	    fillDatastructure(list);
	    testLocationCount(list);
	    testAirportsWithNStations(list);
    }

    public static void fillDatastructure(LocationQuery lq)
    {
        try {
            FileParser locationFile = new FileParser("./data/junctions.csv");

            Instant start = Instant.now();

            Location location;
            while((location=locationFile.nextLocation())!=null)
                lq.add(location);

            Instant end = Instant.now();
            System.out.println("Initialization of "+lq.getClass().getName()+" needed "+Duration.between(start,end).toMillis()+"ms");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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

        int result = lq.airportsWithNStations(10, 500);

        Instant end = Instant.now();
        System.out.println("airportsWithNStations of "+lq.getClass().getName()+" needed "+Duration.between(start,end).toMillis()+"ms");
        System.out.println("Result: "+result);
    }
}

