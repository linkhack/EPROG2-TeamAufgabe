public class Location {
    private String name;
    private float x;
    private float y;
    private LocationType locationType;

    public Location(String name, float x, float y, LocationType locationType) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.locationType = locationType;
    }

    public String getName() {
        return name;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public LocationType getLocationType() {
        return locationType;
    }

    /**
     * Distance suared!!
     * @param x
     * @param y
     * @return Distance squared from location to point (x,y)
     */
    public double getDistance(float x, float y) {
        return (this.x - x) * (this.x - x) + (this.y - y) * (this.y - y);
    }

    /**
     * Distance squared
     * @param loc location to be compared
     * @return Distance from this to loc squared.
     */
    public double getDistance(Location loc) {
        return getDistance(loc.x, loc.y);
    }

    public String toString() {
        return "{" + name + "," + x + "," + y + "," + locationType + "}";
    }
}
