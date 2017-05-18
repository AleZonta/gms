package gms.Point;

/**
 * Created by Alessandro Zonta on 15/05/2017.
 * PhD Situational Analytics
 * <p>
 * Computational Intelligence Group
 * Computer Science Department
 * Faculty of Sciences - VU University Amsterdam
 * <p>
 * a.zonta@vu.nl
 *
 * This class represent one coordinate
 */
public class Coord {
    private Double latitude;
    private Double longitude;

    /**
     * Constructor with zero parameters.
     * Puts everything at zero
     */
    public Coord(){
        this.longitude = null;
        this.latitude = null;
    }

    /**
     * Constructor with two parameters (the coordinates of a point)
     * @param longitude Double value representing longitude
     * @param latitude Double value representing latitude
     */
    public Coord(Double longitude, Double latitude){
        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * setter for latitude
     * @param latitude in double
     */
    public void setLat(Double latitude) {
        this.latitude = latitude;
    }

    /**
     * getter for latitude
     * @return return the latitude in double
     */
    public Double getLat() {
        return this.latitude;
    }

    /**
     * getter for longitude
     * @return return the longitude in double
     */
    public Double getLon() {
        return this.longitude;
    }

    /**
     * setter for longitude
     * @param longitude in double
     */
    public void setLon(Double longitude) {
        this.longitude = longitude;
    }

    /**
     * override method equals
     * @param o object to compare with this Point
     * @return if the two points are the same or not
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Coord)) {
            return false;
        }

        Coord point = (Coord) o;

        return point.latitude.equals(this.latitude) && point.longitude.equals(this.longitude);
    }


    /**
     * Euclidean Distance between two Coordinates
     * @param other second coordinate used to compute the distance
     * @return Double value of the distance
     */
    public Double distance(Coord other){
        Double deltaX = this.latitude - other.getLat();
        Double deltaY = this.longitude - other.getLon();
        return Math.sqrt(deltaX*deltaX + deltaY*deltaY);
    }

}
