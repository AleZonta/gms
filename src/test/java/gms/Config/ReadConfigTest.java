package gms.Config;

import org.junit.Test;

import java.io.IOException;

import static junit.framework.TestCase.*;

/**
 * Created by Alessandro Zonta on 11/05/2017.
 * PhD Situational Analytics
 * <p>
 * Computational Intelligence Group
 * Computer Science Department
 * Faculty of Sciences - VU University Amsterdam
 * <p>
 * a.zonta@vu.nl
 */
public class ReadConfigTest {
    @Test
    public void getUsingMoreSources() throws Exception {
        //test if I return a name -> that is not null
        ReadConfig conf = new ReadConfig();
        try {
            conf.getFileName();
        } catch (Exception e) {
            assertTrue(e.getMessage().equals("Try to access config file before reading it.") );
        }
        try {
            conf.readFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertNotNull(conf.getFileName());
        assertTrue(conf.getFileName().size() == 1);
        assertFalse(conf.getUsingMoreSources());
    }

    @Test
    public void getPath() throws Exception {
        //test if I return a path -> that is not null and is the combination of location + name
        ReadConfig conf = new ReadConfig();
        try {
            conf.getPath(0);
        } catch (Exception e) {
            assertTrue(e.getMessage().equals("Try to access config file before reading it.") );
        }
        try {
            conf.readFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertNotNull(conf.getPath(0));
        String total = conf.getFileLocation() + "/" + conf.getFileName().get(0);
        assertEquals(total, conf.getPath(0));
    }

    @Test
    public void getFileLocation() throws Exception {
        //test if I return a location -> that is not null
        ReadConfig conf = new ReadConfig();
        try {
            conf.getFileLocation();
        } catch (Exception e) {
            assertTrue(e.getMessage().equals("Try to access config file before reading it.") );
        }
        try {
            conf.readFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertNotNull(conf.getFileLocation());
    }

    @Test
    public void getFileName() throws Exception {
        //test if I return a name -> that is not null
        ReadConfig conf = new ReadConfig();
        try {
            conf.getFileName();
        } catch (Exception e) {
            assertTrue(e.getMessage().equals("Try to access config file before reading it.") );
        }
        try {
            conf.readFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertNotNull(conf.getFileName());
        assertTrue(conf.getFileName().size() == 1);
    }

    @Test
    public void readFile() throws Exception {
        //test if I read the file without exception
        //the name of the file is hardcoded
        ReadConfig conf = new ReadConfig();
        try {
            conf.readFile();
        } catch (Exception e) {
            assertTrue(e.getMessage().equals("FileName is missing.") || e.getMessage().equals("FileLocation is missing.") || e.getMessage().equals("Config file not found.") || e.getMessage().equals("JSON file not well formatted."));
        }

    }

}

