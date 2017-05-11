package gms;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * Created by alessandrozonta on 11/05/2017.
 * This class reads the config file with the info needed by the program
 */
public class ReadConfig {
    private String fileName;
    private String fileLocation;

    /**
     * Method that returns the location of the file containing the graph
     * @return String with the path
     */
    public String getFileLocation() {
        return this.fileLocation;
    }

    /**
     * Method that returns the name of the file containing the graph
     * @return String with the name
     */
    public String getFileName() {
        return this.fileName;
    }

    /**
     * Constructor with zero parameter
     * Everything is set to null.
     */
    public ReadConfig(){
        this.fileLocation = null;
        this.fileName = null;
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
    }
}
