package org.example;

import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;
import org.example.graphics.MyBg;
import org.example.graphics.MyBg2;
import org.example.util.AggregateTopology;
import org.example.util.BlindAggregationNode;

public class SimpleMain {


    public static void main(String[] args){
        AggregateTopology tp = new AggregateTopology();
        tp.setDefaultNodeModel(BlindAggregationNode.class);

        //set clock speed
        tp.setTimeUnit(5);
        //add 20 nodes at random location in topology
        for (int i = 0; i < 20; i++) {
            double x = Math.random() * tp.getWidth();
            double y = Math.random() * tp.getHeight();
            tp.addNode(x, y);
        }
        //add 1 MaliciousBlindAggregationNode
        //double x = Math.random() * tp.getWidth();
        //double y = Math.random() * tp.getHeight();
        //tp.addNode(x,y, new MaliciousBlindAggregationNode());
        //tp.setSensingRange(100);


        JViewer jv = new JViewer(tp);



        tp.start();

    }
}
