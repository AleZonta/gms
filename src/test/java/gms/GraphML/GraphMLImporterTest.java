package gms.GraphML;

import gms.ReadConfig;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedPseudograph;
import org.junit.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static gms.GraphML.StringContinousFactory.FACTORY;
import static junit.framework.TestCase.assertNotNull;

/**
 * Created by Alessandro Zonta on 12/05/2017.
 * PhD Situational Analytics
 * <p>
 * Computational Intelligence Group
 * Computer Science Department
 * Faculty of Sciences - VU University Amsterdam
 * <p>
 * a.zonta@vu.nl
 */
public class GraphMLImporterTest {
    @Test
    public void createFromFile() throws Exception {
        ReadConfig conf = new ReadConfig();
        conf.readFile();
        Path path = Paths.get(conf.getPath());
        File file = path.toFile();

        //In this way I have created a reader.
        //I am checking it is not null
        GraphMLImporter<String, DefaultEdge> result = GraphMLImporter.createFromFile(file);
        assertNotNull(result);
    }

    @Test
    public void generateGraph() throws Exception {
        ReadConfig conf = new ReadConfig();
        conf.readFile();
        Path path = Paths.get(conf.getPath());
        File file = path.toFile();

        //In this way I have created a reader.
        GraphMLImporter<InfoNode, InfoEdge> result = GraphMLImporter.createFromFile(file);

        //Now I have to put the graph into a graph object
        Graph<InfoNode, InfoEdge> g = new DirectedPseudograph<>(InfoEdge.class);
        Map<String, InfoNode> map = new HashMap<>();


        result.generateGraph(g, new InfoNode(FACTORY()), map);

        assertNotNull(g);

    }

}