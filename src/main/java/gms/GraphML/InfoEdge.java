package gms.GraphML;

import org.jgrapht.graph.DefaultEdge;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Alessandro Zonta on 12/05/2017.
 * PhD Situational Analytics
 * <p>
 * Computational Intelligence Group
 * Computer Science Department
 * Faculty of Sciences - VU University Amsterdam
 * <p>
 * a.zonta@vu.nl
 *
 *
 * -------------------
 * My version of Edge.
 * It will include all the data I have on the graph
 */
public class InfoEdge extends DefaultEdge {
    private Map<String, String> values; //values that I receive after the reading

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
            }else{
                if(getterAtt.has(Integer.class, k)){
                    this.values.put(k, getterAtt.get(Integer.class, k).toString());
                }
            }
        });
    }

    /**
     * Return the distance between source and target
     * @return String value or null if not present
     */
    public String retDistance(){
        //distance should be "length" element in this case.
        return this.values.getOrDefault("length", null);
    }

    /**
     * Override Getter for the source of the edge
     * @return node that is at the source
     */
    @Override
    public InfoNode getSource() {
        return (InfoNode)super.getSource();
    }

    /**
     * Override Getter for the target of the edge
     * @return node that is at the source
     */
    @Override
    public InfoNode getTarget() {
        return (InfoNode)super.getTarget();
    }

}
