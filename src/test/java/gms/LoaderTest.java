package gms;

import gms.GraphML.InfoEdge;
import gms.GraphML.InfoNode;
import gms.Point.Coord;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
    public void findPathBetweenNodes() throws Exception {
        Loader loader = new Loader();
        loader.loadGraph();

        Coord start = new Coord(52.047668,4.335758);
        Coord end = new Coord(52.028237, 4.312322);

        InfoNode startNode = loader.findNodes(start);
        InfoNode endNode = loader.findNodes(end);
        List<InfoNode> result = loader.findPathBetweenNodes(startNode,endNode);
        assertNotNull(result);


        start = new Coord(52.047668,-4.335758);
        end = new Coord(52.028237, 4.312322);

        startNode = loader.findNodes(start);
        endNode = loader.findNodes(end);
        try {
            result = loader.findPathBetweenNodes(startNode, endNode);
        }catch (Exception e){
            assertEquals("No path is found in the graph", e.getMessage());
        }
    }

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