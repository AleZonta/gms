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
     * Constructor with all the parameter
     * @param id Id of the node
     * @param values detail of the node
     */
    public InfoNode(String id, Map<String, String> values){
        this.stringContinousFactory = null;
        this.id = id;
        this.values = values;
    }

    /**
     * Setter for the variable "values"
     * @param values map containing key and value of every values found
     */
    public void setValues(Map<String, String> values) {
        this.values = values;
    }

    /**
     * Getter for the variable "values"
     * @return the map containing the values
     */
    public Map<String, String> getValues() {
        return this.values;
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
     * Return Latitude node
     * @return string value or null if not present
     */
    public String retLat(){
        //x should be "x" element in this case.
        return this.values.getOrDefault("y", null);
    }

    /**
     * Return Latitude node
     * @return double value or null if not present
     */
    public Double getLat(){
        //x should be "x" element in this case.
        return new Double(this.values.getOrDefault("y", "0.0"));
    }

    /**
     * Return Longitude node
     * @return string value or null if not present
     */
    public String retLon(){
        //x should be "y" element in this case.
        return this.values.getOrDefault("x", null);
    }

    /**
     * Return Longitude node
     * @return double value or null if not present
     */
    public Double getLon(){
        //x should be "x" element in this case.
        return new Double(this.values.getOrDefault("x", "0.0"));
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


    /**
     * Deep Copy method for the node
     * @return deep copy of the node
     */
    public InfoNode deepCopy(){
        return new InfoNode(this.getId(), this.getValues());
    }

    /**
     * override method equals
     * @param obj object to compare with this InfoNode
     * @return if the two nodes are the same or not
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof InfoNode)) {
            return false;
        }

        InfoNode node = (InfoNode) obj;

        return node.id.equals(this.id) && node.values.equals(this.values);
    }
}
