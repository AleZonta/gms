package gms.LoadingSystem;


import gms.Config.ReadConfig;
import gms.GraphML.GraphMLImporter;
import gms.GraphML.InfoEdge;
import gms.GraphML.InfoNode;
import gms.KdTree.KdTree;
import gms.Point.Coord;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DirectedPseudograph;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
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
public class Loader extends AbstractSystem implements System{
    private final ReadConfig conf; //configuration object containing location where to read the graph
    private static Logger logger; //logger for this class
    private final Graph<InfoNode, InfoEdge> graph; //graph
    private DijkstraShortestPath<InfoNode, InfoEdge> enginePath;
    //for the boundaries
    private Coord minValue;
    private Coord maxValue;
    private Boolean optimisedGraph;



    /**
     * Constructor with zero parameter
     * The config file is read.
     * @param log logger
     * @throws Exception If the reading of the config file encounter in problems an exception is raised
     */
    public Loader(Logger log) throws Exception {
        logger = log;
        this.conf = new ReadConfig();
        this.conf.readFile();
        this.graph = new DirectedPseudograph<>(InfoEdge.class); //allows loops and multi-edges
        this.enginePath = null;
        this.minValue = null;
        this.maxValue = null;
        this.kdTree = null;
        this.optimisedGraph = Boolean.FALSE; //Not using the graph, default situation
    }


    /**
     * Method that loads the graph
     * It is creating a Direct graph with loops and multi-edges.
     * @throws Exception the function raises an exception if there are problems with the config file
     */
    @Override
    public void loadGraph() throws Exception {
        logger.log(Level.INFO, "Loading Graph routes...");
        //create File object for read the graphML
        Path path = Paths.get(this.conf.getPath(0));
        File file = path.toFile();

        //create reader
        GraphMLImporter<InfoNode, InfoEdge> result = GraphMLImporter.createFromFile(file);
        //Now I have to put the graph into a graph object
        Map<String, InfoNode> map = new ConcurrentHashMap<>();
        result.generateGraph(this.graph, new InfoNode(FACTORY()), map);

        //set navigator
        this.enginePath = new DijkstraShortestPath<>(this.graph);

        //compute boundaries
        this.computeBoundaries();


        Set<InfoNode> setOfNodes = graph.vertexSet();
        this.kdTree = new KdTree<>();
        setOfNodes.forEach(node -> this.kdTree.add(new KdTree.XYZPoint(node)));

        logger.log(Level.INFO, "Graph loaded!");
    }

    /**
     * Method that return the closest Nodes to the Coordinate given.
     * It computes the distance from the point to all the vertexes and the returns the closest one
     * @param coord coordinate of the point to find
     * @return InfoNode node that are the closest to the coordinate given
     */
    @Override
    public InfoNode findNodes(Coord coord){
        if(this.optimisedGraph){
            return this.findNode(coord);
        }else {
            return this.findNode(this.graph, coord);
        }
    }

    /**
     * Method that find the closest edge to the given coordinates and the node
     * @param coord coordinate of the point in analysis
     * @param nodeCoord coordinate of the initial node
     * @return the closest edge to the coordinate
     */
    @Override
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
     * Find the exit edges from the given node.
     * Only the nodes that has the node as a source are returned from this method
     * @param node the node from where return the exit edges
     * @return set of exit edges
     */
    @Override
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
    @Override
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
     * From the initial node return all the nodes located at the end of the edges starting from it
     * @param initialNode initial node
     * @return list of node
     */
    @Override
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
    @Override
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
    @Override
    public Coord findPointInEdge(InfoNode initialNode, InfoNode endNode, Double distance){
        Double dis = findDistanceBetweenNodesConnected(initialNode, endNode);
        Double x = initialNode.getLat() - ((distance * (initialNode.getLat() - endNode.getLat()))/dis);
        Double m = (endNode.getLon() - initialNode.getLon())/(endNode.getLat() - initialNode.getLat());
        Double y = (m * (x - initialNode.getLat())) + initialNode.getLon();
        return new Coord(x,y);
    }


    /**
     * Compute boundaries of the graph
     */
    private void computeBoundaries(){
        //initialise to zero the value for the root and height and width
        this.minValue = new Coord(Double.MAX_VALUE,Double.MAX_VALUE);
        this.maxValue = new Coord(Double.MIN_VALUE,Double.MIN_VALUE);

        this.graph.vertexSet().forEach(v -> {
            Double lat = v.getLat();
            Double lon = v.getLon();
            this.minValue.setLat(Math.min(this.minValue.getLat(), lat));
            this.minValue.setLon(Math.min(this.minValue.getLon(), lon));
            this.maxValue.setLat(Math.max(this.maxValue.getLat(), lat));
            this.maxValue.setLon(Math.max(this.maxValue.getLon(), lon));
        });
    }

    /**
     * Check if the coordinate given are inside the graph
     * @param coord coordinate to check
     * @return Boolean Value
     * @exception Exception an Exception is raised if the Boundaries are not computed
     */
    @Override
    public Boolean insideBoundaries(Coord coord) throws Exception {
        if (this.maxValue == null) throw new Exception("Compute Boundaries before check if the element is present");
        if(coord.getLon()>=this.minValue.getLon() && coord.getLon()<=this.maxValue.getLon()){
            if(coord.getLat()>=this.minValue.getLat() && coord.getLat()<=this.maxValue.getLat()){
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Method that returns the node from the given ID
     * @param id id of the node
     * @return node
     */
    @Override
    public InfoNode getNodeFromId(String id) {
        return super.getNodeFromId(this.graph, id);
    }


    /**
     * Return all the edge of the graph
     * @return set of edges
     */
    @Override
    public Set<InfoEdge> getEdgeSet(){
        return this.graph.edgeSet();
    }

    /**
     * REturn all the nodes of the graph
     * @return set of nodes
     */
    @Override
    public Set<InfoNode> getNodesSet(){
        return  this.graph.vertexSet();
    }


    /**
     * Get the flag for using the KDtree or not
     * @return Boolean variable
     */
    public Boolean getOptimisedGraph() {
        return optimisedGraph;
    }

    /**
     * Setter for the flag -> TRue I am using the KDTree, False not
     * @param optimisedGraph Boolena value
     */
    @Override
    public void setOptimisedGraph(Boolean optimisedGraph) {
        this.optimisedGraph = optimisedGraph;
    }
}
