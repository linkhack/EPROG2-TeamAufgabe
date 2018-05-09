import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileParser {
    File file;
    Scanner scanner;

    public FileParser(String path){
        file = new File(path);
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        scanner.useDelimiter(";");
    }

    public Location nextLocation(){
        if(scanner.hasNext()) {
            String name = scanner.next();
            float x = scanner.nextFloat();
            float y = scanner.nextFloat();
            LocationType type = (scanner.next().equalsIgnoreCase("airport") ? LocationType.AIRPORT : LocationType.TRAINSTATION);
            return new Location(name, x, y, type);
        }else {
            return null;
        }
    }
}
