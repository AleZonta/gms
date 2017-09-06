package gms.KdTree;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

/**
 * Created by Alessandro Zonta on 06/09/2017.
 * PhD Situational Analytics
 * <p>
 * Computational Intelligence Group
 * Computer Science Department
 * Faculty of Sciences - VU University Amsterdam
 * <p>
 * a.zonta@vu.nl
 */
public class KdTreeTest {
    @Test
    public void add() throws Exception {
        List<KdTree.XYZPoint> points = new ArrayList<KdTree.XYZPoint>();
        KdTree.XYZPoint p1 = new KdTree.XYZPoint(2d, 3d);
        points.add(p1);
        KdTree.XYZPoint p2 = new KdTree.XYZPoint(5d, 4d);
        points.add(p2);
        KdTree<KdTree.XYZPoint> kdTree = new KdTree<>(points);
    }

    @Test
    public void contains() throws Exception {
        List<KdTree.XYZPoint> points = new ArrayList<KdTree.XYZPoint>();
        KdTree.XYZPoint p1 = new KdTree.XYZPoint(2d, 3d);
        points.add(p1);
        KdTree.XYZPoint p2 = new KdTree.XYZPoint(5d, 4d);
        points.add(p2);
        KdTree<KdTree.XYZPoint> kdTree = new KdTree<>(points);
        assertTrue(kdTree.contains(p1));
        assertTrue(kdTree.contains(p2));
    }

    @Test
    public void remove() throws Exception {
        List<KdTree.XYZPoint> points = new ArrayList<KdTree.XYZPoint>();
        KdTree.XYZPoint p1 = new KdTree.XYZPoint(2d, 3d);
        points.add(p1);
        KdTree.XYZPoint p2 = new KdTree.XYZPoint(5d, 4d);
        points.add(p2);
        KdTree<KdTree.XYZPoint> kdTree = new KdTree<>(points);
        kdTree.remove(p2);
        assertTrue(kdTree.contains(p1));
        assertFalse(kdTree.contains(p2));
    }

    @Test
    public void nearestNeighbourSearch() throws Exception {
        List<KdTree.XYZPoint> points = new ArrayList<KdTree.XYZPoint>();
        KdTree.XYZPoint p1 = new KdTree.XYZPoint(2d, 3d);
        points.add(p1);
        KdTree.XYZPoint p2 = new KdTree.XYZPoint(5d, 4d);
        points.add(p2);
        KdTree.XYZPoint p3 = new KdTree.XYZPoint(9d, 6d);
        points.add(p3);
        KdTree.XYZPoint p4 = new KdTree.XYZPoint(4d, 7d);
        points.add(p4);
        KdTree.XYZPoint p5 = new KdTree.XYZPoint(8d, 1d);
        points.add(p5);
        KdTree.XYZPoint p6 = new KdTree.XYZPoint(7d, 2d);
        points.add(p6);
        KdTree<KdTree.XYZPoint> kdTree = new KdTree<>(points);

        Collection<KdTree.XYZPoint> result = kdTree.nearestNeighbourSearch(1, p3);
        assertTrue("K-D Tree query error. query=(k=1, p=(9, 6)) returned="+result, result.contains(p3));

        KdTree.XYZPoint search = new KdTree.XYZPoint(1d, 4d);
        result = kdTree.nearestNeighbourSearch(4, search);
        assertTrue("K-D Tree query error. query=(k=4, p=(1, 4)) returned="+result, (result.contains(p1) &&
                result.contains(p2) &&
                result.contains(p4) &&
                result.contains(p6))
        );
    }

    @Test
    public void iterator() throws Exception {
        List<KdTree.XYZPoint> points = new ArrayList<KdTree.XYZPoint>();
        KdTree.XYZPoint p1 = new KdTree.XYZPoint(2d, 3d);
        points.add(p1);
        KdTree.XYZPoint p2 = new KdTree.XYZPoint(5d, 4d);
        points.add(p2);
        KdTree.XYZPoint p3 = new KdTree.XYZPoint(9d, 6d);
        points.add(p3);
        KdTree.XYZPoint p4 = new KdTree.XYZPoint(4d, 7d);
        points.add(p4);
        KdTree.XYZPoint p5 = new KdTree.XYZPoint(8d, 1d);
        points.add(p5);
        KdTree.XYZPoint p6 = new KdTree.XYZPoint(7d, 2d);
        KdTree<KdTree.XYZPoint> kdTree = new KdTree<>(points);

        for (final KdTree.XYZPoint p : kdTree)
            assertTrue(kdTree.contains(p));
    }

    @Test
    public void reverse_iterator() throws Exception {
    }

}