package org.example;

import io.jbotsim.core.Point;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Stack;

public class ConvexHull {

    public static ArrayList<Point> computeConvexHull(ArrayList<Point> points) {
        // sort the points by their x-coordinate
        points.sort(Comparator.comparingDouble(Point::getX));
        
        // create the lower hull
        Stack<Point> lowerHull = new Stack<>();
        for (Point p : points) {
            while (lowerHull.size() >= 2 && isCounterClockwise(lowerHull.get(lowerHull.size() - 2), lowerHull.peek(), p)) {
                lowerHull.pop();
            }
            lowerHull.push(p);
        }
        
        // create the upper hull
        Stack<Point> upperHull = new Stack<>();
        for (int i = points.size() - 1; i >= 0; i--) {
            Point p = points.get(i);
            while (upperHull.size() >= 2 && isCounterClockwise(upperHull.get(upperHull.size() - 2), upperHull.peek(), p)) {
                upperHull.pop();
            }
            upperHull.push(p);
        }
        
        // combine the lower and upper hulls to form the convex hull
        ArrayList<Point> hull = new ArrayList<>(lowerHull);
        hull.addAll(upperHull.subList(1, upperHull.size()));
        
        return hull;
    }

    private static boolean isCounterClockwise(Point a, Point b, Point c) {
        double area2 = (b.getX() - a.getX()) * (c.getY() - a.getY()) - (b.getY() - a.getY()) * (c.getX() - a.getX());
        return area2 > 0;
    }
}

