package gms.GraphML;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
public class InfoEdgeTest {
    @Test
    public void setValues1() throws Exception {
        //tested in real time. It uses something from external library
    }

    @Test
    public void setValues() throws Exception {
        Map<String, String> test = new HashMap<>();
        test.put("d8","9999.99");
        InfoEdge infoEdge = new InfoEdge();
        infoEdge.setValues(test);
    }

    @Test
    public void retDistance() throws Exception {
        Map<String, String> test = new HashMap<>();
        test.put("length","9999.99");
        InfoEdge infoEdge = new InfoEdge();
        infoEdge.setValues(test);
        assertNotNull(infoEdge.retDistance());
        assertEquals(test.get("length"),infoEdge.retDistance());


        Map<String, String> test2 = new HashMap<>();
        test.put("d9","9999.99");
        infoEdge = new InfoEdge();
        infoEdge.setValues(test2);
        assertNull(infoEdge.retDistance());
    }

}