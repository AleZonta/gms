package gms.ClaxPreload;

import Connections.DatabaseVertexInfo;
import gms.GraphML.InfoNode;
import gms.LoadingSystem.Loader;
import gms.LoadingSystem.System;
import gms.Point.Coord;
import lgds.map.OsmosisLoader;
import lgds.trajectories.Point;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by Alessandro Zonta on 10/08/2017.
 * PhD Situational Analytics
 * <p>
 * Computational Intelligence Group
 * Computer Science Department
 * Faculty of Sciences - VU University Amsterdam
 * <p>
 * a.zonta@vu.nl
 *
 * class that implements the preload of the graph. It is checking, per every arc, what is there and saving into a db
 * the coordinate of the nodes and what is there
 *
 */
public class Preload {
    private DatabaseVertexInfo db;

    /**
     * Load the database
     * @throws FileNotFoundException if the db is not available
     */
    public Preload() throws FileNotFoundException {
        this.db = new DatabaseVertexInfo();
    }


    /**
     * Create the database
     * for every vertex it divides it into several parts
     * for every point it checkes if there is something close to there
     * it counts what is close and how many time there is that thing
     * that it saves in a db the start point, the end point and what there is in that arc
     * @throws Exception
     */
    public void elaborate() throws Exception {
        System loader = new Loader();
        loader.loadGraph();


        OsmosisLoader osm = new OsmosisLoader();
        this.db = new DatabaseVertexInfo(Boolean.TRUE);
        List<Point> points = osm.obtainCoordinates();

        Integer totalSize = ((Loader)loader).getEdgeSet().size();
        final Integer[] count = {0};


        osm.openDB();

        ((Loader)loader).getEdgeSet().forEach(vertex -> {

            InfoNode initialNode = vertex.getSource();
            InfoNode endNode = vertex.getTarget();
            Double dis = new Double(vertex.retDistance());
            //how many division?
            Integer division = 50;
            Double section = dis / division;
            Double distance = section;

            List<Coord> position = new ArrayList<>();

            while(distance <= dis) {
                Double x = initialNode.getLat() - ((distance * (initialNode.getLat() - endNode.getLat())) / dis);
                Double m = (endNode.getLon() - initialNode.getLon()) / (endNode.getLat() - initialNode.getLat());
                Double y = (m * (x - initialNode.getLat())) + initialNode.getLon();
                position.add(new Coord(x,y));
                //move second point
                distance+=section;
            }




            Map<String, Integer> details = new HashMap<>();
            //now I have all the position in a vertex
            //for all the position i need to find if there is something there
            position.forEach(coord -> {
                //having all the coordinates, now with the coordinate from the db I need to find the closest point
                List<Point> positions = this.findClosestPoints(points, coord);
                if(!positions.isEmpty()) {
                    positions.forEach(p -> {
                        String result = null;
                        try {
                            result = osm.findIfThereIsSomethingInPositionBis(p.getLatitude(), p.getLongitude());
                            if (result == null){
                                java.lang.System.out.println("Null, WTF?");
                                java.lang.System.out.println(result);
                                java.lang.System.out.println(p.getLatitude().toString() + " " + p.getLongitude().toString());
                            }else {
                                if (!result.contains("null")) {
                                    List<String> sections = Arrays.asList(result.split("///"));
                                    if (!details.containsKey(sections.get(1))) {
                                        details.put(sections.get(1), 1);
                                    } else {
                                        details.put(sections.get(1), details.get(sections.get(1)) + 1);
                                    }
                                }
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    });

                }
            });

            String data;
            //here in details I should have everything that the vertex has.
            if(!details.isEmpty()){
                data = this.mapToString(details);
            }else{
                // I have nothing
                data = "null";
            }

            this.db.insertData(initialNode.getLat(), initialNode.getLon(), endNode.getLat(), endNode.getLon(), data);

            count[0]++;
            this.progressPercentage(count[0], totalSize);
        });
        osm.closeDB();
    }


    /**
     * Convert a map to a string
     * @param map map to convert
     * @return string converted
     */
    public String mapToString(Map<String, Integer> map) {
        StringBuilder stringBuilder = new StringBuilder();

        for (String key : map.keySet()) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append("&");
            }
            Integer value = map.get(key);
            try {
                stringBuilder.append((key != null ? URLEncoder.encode(key, "UTF-8") : ""));
                stringBuilder.append("=");
                stringBuilder.append(value != null ? URLEncoder.encode(value.toString(), "UTF-8") : "");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("This method requires UTF-8 encoding support", e);
            }
        }

        return stringBuilder.toString();
    }


    /**
     * Convert string to map
     * @param input String to convert
     * @return map converted
     */
    public Map<String, Integer> stringToMap(String input) {
        Map<String, Integer> map = new HashMap<>();

        String[] nameValuePairs = input.split("&");
        for (String nameValuePair : nameValuePairs) {
            String[] nameValue = nameValuePair.split("=");
            try {
                map.put(URLDecoder.decode(nameValue[0], "UTF-8"), nameValue.length > 1 ? new Integer(URLDecoder.decode(
                        nameValue[1], "UTF-8")) : 0);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("This method requires UTF-8 encoding support", e);
            }
        }

        return map;
    }


    /**
     * Find real position closest to coordinate given
     * @param p list of real position
     * @param position position given
     * @return list of real position
     */
    private List<Point> findClosestPoints(List<Point> p, Coord position){
        List<Point> points = new ArrayList<>();
        p.forEach(point -> {
            if(position.distance(new Coord(point.getLatitude(),point.getLongitude())) < 0.005){
                points.add(point);
            }
        });
        return points;
    }

    /**
     * Print a progress bar
     * @param remain where I am now
     * @param total total amount
     */
    private void progressPercentage(int remain, int total) {
        if (remain > total) {
            throw new IllegalArgumentException();
        }
        int maxBareSize = 10; // 10unit for 100%
        int remainProcent = ((100 * remain) / total) / maxBareSize;
        char defaultChar = '-';
        String icon = "*";
        String bare = new String(new char[maxBareSize]).replace('\0', defaultChar) + "]";
        StringBuilder bareDone = new StringBuilder();
        bareDone.append("[");
        for (int i = 0; i < remainProcent; i++) {
            bareDone.append(icon);
        }
        String bareRemain = bare.substring(remainProcent, bare.length());
        java.lang.System.out.print("\r" + bareDone + bareRemain + " " + remainProcent * 10 + "%");
        if (remain == total) {
            java.lang.System.out.print("\n");
        }
    }


    /**
     * Get the element of the selected edge
     * @param lati latitude start
     * @param loni longitude start
     * @param late latitude end
     * @param lone longitude end
     * @return what there is in the edge selected
     */
    public Map<String, Integer> getElementEdge(Double lati, Double loni, Double late, Double lone){
        return this.stringToMap(this.db.readData(lati,loni,late,lone));
    }

}
