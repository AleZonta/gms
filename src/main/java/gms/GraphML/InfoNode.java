package gms.GraphML;

import org.jgrapht.VertexFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Alessandro Zonta on 15/05/2017.
 * PhD Situational Analytics
 * <p>
 * Computational Intelligence Group
 * Computer Science Department
 * Faculty of Sciences - VU University Amsterdam
 * <p>
 * a.zonta@vu.nl
 *
 *
 *
 * -------------------
 * My version of Node.
 * It will include all the data I have on the graph about nodes
 */
public class InfoNode implements VertexFactory<InfoNode> {
    private Map<String, String> values; //values that I receive after the reading
    private String id; //i need a id for every node
    private StringContinousFactory stringContinousFactory; //creator of Ids

    /**
     * create Id for the Node
     * @param stringContinousFactory creator of ids
     */
    public InfoNode(StringContinousFactory stringContinousFactory){
        this.stringContinousFactory = stringContinousFactory;
        this.id = stringContinousFactory.createVertex();
    }

    /**
     * Setter for the variable "values"
     * @param values map containing key and value of every values found
     */
    public void setValues(Map<String, String> values) {
        this.values = values;
    }

    /**
     * Setter for variable "values"
     * Translate the variable from AttributeGetterClass to normal map
     * @param getterAtt object containing all the parameter
     */
    public void setValues(AttributeGetter getterAtt){
        this.values = new HashMap<>();
        Set<String> key = getterAtt.keys();
        key.stream().forEach(k -> {
            if(getterAtt.has(String.class, k)){
                this.values.put(k, getterAtt.get(String.class, k));
            }
        });
    }

    /**
     * Return Longitude node
     * @return string value or null if not present
     */
    public String retLon(){
        //x should be "x" element in this case.
        return this.values.getOrDefault("x", null);
    }

    /**
     * Return Latitude node
     * @return string value or null if not present
     */
    public String retLat(){
        //x should be "y" element in this case.
        return this.values.getOrDefault("y", null);
    }

    /**
     * Override method for create a new vertex
     * @return the new vertex node
     */
    @Override
    public InfoNode createVertex() {
        return new InfoNode(this.stringContinousFactory);
    }

    /**
     * Getter for the Id
     * @return String id
     */
    public String getId() {
        return this.id;
    }
}
