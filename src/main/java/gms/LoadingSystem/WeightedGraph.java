package gms.LoadingSystem;

import gms.GraphML.InfoEdge;
import gms.GraphML.InfoNode;
import gms.Point.Coord;
import lgds.trajectories.Point;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedPseudograph;

import java.util.List;
import java.util.Set;

/**
 * Created by Alessandro Zonta on 14/08/2017.
 * PhD Situational Analytics
 * <p>
 * Computational Intelligence Group
 * Computer Science Department
 * Faculty of Sciences - VU University Amsterdam
 * <p>
 * a.zonta@vu.nl
 */
public class WeightedGraph implements System{
    private WeightedPseudograph<InfoNode,DefaultWeightedEdge> graph;
    private DijkstraShortestPath<InfoNode, DefaultWeightedEdge> enginePath;

    /**
     * Constructor zero parameter
     * initialisation of the graph
     */
    public WeightedGraph(){
        this.graph = new WeightedPseudograph<>(DefaultWeightedEdge.class);
    }

    /**
     * Getter for the graph
     * @return the graph
     */
    public WeightedPseudograph<InfoNode, DefaultWeightedEdge> getGraph() {
        return this.graph;
    }

    /**
     * Add one node to the graph
     * @param toAdd {@link InfoNode} to add
     */
    public void addNode(InfoNode toAdd){
        this.graph.addVertex(toAdd);
    }

    /**
     * Add edge to graph
     * @param start {@link InfoNode} start of the edge
     * @param end {@link InfoNode} end of the edge
     * @param weight weight of the edge
     */
    public void addEdge(InfoNode start, InfoNode end, Double weight){
        DefaultWeightedEdge e =  new DefaultWeightedEdge();
        this.graph.setEdgeWeight(e, weight);
        this.graph.addEdge(start, end, e);
    }

    @Override
    public void loadGraph() throws Exception {
        throw new NoSuchMethodException("This method is not implemented");
    }

    @Override
    public InfoNode findNodes(Coord coord) {
        throw new NoSuchMethodError("This method is not implemented");
    }

    @Override
    public InfoEdge findClosestEdge(Coord coord, Coord nodeCoord) {
        throw new NoSuchMethodError("This method is not implemented");
    }

    @Override
    public Set<InfoEdge> findEdges(InfoNode node) {
        throw new NoSuchMethodError("This method is not implemented");
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
     * Method that finds a path between two nodes.
     * The method is using Dijkstra algorithm from the jgraph library.
     * @param start start point of the path
     * @param end end point of the path
     * @return List of all the nodes of the path
     * @throws Exception if no path is found the method is raising an exception
     */
    public List<InfoNode> findPathBetweenNodes(Point start, Point end) throws Exception {
        InfoNode startNode = this.findNodes(new Coord(start.getLatitude(), start.getLongitude()));
        InfoNode endNode = this.findNodes(new Coord(end.getLatitude(), end.getLongitude()));
        return this.findPathBetweenNodes(startNode,endNode);
    }





    @Override
    public List<InfoNode> retAllEndEdges(InfoNode initialNode) {
        throw new NoSuchMethodError("This method is not implemented");
    }

    @Override
    public Double findDistanceBetweenNodesConnected(InfoNode initialNode, InfoNode endNode) {
        throw new NoSuchMethodError("This method is not implemented");
    }

    @Override
    public Coord findPointInEdge(InfoNode initialNode, InfoNode endNode, Double distance) {
        throw new NoSuchMethodError("This method is not implemented");
    }

    @Override
    public Object insideBoundaries(Coord coord) throws Exception {
        throw new NoSuchMethodException("This method is not implemented");
    }

    @Override
    public Boolean isEdgeCloser(InfoEdge closestEdge, InfoNode nextPoint, Coord coord) {
        throw new NoSuchMethodError("This method is not implemented");
    }

    @Override
    public InfoNode getNodeFromId(String id) {
        throw new NoSuchMethodError("This method is not implemented");
    }

    @Override
    public Set<InfoEdge> getEdgeSet() {
        throw new NoSuchMethodError("This method is not implemented");
    }

    @Override
    public Set<InfoNode> getNodesSet() {
        throw new NoSuchMethodError("This method is not implemented");
    }

    @Override
    public void setOptimisedGraph(Boolean optimisedGraph) {
        throw new NoSuchMethodError("This method is not implemented");
    }
}
