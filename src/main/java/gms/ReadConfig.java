package gms;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * Created by Alessandro Zonta on 11/05/2017.
 * PhD Situational Analytics
 * <p>
 * Computational Intelligence Group
 * Computer Science Department
 * Faculty of Sciences - VU University Amsterdam
 * <p>
 * a.zonta@vu.nl
 *
 * This class reads the config file with the info needed by the program
 */
public class ReadConfig {
    private String fileName;
    private String fileLocation;
    private String path;

    /**
     * Method that returns the location of the file containing the graph
     * @return String with the path
     */
    public String getFileLocation() throws Exception {
        if(this.fileLocation == null) throw new Exception("Try to access config file before reading it.");
        return this.fileLocation;
    }

    /**
     * Method that returns the name of the file containing the graph
     * @return String with the name
     */
    public String getFileName() throws Exception {
        if(this.fileName == null) throw new Exception("Try to access config file before reading it.");
        return this.fileName;
    }

    /**
     * Method that returns the complete path of the file
     * path is the combination of file location and file name
     * @return String with the path
     */
    public String getPath() throws Exception {
        if(this.path == null) throw new Exception("Try to access config file before reading it.");
        return this.path;
    }

    /**
     * Constructor with zero parameter
     * Everything is set to null.
     */
    public ReadConfig(){
        this.fileLocation = null;
        this.fileName = null;
        this.path = null;
    }

    /**
     * Method that reads the file with all the settings.
     * The file's name is hardcoded as "graph_setting.json".
     * @throws Exception If the file is not available, not well formatted or the settings are not all coded an exception
     * is raised
     */
    public void readFile() throws Exception {
        //config file has to be located in the same directory as the program is
        String currentPath = Paths.get(".").toAbsolutePath().normalize().toString() + "/graph_setting.json";
        //file is a json file, need to parse it and than I can read it
        FileReader reader;
        try {
            reader = new FileReader(currentPath);
        } catch (FileNotFoundException e) {
            throw new Exception("Config file not found.");
        }
        JSONParser parser = new JSONParser();
        JSONObject jsonObject;
        try {
            jsonObject = (JSONObject) parser.parse(reader);
        } catch (IOException | ParseException e) {
            throw new Exception("JSON file not well formatted.");
        }
        //reading the settings
        this.fileName = (String) jsonObject.get("fileName");
        if (this.fileName == null || this.fileName.isEmpty()) throw new Exception("FileName is missing.");
        this.fileLocation = (String) jsonObject.get("fileLocation");
        if (this.fileLocation == null || this.fileLocation.isEmpty()) throw new Exception("FileLocation is missing.");
        this.path = this.fileLocation + "/" + this.fileName;
    }


}
