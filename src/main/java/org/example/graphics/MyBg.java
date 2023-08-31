package org.example.graphics;

import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.ui.painting.UIComponent;
import io.jbotsim.ui.painting.BackgroundPainter;
import org.example.util.GrahamScan;

import java.awt.*;
import java.util.ArrayList;

public class MyBg implements BackgroundPainter {
    @Override
    public void paintBackground(UIComponent c, Topology tp) {
        Graphics2D g = (Graphics2D) c.getComponent();
        g.setColor(Color.GRAY);

        // Step 1: Get the nodes from the topology
        Node[] nodes = tp.getNodes().toArray(new Node[0]);

        // Step 2: Convert the nodes to points
        ArrayList<Point> points = new ArrayList<Point>();
        for (Node node : nodes) {
            Point point = new Point((int) node.getX(), (int) node.getY());
            points.add(point);
        }

        // Step 3: Compute the convex hull if there are at least three points
        if (points.size() >= 3) {
            ArrayList<Point> convexHullPoints = GrahamScan.getConvexHull(points);
            System.out.println(convexHullPoints);
            // Step 4: Draw the edges of the convex hull
            g.setColor(Color.RED);
            for (int i = 0; i < convexHullPoints.size(); i++) {
                Point p1 = convexHullPoints.get(i);
                Point p2 = convexHullPoints.get((i + 1) % convexHullPoints.size());
                g.drawLine(p1.x, p1.y, p2.x, p2.y);
            }

        }
    }

}