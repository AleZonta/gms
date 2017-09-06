package gms.KdTree;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Alessandro Zonta on 01/09/2017.
 * PhD Situational Analytics
 * <p>
 * Computational Intelligence Group
 * Computer Science Department
 * Faculty of Sciences - VU University Amsterdam
 * <p>
 * a.zonta@vu.nl
 */
public class XYZPointTest {
    @Test
    public void getX() throws Exception {
        KdTree.XYZPoint p = new KdTree.XYZPoint(5d,8d);
        assertEquals(new Double(5),p.getX());
    }

    @Test
    public void getY() throws Exception {
        KdTree.XYZPoint p = new KdTree.XYZPoint(5d,8d);
        assertEquals(new Double(8),p.getY());
    }

    @Test
    public void euclideanDistance() throws Exception {
        KdTree.XYZPoint p = new KdTree.XYZPoint(5d,8d);
        KdTree.XYZPoint p1 = new KdTree.XYZPoint(55d,88d);
        assertEquals(94.339811,p.euclideanDistance(p1),0.00001);
    }

    @Test
    public void equals() throws Exception {
        KdTree.XYZPoint p = new KdTree.XYZPoint(5d,8d);
        KdTree.XYZPoint p1 = new KdTree.XYZPoint(5d,8d);
        assertTrue(p.equals(p1));
    }

    @Test
    public void compareTo() throws Exception {
        KdTree.XYZPoint p = new KdTree.XYZPoint(5d,8d);
        KdTree.XYZPoint p1 = new KdTree.XYZPoint(5d,8d);
        assertEquals(0, p.compareTo(p1));

        KdTree.XYZPoint p2 = new KdTree.XYZPoint(6d,8d);
        assertEquals(-1, p.compareTo(p2));

        KdTree.XYZPoint p3 = new KdTree.XYZPoint(5d,5d);
        assertEquals(1, p.compareTo(p3));
    }

}