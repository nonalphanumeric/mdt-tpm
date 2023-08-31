package org.example.util;

import io.jbotsim.core.*;
import org.example.MaliciousBlindAggregationNode;
import org.example.graphics.MyBg2;

import java.io.IOException;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class AggregateTopology extends Topology {

    private static final int SAMPLER_FREQUENCY = 10;
    private int clockCounter = 0;
    private Logger logger;

    //make a dictionnary that maps distance to length of link (5 from 0 to 10, 4 from 10 to 20, etc)
    private Map<Integer, Integer> linkLengths = new HashMap<>();

    public AggregateTopology() {
        super(711,400);
        //set background color


        //do to mapping of length
        for (int i = 0; i < 100; i++) {
            if(i < 10 && i >= 0){
                linkLengths.put(i, 5);
            }
            else if(i < 20 && i >= 10){
                linkLengths.put(i, 4);
            }
            else if(i < 30 && i >= 20){
                linkLengths.put(i, 3);
            }
            else if(i < 40 && i >= 30){
                linkLengths.put(i, 2);
            }
            else if(i < 50 && i >= 40){
                linkLengths.put(i, 1);
            }
            else if(i < 60 && i >= 50){
                linkLengths.put(i, 0);
            }
            else if(i < 70 && i >= 60){
                linkLengths.put(i, 1);
            }
            else if(i < 80 && i >= 70){
                linkLengths.put(i, 2);
            }
            else if(i < 90 && i >= 80){
                linkLengths.put(i, 3);
            }
            else if(i < 100 && i >= 90){
                linkLengths.put(i, 4);
            }
        }

        initLogger();
    }

    private void initLogger() {
        try {
            logger = Logger.getLogger("AggregateTopology");
            //ADD A timestamp to the file name
            String s = "AggregateTopology-";
            String timestamp = new java.text.SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new java.util.Date());
            String extension = ".csv";
            s = s + timestamp + extension;
            FileHandler fileHandler = new FileHandler(s);
            fileHandler.setFormatter(new CsvFormatter());  // use CsvFormatter to format the log records
            logger.addHandler(fileHandler);
            // Log the header
            logger.info("clock;node_id;position");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to initialize logger", e);
        }
    }

    @Override
    public void onClock() {
        super.onClock();
        //for all nodes, avoid getting out of the topology by reversing direction
        for (Node node : getNodes()) {
            if (node.getX() < 0 || node.getX() > getWidth())
                node.setDirection(node.getDirection() + Math.PI);
            if (node.getY() < 0 || node.getY() > getHeight())
                node.setDirection(node.getDirection() + Math.PI);
        }
        clockCounter++;
        if (clockCounter % SAMPLER_FREQUENCY == 0) {
            sample();
        }
        for (Link link : getLinks()) {
            Node source = link.source;
            Node destination = link.destination;
            int distance = (int) source.distance(destination);
            //if distance is not in the dictionnary, set it to 0
            int width = linkLengths.getOrDefault(distance, 0);

            link.setWidth(width);
        }
        /*
        //if clockCounter is greater than 2000, remove all instance of MaliciousBlindAggregationNode
        if(clockCounter > 2000){
            for (Node node : getNodes()) {
                if(node instanceof MaliciousBlindAggregationNode){
                    removeNode(node);
                }
            }
        }*/
    }

    public void sample() {
        List<Node> nodes = getNodes();
        for (Node node : nodes) {

            int totalNodes = nodes.size();
            //get position of node
            Point position = node.getLocation();
            //Create a string with the position as "x:y"
            String positionString = position.getX() + ":" + position.getY();


            String message = String.format("%d;%d;%s", clockCounter, node.getID(), positionString);
            logger.info(message);

        }
    }


}