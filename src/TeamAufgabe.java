import java.io.FileNotFoundException;
import java.time.Duration;
import java.time.Instant;

public class TeamAufgabe {

    public static void main(String[] args) {
	    LocationArrayList list = new LocationArrayList();
	    fillDatastructure(list);
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
            System.out.println("Initialization of "+lq.getClass().getName()+" needed "+Duration.between(start,end));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

