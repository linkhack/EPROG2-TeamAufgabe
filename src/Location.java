public class Location {
    private String name;
    private double x;
    private double y;
    private LocationType locationType;

    public Location(String name, double x, double y, LocationType locationType) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.locationType = locationType;
    }

    public String getName() {
        return name;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public LocationType getLocationType() {
        return locationType;
    }

    /**
     * Distance suared!!
     *
     * @param x
     * @param y
     * @return Distance squared from location to point (x,y)
     */
    public double getDistanceSquared(double x, double y) {
        return (this.x - x) * (this.x - x) + (this.y - y) * (this.y - y);
    }

    /**
     * Distance squared
     *
     * @param loc location to be compared
     * @return Distance from this to loc squared.
     */
    public double getDistanceSquared(Location loc) {
        return getDistanceSquared(loc.x, loc.y);
    }

    public String toString() {
        return "{" + name + "," + x + "," + y + "," + locationType + "}";
    }
}
