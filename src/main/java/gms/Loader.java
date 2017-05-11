package gms;

/**
 * Created by alessandrozonta on 11/05/2017.
 * This class loads the graph of all the path from file.
 */
public class Loader {
    private ReadConfig conf; //configuration object containing location where to read the graph

    /**
     * Constructor with zero parameter
     * The config file is read.
     * @throws Exception If the reading of the config file encounter in problems an exception is raised
     */
    public Loader() throws Exception {
        this.conf = new ReadConfig();
        this.conf.readFile();
    }


}
