package gms;


import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

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
 * This class loads the graph of all the path from file.
 */
public class Loader {
    private ReadConfig conf; //configuration object containing location where to read the graph
    private static final Logger logger = Logger.getLogger(Loader.class.getName()); //logger for this class


    /**
     * Constructor with zero parameter
     * The config file is read.
     * @throws Exception If the reading of the config file encounter in problems an exception is raised
     */
    public Loader() throws Exception {
        this.conf = new ReadConfig();
        this.conf.readFile();

        //logger action
        ConsoleHandler consoleHandler = new ConsoleHandler();
        logger.addHandler(consoleHandler);
    }


    public void loadGraph() throws Exception {
        logger.log(Level.INFO, "Loading Graph routes...");
        //create File object for read the graphML
        Path path = Paths.get(this.conf.getPath());
        File file = path.toFile();

    }




}
