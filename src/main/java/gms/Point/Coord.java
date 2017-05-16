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

}
