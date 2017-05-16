package gms.Config;

import gms.Config.ReadConfig;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

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
    public void getPath() throws Exception {
        //test if I return a path -> that is not null and is the combination of location + name
        ReadConfig conf = new ReadConfig();
        try {
            conf.getPath();
        } catch (Exception e) {
            assertTrue(e.getMessage().equals("Try to access config file before reading it.") );
        }
        try {
            conf.readFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertNotNull(conf.getPath());
        String total = conf.getFileLocation() + "/" + conf.getFileName();
        assertEquals(total, conf.getPath());
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

