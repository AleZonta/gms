package gms.GraphML;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static gms.GraphML.StringContinousFactory.FACTORY;
import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Alessandro Zonta on 15/05/2017.
 * PhD Situational Analytics
 * <p>
 * Computational Intelligence Group
 * Computer Science Department
 * Faculty of Sciences - VU University Amsterdam
 * <p>
 * a.zonta@vu.nl
 */
public class InfoNodeTest {
    @Test
    public void equals() throws Exception {
        Map<String, String> test = new HashMap<>();
        test.put("x","123");
        test.put("y","456");
        InfoNode infoNode = new InfoNode(FACTORY());
        infoNode.setValues(test);

        assertTrue(infoNode.equals(infoNode));

        String obj = "ss";

        assertFalse(infoNode.equals(obj));

        InfoNode infoNode2 = new InfoNode(FACTORY());
        infoNode2.setValues(test);
        
        assertTrue(infoNode.equals(infoNode2));
    }

    @Test
    public void getLat() throws Exception {
        Map<String, String> test = new HashMap<>();
        test.put("x","123");
        test.put("y","456");
        InfoNode infoNode = new InfoNode(FACTORY());
        infoNode.setValues(test);
        assertNotNull(infoNode.getLat());
        assertEquals(new Double(test.get("y")), infoNode.getLat());


        Map<String, String> test2 = new HashMap<>();
        test2.put("xx","123");
        test2.put("yy","456");
        infoNode = new InfoNode(FACTORY());
        infoNode.setValues(test2);
        assertEquals(new Double(0.0), infoNode.getLat());
    }

    @Test
    public void getLon() throws Exception {
        Map<String, String> test = new HashMap<>();
        test.put("x","123");
        test.put("y","456");
        InfoNode infoNode = new InfoNode(FACTORY());
        infoNode.setValues(test);
        assertNotNull(infoNode.getLon());
        assertEquals(new Double(test.get("x")), infoNode.getLon());


        Map<String, String> test2 = new HashMap<>();
        test2.put("xx","123");
        test2.put("yy","456");
        infoNode = new InfoNode(FACTORY());
        infoNode.setValues(test2);
        assertEquals(new Double(0.0), infoNode.getLon());
    }

    @Test
    public void getValues() throws Exception {
        Map<String, String> test = new HashMap<>();
        test.put("x","123");
        test.put("y","456");
        InfoNode infoNode = new InfoNode(FACTORY());
        infoNode.setValues(test);
        assertNotNull(infoNode.getValues());
    }

    @Test
    public void deepCopy() throws Exception {
        Map<String, String> test = new HashMap<>();
        test.put("x","123");
        test.put("y","456");
        InfoNode infoNode = new InfoNode(FACTORY());
        infoNode.setValues(test);

        InfoNode secondNode = infoNode.deepCopy();
        assertNotEquals(infoNode,secondNode);

    }

    @Test
    public void getId() throws Exception {
        InfoNode infoNode = new InfoNode(FACTORY());
        assertEquals("n1",infoNode.getId());
    }

    @Test
    public void createVertex() throws Exception {
        InfoNode infoNode = new InfoNode(FACTORY());
        assertEquals("n1",infoNode.getId());
        InfoNode infoNode1 = infoNode.createVertex();
        assertNotEquals(infoNode.getId(), infoNode1.getId());
        assertEquals("n2",infoNode1.getId());
    }

    @Test
    public void setValues() throws Exception {
        Map<String, String> test = new HashMap<>();
        test.put("x","123");
        test.put("y","456");
        InfoNode infoNode = new InfoNode(FACTORY());
        infoNode.setValues(test);
    }

    @Test
    public void setValues1() throws Exception {
        //tested in real time. It uses something from external library

    }

    @Test
    public void retLon() throws Exception {
        Map<String, String> test = new HashMap<>();
        test.put("x","123");
        test.put("y","456");
        InfoNode infoNode = new InfoNode(FACTORY());
        infoNode.setValues(test);
        assertNotNull(infoNode.retLon());
        assertEquals(test.get("x"),infoNode.retLon());


        Map<String, String> test2 = new HashMap<>();
        test2.put("xx","123");
        test2.put("yy","456");
        infoNode = new InfoNode(FACTORY());
        infoNode.setValues(test2);
        assertNull(infoNode.retLon());
    }

    @Test
    public void retLat() throws Exception {
        Map<String, String> test = new HashMap<>();
        test.put("x","123");
        test.put("y","456");
        InfoNode infoNode = new InfoNode(FACTORY());
        infoNode.setValues(test);
        assertNotNull(infoNode.retLat());
        assertEquals(test.get("y"),infoNode.retLat());


        Map<String, String> test2 = new HashMap<>();
        test.put("xx","123");
        test.put("yy","456");
        infoNode = new InfoNode(FACTORY());
        infoNode.setValues(test2);
        assertNull(infoNode.retLat());
    }

}