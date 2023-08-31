package org.example;

import io.jbotsim.core.Node;
import io.jbotsim.core.Point;
import io.jbotsim.core.Topology;

import java.util.ArrayList;

public class HullShowerTopology extends Topology {

    public HullShowerTopology() {
        super();
    }

    @Override
    public void onClock() {
        super.onClock();
        //Create an empty list of position
        //if more than two nodes are present
        if(getNodes().size() < 4) return;
        ArrayList<Point> points = new ArrayList<>();
        //Get all nodes
        for (Node node : getNodes()) {
            //Compute the convex hull
            points.add(new Point(node.getX(), node.getY()));
        }

        ArrayList<Point> res = ConvexHull.computeConvexHull(points);
        System.out.println(res);
    }
}
