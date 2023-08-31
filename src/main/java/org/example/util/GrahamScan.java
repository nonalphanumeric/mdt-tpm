package org.example.util;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;

public class GrahamScan {
    private static int orientation(Point p, Point q, Point r) {
        int val = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y);
        if (val == 0) return 0;
        return (val > 0) ? 1 : 2;
    }

    private static void swap(List<Point> points, int i, int j) {
        Point temp = points.get(i);
        points.set(i, points.get(j));
        points.set(j, temp);
    }

    private static Point getMinYPoint(List<Point> points) {
        Point minYPoint = points.get(0);
        for (int i = 1; i < points.size(); i++) {
            Point point = points.get(i);
            if (point.y < minYPoint.y || (point.y == minYPoint.y && point.x < minYPoint.x)) {
                minYPoint = point;
            }
        }
        return minYPoint;
    }

    private static double getDistance(Point p1, Point p2) {
        return Math.sqrt((p2.x - p1.x) * (p2.x - p1.x) + (p2.y - p1.y) * (p2.y - p1.y));
    }

    public static ArrayList<Point> getConvexHull(List<Point> points) {
        if (points.size() < 3) {
            throw new IllegalArgumentException("At least 3 points are required");
        }

        Point minYPoint = getMinYPoint(points);

        Comparator<Point> pointComparator = (p1, p2) -> {
            int orientation = orientation(minYPoint, p1, p2);
            if (orientation == 0) {
                return Double.compare(getDistance(minYPoint, p2), getDistance(minYPoint, p1));
            }
            return (orientation == 2) ? -1 : 1;
        };

        points.remove(minYPoint);
        Collections.sort(points, pointComparator);
        points.add(0, minYPoint);

        Stack<Point> stack = new Stack<>();
        stack.push(points.get(0));
        stack.push(points.get(1));
        for (int i = 2; i < points.size(); i++) {
            Point top = stack.pop();
            while (orientation(stack.peek(), top, points.get(i)) != 2) {
                top = stack.pop();
            }
            stack.push(top);
            stack.push(points.get(i));
        }

        ArrayList<Point> convexHull = new ArrayList<>();
        while (!stack.isEmpty()) {
            convexHull.add(stack.pop());
        }
        Collections.reverse(convexHull);
        //System.out.println(convexHull);
        return convexHull;
    }
}

