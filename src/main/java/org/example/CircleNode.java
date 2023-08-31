package org.example;

import io.jbotsim.core.Node;
import io.jbotsim.core.Message;

import java.util.List;

public class CircleNode extends Node {
    private static final double CIRCLE_RADIUS = 100;
    private static final double ATTRACTION_STRENGTH = 0.001;

    @Override
    public void onStart() {
        // JBotSim executes this method on each node upon initialization
    }

    @Override
    public void onSelection() {
        // JBotSim executes this method on a selected node
    }

    @Override
    public void onClock() {
        // JBotSim executes this method on each node in each round
        List<Node> neighbors = getNeighbors();
        if (neighbors.isEmpty()) {
            return;
        }

        // Compute midpoint of neighbors
        double midX = 0;
        double midY = 0;
        for (Node neighbor : neighbors) {
            midX += neighbor.getX();
            midY += neighbor.getY();
        }
        midX /= neighbors.size();
        midY /= neighbors.size();

        // Compute desired angle for this node
        double angle = 0;
        for (Node neighbor : neighbors) {
            double neighborAngle = Math.atan2(neighbor.getY() - midY, neighbor.getX() - midX);
            angle += neighborAngle;
        }
        angle /= neighbors.size();

        // Compute desired position for this node in circle around midpoint
        double desiredX = midX + CIRCLE_RADIUS * Math.cos(angle);
        double desiredY = midY + CIRCLE_RADIUS * Math.sin(angle);

        // Compute direction towards desired position
        double dx = desiredX - getX();
        double dy = desiredY - getY();
        double direction = Math.atan2(dy, dx);

        // Set direction and move
        setDirection(direction);
        move(1);
    }

    @Override
    public void onMessage(Message message) {
        // JBotSim executes this method on a node every time it receives a message
    }
}

