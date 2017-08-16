package gms.ClaxPreload;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

/**
 * Created by Alessandro Zonta on 11/08/2017.
 * PhD Situational Analytics
 * <p>
 * Computational Intelligence Group
 * Computer Science Department
 * Faculty of Sciences - VU University Amsterdam
 * <p>
 * a.zonta@vu.nl
 */
public class PreloadTest {
    @Test
    public void elaborate() throws Exception {
        Preload p = new Preload();
        p.elaborate();
    }

    @Test
    public void mapToString() throws Exception {
        Map<String, Integer> test = new HashMap<>();
        test.put("a", 1);
        test.put("b", 2);

        Preload p = new Preload();
        String res = p.mapToString(test);
        assertNotNull(res);
        System.out.println(res);
    }

    @Test
    public void stringToMap() throws Exception {
        Map<String, Integer> test = new HashMap<>();
        test.put("a", 1);
        test.put("b", 2);

        Preload p = new Preload();
        String res = p.mapToString(test);

        Map<String, Integer> seconRes = p.stringToMap(res);
        assertEquals(test.size(), seconRes.size());
        assertEquals(test.get("a"), seconRes.get("a"));
        assertEquals(test.get("b"), seconRes.get("b"));
    }

}