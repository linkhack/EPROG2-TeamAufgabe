import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileParser {
    private Scanner scanner;

    /**
     * Creates a parser object reading File  path
     * @param path Filename "path/filename.extension"
     */
    public FileParser(String path) throws FileNotFoundException{
        File file = new File(path);
        scanner = new Scanner(file);
        scanner.useDelimiter(";|\\n");
    }

    /**
     * next Location
     * @return Location containing the next Location, null if end of file is reached
     */
    public Location nextLocation(){
        if(scanner.hasNext()) {
            String name = scanner.next();
            double x = scanner.nextDouble();
            double y = scanner.nextDouble();
            LocationType type = (scanner.next().equalsIgnoreCase("airport") ? LocationType.AIRPORT : LocationType.TRAINSTATION);
            return new Location(name, x, y, type);
        }

        return null;
    }
}
