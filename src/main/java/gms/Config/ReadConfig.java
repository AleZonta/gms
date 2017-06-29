package gms.Config;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

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
    private List<String> fileName;
    private String fileLocation;
    private List<String> path;
    private Boolean usingMoreSources;

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
    public List<String> getFileName() throws Exception {
        if(this.fileName == null) throw new Exception("Try to access config file before reading it.");
        return this.fileName;
    }

    /**
     * Method that returns the complete path of the file
     * path is the combination of file location and file name
     * @return String with the path
     * @param position number of path to retrieve
     */
    public String getPath(Integer position) throws Exception {
        if(this.path == null) throw new Exception("Try to access config file before reading it.");
        return this.path.get(position);
    }

    /**
     * Constructor with zero parameter
     * Everything is set to null.
     */
    public ReadConfig(){
        this.fileLocation = null;
        this.fileName = new ArrayList<>();
        this.path = null;
        this.usingMoreSources = null;
    }

    /**
     * Method that reads the file with all the settings.
     * The file's name is hardcoded as "graph_setting.json".
     * @throws Exception If the file is not available, not well formatted or the settings are not all coded an exception
     * is raised
     */
    public void readFile() throws Exception {
        //config file has to be located in the same directory as the program is
        String currentPath = Paths.get(".").toAbsolutePath().normalize().toString() + "/settings.json";
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
        JSONArray names = (JSONArray) jsonObject.get("fileName");
        names.forEach(el -> this.fileName.add((String) el));
        if(this.fileName.size() == 1){
            this.usingMoreSources = Boolean.FALSE;
        } else {
            this.usingMoreSources = Boolean.TRUE;
        }

        if (this.fileName == null || this.fileName.isEmpty()) throw new Exception("FileName is missing.");
        this.fileLocation = (String) jsonObject.get("fileLocation");
        if (this.fileLocation == null || this.fileLocation.isEmpty()) throw new Exception("FileLocation is missing.");
        //path is saying only first location
        this.path = new ArrayList<>();
        IntStream.range(0, this.fileName.size()).forEach(i -> this.path.add(this.fileLocation + "/" + this.fileName.get(i)));

    }

    /**
     * Override to string method
     * @return string version of the config file
     */
    @Override
    public String toString() {
        return "ReadConfig{" + "\n" +
                "fileName='" + fileName.toString() + '\'' + ",\n" +
                "fileLocation='" + fileLocation + '\'' + ",\n" +
                "path='" + path + '\'' + "\n" +
                '}';
    }

    /**
     * If there are more than one name that means I will use more source location, therefore this variable is true
     * @return Boolean variable
     */
    public Boolean getUsingMoreSources() throws Exception {
        if(this.usingMoreSources == null) throw new Exception("Try to access config file before reading it.");
        return this.usingMoreSources;
    }
}
