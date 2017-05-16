package gms;

import gms.GraphML.InfoEdge;
import gms.GraphML.InfoNode;
import gms.Point.Coord;
import org.junit.Test;

import java.util.Set;

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
public class LoaderTest {
    @Test
    public void findEdges() throws Exception {
        Loader loader = new Loader();
        loader.loadGraph();

        Coord coord = new Coord();
        coord.setLat(52.0270636);
        coord.setLon(4.3002281);


        InfoNode node = loader.findNodes(coord);
        Set<InfoEdge> sets = loader.findEdges(node);
    }

    @Test
    public void loadGraph() throws Exception {
        Loader loader = new Loader();
        loader.loadGraph();
    }

    @Test
    public void findNodes() throws Exception {
        Loader loader = new Loader();
        loader.loadGraph();

        Coord coord = new Coord();
        coord.setLat(52.0270636);
        coord.setLon(4.3002281);


        InfoNode node = loader.findNodes(coord);
        assertEquals(coord.getLon().toString(), node.retLon());
        assertEquals(coord.getLat().toString(), node.retLat());

    }

}