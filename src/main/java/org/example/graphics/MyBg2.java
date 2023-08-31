package org.example.graphics;

import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.ui.painting.UIComponent;
import io.jbotsim.ui.painting.BackgroundPainter;
import org.example.util.GrahamScan;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MyBg2 implements BackgroundPainter {
    @Override
    public void paintBackground(UIComponent c, Topology tp) {
        Graphics2D g = (Graphics2D) c.getComponent();
        g.setColor(Color.GRAY);

        // Step 1: Get the nodes from the topology
        Node[] nodes = tp.getNodes().toArray(new Node[0]);

        // Step 2: Convert the nodes to points
        ArrayList<Point> points = new ArrayList<>();
        for (Node node : nodes) {
            points.add(new Point((int) node.getX(), (int) node.getY()));
        }

        // Step 3: Compute the convex hull if there are at least three points
        if (points.size() >= 3) {
            ArrayList<Point> convexHullPoints = GrahamScan.getConvexHull(points);

            // Step 4: Draw the convex hull polygon
            int[] xPoints = new int[convexHullPoints.size()];
            int[] yPoints = new int[convexHullPoints.size()];
            for (int i = 0; i < convexHullPoints.size(); i++) {
                Point p = convexHullPoints.get(i);
                xPoints[i] = p.x;
                yPoints[i] = p.y;
            }
            g.setColor(Color.RED);
            g.drawPolygon(xPoints, yPoints, convexHullPoints.size());
        }
    }
}




