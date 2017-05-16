package gms.Point;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
public class HaversineTest {
    @Test
    public void distance() throws Exception {
        Double results = Haversine.distance(47.6788206, -122.3271205, 47.6788206, -122.5271205);
        Double expected = 14.973190481586224;

        assertEquals(expected,results);

    }

}