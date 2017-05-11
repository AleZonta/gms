package gms;

import org.junit.Test;

import java.io.IOException;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

/**
 * Created by alessandrozonta on 11/05/2017.
 */
public class ReadConfigTest {
    @Test
    public void getFileLocation() throws Exception {
        //test if I return a location -> that is not null
        ReadConfig conf = new ReadConfig();
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

