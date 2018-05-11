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

    public double getDistance(float x, float y){
        return Math.sqrt(Math.pow(this.x-x,2)+Math.pow(this.y-y,2));
    }
    public double getDistance(Location loc){
        return getDistance(loc.x,loc.y);
    }

    public String toString(){
        return "{"+name+","+x+","+y+","+locationType+"}";
    }
}
