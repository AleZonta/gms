package gms;


import gms.Config.ReadConfig;
import gms.GraphML.GraphMLImporter;
import gms.GraphML.InfoEdge;
import gms.GraphML.InfoNode;
import gms.Point.Coord;
import gms.Point.Haversine;
import org.jgrapht.Graph;
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
    private ReadConfig conf; //configuration object containing location where to read the graph
    private static final Logger logger = Logger.getLogger(Loader.class.getName()); //logger for this class
    private Graph<InfoNode, InfoEdge> graph; //graph


    /**
     * Constructor with zero parameter
     * The config file is read.
     * @throws Exception If the reading of the config file encounter in problems an exception is raised
     */
    public Loader() throws Exception {
        this.conf = new ReadConfig();
        this.conf.readFile();
        this.graph = new DirectedPseudograph<>(InfoEdge.class); //allows loops and multi-edges

        //logger action
//        ConsoleHandler consoleHandler = new ConsoleHandler();
//        logger.addHandler(consoleHandler);

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
        setOfNodes.stream().forEach(infoNode -> distances.put(infoNode.getId(), Haversine.distance(coord.getLat(), coord.getLon(), new Double(infoNode.retLat()), new Double(infoNode.retLon()))));
        //find the min element
        Map.Entry<String, Double> min = Collections.min(distances.entrySet(), Comparator.comparingDouble(Map.Entry::getValue));

        //retrieve closest point -> it should never return null
        return setOfNodes.stream().filter(x -> x.getId().equals(min.getKey())).findFirst().orElse(null);
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


}
