package gms.LoadingSystem;

import gms.Config.ReadConfig;
import gms.GraphML.GraphMLImporter;
import gms.GraphML.InfoEdge;
import gms.GraphML.InfoNode;
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
import java.util.stream.IntStream;

import static gms.GraphML.StringContinousFactory.FACTORY;

/**
 * Created by Alessandro Zonta on 29/06/2017.
 * PhD Situational Analytics
 * <p>
 * Computational Intelligence Group
 * Computer Science Department
 * Faculty of Sciences - VU University Amsterdam
 * <p>
 * a.zonta@vu.nl
 */
public class MultyLoader extends AbstractSystem  implements System{
    private final ReadConfig conf; //configuration object containing location where to read the graph
    private static final Logger logger = Logger.getLogger(Loader.class.getName()); //logger for this class
    private final List<Graph<InfoNode, InfoEdge>> graphs; //graph
    private List<DijkstraShortestPath<InfoNode, InfoEdge>> enginePaths;
    //for the boundaries
    private List<Coord> minValue;
    private List<Coord> maxValue;
    private Integer numOfGraph;
    private Boolean optimisedGraph;

    /**
     * Constructor of the class with zero parameter
     */
    public MultyLoader() throws Exception {
        this.conf = new ReadConfig();
        this.conf.readFile();
        this.numOfGraph = this.conf.getFileName().size();
        this.graphs = new ArrayList<>();
        this.enginePaths = new ArrayList<>();
        this.minValue = new ArrayList<>();
        this.maxValue = new ArrayList<>();
        this.optimisedGraph = Boolean.FALSE; //Not using the graph, default situation
    }

    /**
     * Method that loads the graph
     * It is creating a Direct graph with loops and multi-edges.
     * @throws Exception the function raises an exception if there are problems with the config file
     */
    @Override
    public void loadGraph() throws Exception {
        logger.log(Level.INFO, "Loading Graphs routes...");
        IntStream.range(0, this.numOfGraph).forEach(i ->{
            //create File object for read the graphML
            Path path = null;
            try {
                path = Paths.get(this.conf.getPath(i));
            } catch (Exception e) {
                throw new Error("No file's name found");
            }
            File file = path.toFile();

            //create reader
            GraphMLImporter<InfoNode, InfoEdge> result = GraphMLImporter.createFromFile(file);
            //Now I have to put the graph into a graph object
            Map<String, InfoNode> map = new ConcurrentHashMap<>();
            Graph<InfoNode, InfoEdge> graph = new DirectedPseudograph<>(InfoEdge.class); //allows loops and multi-edges
            result.generateGraph(graph, new InfoNode(FACTORY()), map);

            //set navigator
            DijkstraShortestPath<InfoNode, InfoEdge> enginePath = new DijkstraShortestPath<>(graph);

            //add everything to list
            this.graphs.add(graph);
            this.enginePaths.add(enginePath);

            //compute boundaries on graph position i
            this.computeBoundaries(i);
            logger.log(Level.INFO, "Graph loaded!");
        });
    }

    /**
     * Method that return the closest Nodes to the Coordinate given.
     * It computes the distance from the point to all the vertexes and the returns the closest one
     * @param coord coordinate of the point to find
     * @return InfoNode node that are the closest to the coordinate given
     */
    @Override
    public InfoNode findNodes(Coord coord) {
        try {
            Integer pos = this.insideBoundaries(coord);
            if(pos != -1){
                return this.findNode(this.graphs.get(pos), coord);
            }else{
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Method that find the closest edge to the given coordinates and the node
     * @param coord coordinate of the point in analysis
     * @param nodeCoord coordinate of the initial node
     * @return the closest edge to the coordinate
     */
    @Override
    public InfoEdge findClosestEdge(Coord coord, Coord nodeCoord) {
        //Return all the edges starting from the node
        InfoNode node = this.findNodes(nodeCoord);
        if(node!=null) {
            Set<InfoEdge> edges = this.findEdges(node);
            Map<String, Double> distances = new HashMap<>();
            edges.forEach(e -> distances.put(e.getTarget().getId(), this.retDistance(coord, e)));

            //find the min element
            Map.Entry<String, Double> min = Collections.min(distances.entrySet(), Comparator.comparingDouble(Map.Entry::getValue));
            //return the edge or return null
            return edges.stream().filter(e -> e.getTarget().getId().equals(min.getKey())).findFirst().orElse(null);
        }else{
            return null;
        }
    }

    /**
     * Find the exit edges from the given node.
     * Only the nodes that has the node as a source are returned from this method
     * @param node the node from where return the exit edges
     * @return set of exit edges
     */
    @Override
    public Set<InfoEdge> findEdges(InfoNode node) {
        Coord coord = new Coord(node.getLat(),node.getLon());
        try {
            Integer pos = this.insideBoundaries(coord);
            if(pos != -1){
                Set<InfoEdge> sets = this.graphs.get(pos).edgesOf(node);
                Set<InfoEdge> realSet = new HashSet<>();
                sets.stream().forEach(el -> {
                    if (el.getSource().getId().equals(node.getId())) {
                        realSet.add(el);
                    }
                });
                return realSet;
            }else{
                return null;
            }
        } catch (Exception e) {
            return null;
        }
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
        if(start == null || end == null) throw new Exception("No path is found in the graph");
        Coord coord = new Coord(start.getLat(), start.getLon());
        Integer pos = this.insideBoundaries(coord);
        if(pos != -1){
            //check if a path is in the system
            GraphPath result = this.enginePaths.get(pos).getPath(start, end);
            //if the result is null no path is found
            if (result == null){
                throw new Exception("No path is found in the graph");
            }
            return result.getVertexList();
        }else{
            return null;
        }

    }

    /**
     * From the initial node return all the nodes located at the end of the edges starting from it
     * @param initialNode initial node
     * @return list of node
     */
    @Override
    public List<InfoNode> retAllEndEdges(InfoNode initialNode) {
        Set<InfoEdge> allTheEdges = this.findEdges(initialNode);
        if(allTheEdges!= null) {
            List<InfoNode> endNodes = new ArrayList<>();
            allTheEdges.forEach(infoEdge -> endNodes.add(infoEdge.getTarget()));
            return endNodes;
        }
        return null;
    }

    /**
     * Get distance between two connected node
     * @param initialNode start node
     * @param endNode end node
     * @return distance between them
     */
    @Override
    public Double findDistanceBetweenNodesConnected(InfoNode initialNode, InfoNode endNode) {
        Set<InfoEdge> edges = this.findEdges(initialNode);
        if(edges!= null) {
            return new Double(edges.stream().filter(e -> e.getTarget().equals(endNode)).findFirst().get().retDistance());
        }
        return 0d;
    }

    /**
     * Find point in edge between initial node and end node located at distance distance
     * @param initialNode initial node of the edge
     * @param endNode end node of the edge
     * @param distance distance from the start
     * @return coordinate new point
     */
    @Override
    public Coord findPointInEdge(InfoNode initialNode, InfoNode endNode, Double distance) {
        Double dis = findDistanceBetweenNodesConnected(initialNode, endNode);
        if(dis != 0) {
            Double x = initialNode.getLat() - ((distance * (initialNode.getLat() - endNode.getLat())) / dis);
            Double m = (endNode.getLon() - initialNode.getLon()) / (endNode.getLat() - initialNode.getLat());
            Double y = (m * (x - initialNode.getLat())) + initialNode.getLon();
            return new Coord(x,y);
        }
        return new Coord(initialNode.getLat(),initialNode.getLon());
    }

    /**
     * Compute boundaries of the graph
     */
    private void computeBoundaries(Integer position) {
        //initialise to zero the value for the root and height and width
        Coord minValue = new Coord(Double.MAX_VALUE,Double.MAX_VALUE);
        Coord maxValue = new Coord(Double.MIN_VALUE,Double.MIN_VALUE);

        this.graphs.get(position).vertexSet().forEach(v -> {
            Double lat = v.getLat();
            Double lon = v.getLon();
            minValue.setLat(Math.min(minValue.getLat(), lat));
            minValue.setLon(Math.min(minValue.getLon(), lon));
            maxValue.setLat(Math.max(maxValue.getLat(), lat));
            maxValue.setLon(Math.max(maxValue.getLon(), lon));
        });
        //add to the list
        this.maxValue.add(maxValue);
        this.minValue.add(minValue);
    }

    /**
     * Check if the coordinate given are inside the graph
     * @param coord coordinate to check
     * @return Integer Value -> index of the graph containing the coordinate. -1 if no graph contains it
     * @exception Exception an Exception is raised if the Boundaries are not computed
     */
    public Integer insideBoundaries(Coord coord) throws Exception {
        if (this.maxValue.size() == 0) throw new Exception("Compute Boundaries before check if the element is present");
        List<Integer> positions = new ArrayList<>();
        IntStream.range(0, this.numOfGraph).forEach(i -> {
            if(coord.getLon()>=this.minValue.get(i).getLon() && coord.getLon()<=this.maxValue.get(i).getLon()){
                if(coord.getLat()>=this.minValue.get(i).getLat() && coord.getLat()<=this.maxValue.get(i).getLat()){
                    positions.add(i);
                }else{
                    positions.add(-1);
                }
            }else{
                positions.add(-1);
            }

        });
        Integer occ = Collections.frequency(positions, -1);
        if(Objects.equals(occ, this.numOfGraph)){
            return -1;
        }else{
            int i = 0;
            while(i < this.numOfGraph && positions.get(i) == -1){
                i++;
            }
            return positions.get(i);
        }

    }

    /**
     * Method that returns the node from the given ID
     * @param id id of the node
     * @return node
     */
    public InfoNode getNodeFromId(String id) {
        for(Graph<InfoNode, InfoEdge> graph: this.graphs){
            InfoNode res = super.getNodeFromId(graph, id);
            if(res!=null) return res;
        }
        return null;
    }

    /**
     * Return all the edge of the graph
     * @return set of edges
     */
    @Override
    public Set<InfoEdge> getEdgeSet() {
        throw new NoSuchMethodError("Not jet implemented");
    }

    /**
     * REturn all the nodes of the graph
     * @return set of nodes
     */
    @Override
    public Set<InfoNode> getNodesSet() {
        throw new NoSuchMethodError("Not jet implemented");
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
        if(optimisedGraph) throw new NoSuchMethodError("DkTree search not miplemented with multy graph");
        this.optimisedGraph = optimisedGraph;
    }
}
