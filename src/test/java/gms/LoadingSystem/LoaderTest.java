package gms.LoadingSystem;

import gms.GraphML.InfoEdge;
import gms.GraphML.InfoNode;
import gms.Point.Coord;
import org.junit.Test;

import java.util.List;
import java.util.Set;

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
public class LoaderTest {
    @Test
    public void findDistanceBetweenNodesConnected() throws Exception {
    }

    @Test
    public void findPointInEdge() throws Exception {
    }

    @Test
    public void computeminValueBoundaries() throws Exception {
        System loader = new Loader();
        loader.loadGraph();
    }

    @Test
    public void insideBoundaries() throws Exception {
        System loader = new Loader();


        Coord coordB = new Coord(52.0366015, 4.3027702);
        try{
            loader.insideBoundaries(coordB);
        }catch (Exception e){
            assertEquals("Compute Boundaries before check if the element is present",e.getMessage());
        }
        loader.loadGraph();
        assertTrue((Boolean) loader.insideBoundaries(coordB));
        assertFalse((Boolean) loader.insideBoundaries(new Coord(52.0366015, 2.3027702)));
    }

    @Test
    public void retAllEndEdges() throws Exception {
        System loader = new Loader();
        loader.loadGraph();
        Coord coordB = new Coord(52.0366015, 4.3027702);
        assertNotNull(loader.retAllEndEdges(loader.findNodes(coordB)));
    }

    @Test
    public void isEdgeCloser() throws Exception {
        System loader = new Loader();
        loader.loadGraph();

        Coord coordB = new Coord(52.0366015, 4.3027702);
        InfoEdge closerEdgeToNode = loader.findClosestEdge(coordB, new Coord(52.0366086, 4.3025929));

        Boolean test = loader.isEdgeCloser(closerEdgeToNode, loader.findNodes(coordB), coordB);
        assertTrue(test);
    }

    @Test
    public void findClosestEdge() throws Exception {
        System loader = new Loader();
        loader.loadGraph();

        Coord start = new Coord(52.033872, 4.315144);

        Coord point = new Coord(52.033654, 4.314779);
        InfoEdge edge = loader.findClosestEdge(point, start);
        assertNotNull(edge);


        Coord cTest = new Coord(52.0366086, 4.3025929);

        Coord coordB = new Coord(52.0366015, 4.3027702);
        InfoEdge edgeTest = loader.findClosestEdge(coordB, cTest);
        assertNotNull(edgeTest);

    }

    @Test
    public void findPathBetweenNodes() throws Exception {
        System loader = new Loader();
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
        System loader = new Loader();
        loader.loadGraph();

        Coord coord = new Coord();
        coord.setLat(52.0270636);
        coord.setLon(4.3002281);


        InfoNode node = loader.findNodes(coord);
        Set<InfoEdge> sets = loader.findEdges(node);
        assertNotNull(sets);

        Coord coordq = new Coord(52.0366086, 4.3025929);
        InfoNode nodes = loader.findNodes(coordq);
        Set<InfoEdge> setss = loader.findEdges(nodes);
        assertNotNull(setss);

    }

    @Test
    public void loadGraph() throws Exception {
        System loader = new Loader();
        loader.loadGraph();
    }

    @Test
    public void findNodes() throws Exception {
        System loader = new Loader();
        loader.loadGraph();

        Coord coord = new Coord();
        coord.setLat(52.0270636);
        coord.setLon(4.3002281);


        InfoNode node = loader.findNodes(coord);
        assertEquals(coord.getLon().toString(), node.retLon());
        assertEquals(coord.getLat().toString(), node.retLat());




        Coord coords = new Coord(52.0366868, 4.3031489);
        InfoNode node1 = loader.findNodes(coords);
        //System.out.println(node1.retLat() + " " + node1.retLon());

        //outside position
        Coord coordss = new Coord(52.035610, 4.364193);
        InfoNode node2 = loader.findNodes(coordss);
        java.lang.System.out.println(node2.retLat() + " " + node2.retLon());

        coordss = new Coord(52.002369, 4.327823);
        node2 = loader.findNodes(coordss);
        java.lang.System.out.println(node2.retLat() + " " + node2.retLon());

        coordss = new Coord(52.004358, 4.217489);
        node2 = loader.findNodes(coordss);
        java.lang.System.out.println(node2.retLat() + " " + node2.retLon());

        coordss = new Coord(52.099761, 4.270932);
        node2 = loader.findNodes(coordss);
        java.lang.System.out.println(node2.retLat() + " " + node2.retLon());
    }

}