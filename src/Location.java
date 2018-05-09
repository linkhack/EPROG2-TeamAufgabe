public class Location {
    String name;
    float x;
    float y;
    LocationType locationType;

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

}
