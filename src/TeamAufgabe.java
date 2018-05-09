import java.io.FileNotFoundException;

public class TeamAufgabe {

    public static void main(String[] args) {
	    System.out.println("Hallo");
        try {
            FileParser locationFile = new FileParser("./data/junctions.csv");
            System.out.println(locationFile.nextLocation());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

