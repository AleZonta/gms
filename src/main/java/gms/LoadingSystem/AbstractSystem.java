package gms.LoadingSystem;

import Connections.DatabaseDistance;
import gms.GraphML.InfoEdge;
import gms.GraphML.InfoNode;
import gms.KdTree.KdTree;
import gms.Point.Coord;
import gms.Point.Haversine;
import org.jgrapht.Graph;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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
 * Class implementing coomon method to the loaders
 */
public abstract class AbstractSystem implements System{
    private DatabaseDistance db; //database saving the distance
    //tree for find the closest node in a fastest way
    protected KdTree<KdTree.XYZPoint> kdTree;

    /**
     * Constructor one element. Build the distance
     */
    public AbstractSystem(){
        this.db = new DatabaseDistance();
    }


    /**
     * Method that return the closest Nodes to the Coordinate given.
     * It computes the distance from the point to all the vertexes and the returns the closest one
     * @param graph graph where to check for the node
     * @param coord coordinate of the point to find
     * @return InfoNode node that are the closest to the coordinate given
     */
    protected InfoNode findNode(Graph<InfoNode, InfoEdge> graph, Coord coord){
        //retrieve all the Nodes
        Set<InfoNode> setOfNodes = graph.vertexSet();
        //save all the distance
        Map<String, Double> distances = new ConcurrentHashMap<>();
        //Haversine Distance

        setOfNodes.parallelStream().forEach(infoNode ->  {

            //If i am using the db system
            Double dist = 0d;
            if(this.db.getEnable()) {
                //check if I have already found these two elements
                Double dis = this.db.readData(coord.getLat(), coord.getLon(), infoNode.getLat(), infoNode.getLon());
                if (dis == null) {
                    //I have not found them. Need to compute
                    dist = Haversine.distance(coord.getLat(), coord.getLon(), infoNode.getLat(), infoNode.getLon());
                    this.db.insertData(coord.getLat(), coord.getLon(), infoNode.getLat(), infoNode.getLon(), dist);
                }
            }else{
                dist = Haversine.distance(coord.getLat(), coord.getLon(), infoNode.getLat(), infoNode.getLon());
            }


            distances.put(infoNode.getId(), dist);
        });
        //Euclidean Distance
//        setOfNodes.stream().forEach(infoNode -> distances.put(infoNode.getId(), new Coord(new Double(infoNode.retLon()), new Double(infoNode.retLat())).distance(coord)));
        //find the min element
        Map.Entry<String, Double> min = Collections.min(distances.entrySet(), Comparator.comparingDouble(Map.Entry::getValue));

        //retrieve closest point -> it should never return null
        return setOfNodes.stream().filter(x -> x.getId().equals(min.getKey())).findFirst().orElse(null);
    }

    /**
     * Method that return the closest Nodes to the Coordinate given.
     * It usesd the {@link KdTree} build during the loading of the system
     * @param coord coordinate of the point to find
     * @return InfoNode node that are the closest to the coordinate given
     */
    protected InfoNode findNode(Coord coord){
        //convert coord to XYZPoint
        KdTree.XYZPoint pointConverted = new KdTree.XYZPoint(coord.getLon(), coord.getLat());
        Integer k = 1; //how many nodes do I want to find
        List<KdTree.XYZPoint> result = (List<KdTree.XYZPoint>) this.kdTree.nearestNeighbourSearch(1, pointConverted);
        //should be only one point
        return (InfoNode)result.get(0);
    }


    /**
     * Return distance between point and line segment
     * @param point point from where compute the distance
     * @param edge segment
     * @return Double number containing the distance
     */
    protected Double retDistance(Coord point, InfoEdge edge){
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
    protected Double retDistance(Coord point, Double x1, Double y1, Double x2, Double y2 ){
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
     * Method that returns if the edge found is closer than the point found
     * @param closestEdge the edge that is found to be the closest to the position in consideration
     * @param nextPoint point found to be the closest one to the position
     * @param coord position in consideration
     * @return True if the edge is closer, otherwise False
     */
    @Override
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
     * Method that returns the node from the given ID
     * @param graph graph where to find the node
     * @param id id of the node
     * @return node
     */
    public InfoNode getNodeFromId(Graph<InfoNode, InfoEdge> graph, String id) {
        //retrieve all the Nodes
        Set<InfoNode> setOfNodes = graph.vertexSet();
        return setOfNodes.stream().filter(x -> x.getId().equals(id)).findFirst().orElse(null);
    }
}
