package gms.Point;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Alessandro Zonta on 15/05/2017.
 * PhD Situational Analytics
 * <p>
 * Computational Intelligence Group
 * Computer Science Department
 * Faculty of Sciences - VU University Amsterdam
 * <p>
 * a.zonta@vu.nl
 */
public class CoordTest {
    @Test
    public void setLat() throws Exception {
        Coord coord = new Coord();
        coord.setLat(123.4);
    }

    @Test
    public void getLat() throws Exception {
        Coord coord = new Coord();
        coord.setLat(123.4);
        assertNotNull(coord.getLat());
        assertEquals(new Double(123.4),coord.getLat());
    }

    @Test
    public void getLon() throws Exception {
        Coord coord = new Coord();
        coord.setLon(123.4);
        assertNotNull(coord.getLon());
        assertEquals(new Double(123.4),coord.getLon());
    }

    @Test
    public void setLon() throws Exception {
        Coord coord = new Coord();
        coord.setLon(123.45);
    }

    @Test
    public void equals() throws Exception {
        Coord coord = new Coord();
        coord.setLon(123.45);
        coord.setLat(123.45);

        assertTrue(coord.equals(coord));

        String obj = "ss";

        assertFalse(coord.equals(obj));

        Coord coordw = new Coord();
        coordw.setLon(123.45);
        coordw.setLat(123.45);
        assertTrue(coord.equals(coordw));

    }

}