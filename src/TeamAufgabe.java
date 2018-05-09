public class TeamAufgabe {

    public static void main(String[] args) {
	    System.out.println("Hallo");
	    FileParser locationFile = new FileParser("./data/junctions.csv");
	    System.out.println(locationFile.nextLocation());
    }
}

