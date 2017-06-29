package gms.LoadingSystem;

import gms.GraphML.InfoEdge;
import gms.GraphML.InfoNode;
import gms.Point.Coord;

import java.util.List;
import java.util.Set;

/**
 * Created by Alessandro Zonta on 29/06/2017.
 * PhD Situational Analytics
 * <p>
 * Computational Intelligence Group
 * Computer Science Department
 * Faculty of Sciences - VU University Amsterdam
 * <p>
 * a.zonta@vu.nl
 *
 * Interface that a loader has to implement
 */
public interface System {

    /**
     * Method that loads the graph
     * It is creating a Direct graph with loops and multi-edges.
     * @throws Exception the function raises an exception if there are problems with the config file
     */
    void loadGraph() throws Exception;

    /**
     * Method that return the closest Nodes to the Coordinate given.
     * It computes the distance from the point to all the vertexes and the returns the closest one
     * @param coord coordinate of the point to find
     * @return InfoNode node that are the closest to the coordinate given
     */
    InfoNode findNodes(Coord coord);

    /**
     * Method that find the closest edge to the given coordinates and the node
     * @param coord coordinate of the point in analysis
     * @param nodeCoord coordinate of the initial node
     * @return the closest edge to the coordinate
     */
    InfoEdge findClosestEdge(Coord coord, Coord nodeCoord);

    /**
     * Find the exit edges from the given node.
     * Only the nodes that has the node as a source are returned from this method
     * @param node the node from where return the exit edges
     * @return set of exit edges
     */
    Set<InfoEdge> findEdges(InfoNode node);

    /**
     * Method that finds a path between two nodes.
     * The method is using Dijkstra algorithm from the jgraph library.
     * @param start start node of the path
     * @param end end node of the path
     * @return List of all the nodes of the path
     * @throws Exception if no path is found the method is raising an exception
     */
    List<InfoNode> findPathBetweenNodes(InfoNode start, InfoNode end) throws Exception;

    /**
     * From the initial node return all the nodes located at the end of the edges starting from it
     * @param initialNode initial node
     * @return list of node
     */
    List<InfoNode> retAllEndEdges(InfoNode initialNode);

    /**
     * Get distance between two connected node
     * @param initialNode start node
     * @param endNode end node
     * @return distance between them
     */
    Double findDistanceBetweenNodesConnected(InfoNode initialNode, InfoNode endNode);

    /**
     * Find point in edge between initial node and end node located at distance distance
     * @param initialNode initial node of the edge
     * @param endNode end node of the edge
     * @param distance distance from the start
     * @return coordinate new point
     */
    Coord findPointInEdge(InfoNode initialNode, InfoNode endNode, Double distance);


    /**
     * Check if the coordinate given are inside the graph
     * @param coord coordinate to check
     * @return Object Value
     * @exception Exception an Exception is raised if the Boundaries are not computed
     */
    Object insideBoundaries(Coord coord) throws Exception;

    /**
     * Method that returns if the edge found is closer than the point found
     * @param closestEdge the edge that is found to be the closest to the position in consideration
     * @param nextPoint point found to be the closest one to the position
     * @param coord position in consideration
     * @return True if the edge is closer, otherwise False
     */
    Boolean isEdgeCloser(InfoEdge closestEdge, InfoNode nextPoint, Coord coord);

}
