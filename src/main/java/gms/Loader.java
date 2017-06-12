package gms;


import gms.Config.ReadConfig;
import gms.GraphML.GraphMLImporter;
import gms.GraphML.InfoEdge;
import gms.GraphML.InfoNode;
import gms.Point.Coord;
import gms.Point.Haversine;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DirectedPseudograph;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static gms.GraphML.StringContinousFactory.FACTORY;

/**
 * Created by Alessandro Zonta on 11/05/2017.
 * PhD Situational Analytics
 * <p>
 * Computational Intelligence Group
 * Computer Science Department
 * Faculty of Sciences - VU University Amsterdam
 * <p>
 * a.zonta@vu.nl
 *
 * This class loads the graph of all the path from file.
 */
public class Loader {
    private final ReadConfig conf; //configuration object containing location where to read the graph
    private static final Logger logger = Logger.getLogger(Loader.class.getName()); //logger for this class
    private final Graph<InfoNode, InfoEdge> graph; //graph
    private DijkstraShortestPath<InfoNode, InfoEdge> enginePath;


    /**
     * Constructor with zero parameter
     * The config file is read.
     * @throws Exception If the reading of the config file encounter in problems an exception is raised
     */
    public Loader() throws Exception {
        this.conf = new ReadConfig();
        this.conf.readFile();
        this.graph = new DirectedPseudograph<>(InfoEdge.class); //allows loops and multi-edges
        this.enginePath = null;
    }


    /**
     * Method that loads the graph
     * It is creating a Direct graph with loops and multi-edges.
     * @throws Exception the function raises an exception if there are problems with the config file
     */
    public void loadGraph() throws Exception {
        logger.log(Level.INFO, "Loading Graph routes...");
        //create File object for read the graphML
        Path path = Paths.get(this.conf.getPath());
        File file = path.toFile();

        //create reader
        GraphMLImporter<InfoNode, InfoEdge> result = GraphMLImporter.createFromFile(file);
        //Now I have to put the graph into a graph object
        Map<String, InfoNode> map = new HashMap<>();
        result.generateGraph(this.graph, new InfoNode(FACTORY()), map);

        //set navigator
        this.enginePath = new DijkstraShortestPath<>(this.graph);
        logger.log(Level.INFO, "Graph loaded!");
    }

    /**
     * Method that return the closest Nodes to the Coordinate given.
     * It computes the distance from the point to all the vertexes and the returns the closest one
     * @param coord coordinate of the point to find
     * @return InfoNode node that are the closest to the coordinate given
     */
    public InfoNode findNodes(Coord coord){
        //retrieve all the Nodes
        Set<InfoNode> setOfNodes = this.graph.vertexSet();
        //save all the distance
        Map<String, Double> distances = new HashMap<>();
        //Haversine Distance
        setOfNodes.stream().forEach(infoNode -> distances.put(infoNode.getId(), Haversine.distance(coord.getLat(), coord.getLon(), infoNode.getLat(), infoNode.getLon())));
        //Euclidean Distance
//        setOfNodes.stream().forEach(infoNode -> distances.put(infoNode.getId(), new Coord(new Double(infoNode.retLon()), new Double(infoNode.retLat())).distance(coord)));
        //find the min element
        Map.Entry<String, Double> min = Collections.min(distances.entrySet(), Comparator.comparingDouble(Map.Entry::getValue));

        //retrieve closest point -> it should never return null
        return setOfNodes.stream().filter(x -> x.getId().equals(min.getKey())).findFirst().orElse(null);
    }

    /**
     * Method that find the closest edge to the given coordinates and the node
     * @param coord coordinate of the point in analysis
     * @param nodeCoord coordinate of the initial node
     * @return the closest edge to the coordinate
     */
    public InfoEdge findClosestEdge(Coord coord, Coord nodeCoord){
        //Return all the edges starting from the node
        InfoNode node = this.findNodes(nodeCoord);
        Set<InfoEdge> edges = this.findEdges(node);
        Map<String, Double> distances = new HashMap<>();
        edges.forEach(e -> distances.put(e.getTarget().getId(), this.retDistance(coord, e)));

        //find the min element
        Map.Entry<String, Double> min = Collections.min(distances.entrySet(), Comparator.comparingDouble(Map.Entry::getValue));
        //return the edge or return null
        return edges.stream().filter(e -> e.getTarget().getId().equals(min.getKey())).findFirst().orElse(null);
    }

    /**
     * Return distance between point and line segment
     * @param point point from where compute the distance
     * @param edge segment
     * @return Double number containing the distance
     */
    private Double retDistance(Coord point, InfoEdge edge){
        //x1, y1 to x2, y2 is your line segment
        Double x1 = edge.getSource().getLat();
        Double y1 = edge.getSource().getLon();
        Double x2 = edge.getTarget().getLat();
        Double y2 = edge.getTarget().getLon();
        return this.retDistance(point, x1, y1, x2, y2);
    }

    /**
     * Return distance between point and line segment
     * @param point point from where compute the distance
     * @param x1 segment coordinate
     * @param y1 segment coordinate
     * @param x2 segment coordinate
     * @param y2 segment coordinate
     * @return Double number containing the distance
     */
    private Double retDistance(Coord point, Double x1, Double y1, Double x2, Double y2 ){
        //x, y is your target point
        Double x = point.getLat();
        Double y = point.getLon();
        Double A = x - x1;
        Double B = y - y1;
        Double C = x2 - x1;
        Double D = y2 - y1;

        Double dot = A * C + B * D;
        Double len_sq = C * C + D * D;
        Double param = -1d;
        if (len_sq != 0) //in case of 0 length line
            param = dot / len_sq;

        Double xx, yy;

        if (param < 0) {
            xx = x1;
            yy = y1;
        }
        else if (param > 1) {
            xx = x2;
            yy = y2;
        }
        else {
            xx = x1 + param * C;
            yy = y1 + param * D;
        }

        Double dx = x - xx;
        Double dy = y - yy;
        return Math.sqrt(dx * dx + dy * dy);
    }


    /**
     * Find the exit edges from the given node.
     * Only the nodes that has the node as a source are returned from this method
     * @param node the node from where return the exit edges
     * @return set of exit edges
     */
    public Set<InfoEdge> findEdges(InfoNode node){
        Set<InfoEdge> sets = this.graph.edgesOf(node);
        Set<InfoEdge> realSet = new HashSet<>();
        sets.stream().forEach(el -> {
            if (el.getSource().getId().equals(node.getId())) {
                realSet.add(el);
            }
        });
        return realSet;
    }


    /**
     * Method that finds a path between two nodes.
     * The method is using Dijkstra algorithm from the jgraph library.
     * @param start start node of the path
     * @param end end node of the path
     * @return List of all the nodes of the path
     * @throws Exception if no path is found the method is raising an exception
     */
    public List<InfoNode> findPathBetweenNodes(InfoNode start, InfoNode end) throws Exception {
        //check if a path is in the system
        GraphPath result = this.enginePath.getPath(start, end);
        //if the result is null no path is found
        if (result == null){
            throw new Exception("No path is found in the graph");
        }
        return result.getVertexList();
    }


    /**
     * Method that returns if the edge found is closer than the point found
     * @param closestEdge the edge that is found to be the closest to the position in consideration
     * @param nextPoint point found to be the closest one to the position
     * @param coord position in consideration
     * @return True if the edge is closer, otherwise False
     */
    public Boolean isEdgeCloser(InfoEdge closestEdge, InfoNode nextPoint, Coord coord){
        //distance between the edge and the position
        Double distanceEdgePosition = this.retDistance(coord, closestEdge);

        InfoNode node = closestEdge.getSource();
        //distance between the point found -> start point and the position

        Double distanceLineNewPosition = this.retDistance(coord, node.getLat(), node.getLon(), nextPoint.getLat(), nextPoint.getLon());
        if (distanceEdgePosition <= distanceLineNewPosition){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }


    /**
     * From the initial node return all the nodes located at the end of the edges starting from it
     * @param initialNode initial node
     * @return list of node
     */
    public List<InfoNode> retAllEndEdges(InfoNode initialNode){
        Set<InfoEdge> allTheEdges = this.findEdges(initialNode);
        List<InfoNode> endNodes = new ArrayList<>();
        allTheEdges.forEach(infoEdge -> endNodes.add(infoEdge.getTarget()));
        return endNodes;
    }


    /**
     * Get distance between two connected node
     * @param initialNode start node
     * @param endNode end node
     * @return distance between them
     */
    public Double findDistanceBetweenNodesConnected(InfoNode initialNode, InfoNode endNode){
        Set<InfoEdge> edges = this.findEdges(initialNode);
        return new Double(edges.stream().filter(e -> e.getTarget().equals(endNode)).findFirst().get().retDistance());
    }


    /**
     * Find point in edge between initial node and end node located at distance distance
     * @param initialNode initial node of the edge
     * @param endNode end node of the edge
     * @param distance distance from the start
     * @return coordinate new point
     */
    public Coord findPointInEdge(InfoNode initialNode, InfoNode endNode, Double distance){
        Double dis = findDistanceBetweenNodesConnected(initialNode, endNode);
        Double x = initialNode.getLat() - ((distance * (initialNode.getLat() - endNode.getLat()))/dis);
        Double m = (endNode.getLon() - initialNode.getLon())/(endNode.getLat() - initialNode.getLat());
        Double y = (m * (x - initialNode.getLat())) + initialNode.getLon();
        return new Coord(x,y);

    }
}
