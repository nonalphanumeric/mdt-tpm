package org.example;

import io.jbotsim.core.Node;
import io.jbotsim.core.Message;

import java.util.List;

public class AggregatingNode extends Node {
    private static final double SAFETY_DISTANCE = 30;
    private static final double ATTRACTION_STRENGTH = 0.001;
    private static final double REPULSION_STRENGTH = 100;

    private double vx = 0;
    private double vy = 0;

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

        // Compute net force acting on this node
        double netForceX = 0;
        double netForceY = 0;
        for (Node neighbor : neighbors) {
            double distance = distance(neighbor);
            if (distance < SAFETY_DISTANCE) {
                // Repulsion force
                double repulsionForce = REPULSION_STRENGTH / (distance * distance);
                double angle = Math.atan2(getY() - neighbor.getY(), getX() - neighbor.getX());
                netForceX += repulsionForce * Math.cos(angle);
                netForceY += repulsionForce * Math.sin(angle);
            } else {
                // Attraction force
                double attractionForce = ATTRACTION_STRENGTH * (distance - SAFETY_DISTANCE);
                double angle = Math.atan2(neighbor.getY() - getY(), neighbor.getX() - getX());
                netForceX += attractionForce * Math.cos(angle);
                netForceY += attractionForce * Math.sin(angle);
            }
        }

        // Compute acceleration
        double ax = netForceX;
        double ay = netForceY;

        // Compute new velocity
        double timeStep = 1; // time step is 1 for simplicity
        vx += ax * timeStep;
        vy += ay * timeStep;

        // Compute new position
        double newX = getX() + vx * timeStep;
        double newY = getY() + vy * timeStep;

        // Move to new position
        setDirection(Math.atan2(newY - getY(), newX - getX()));
        move(distance(newX, newY));
    }

    @Override
    public void onMessage(Message message) {
        // JBotSim executes this method on a node every time it receives a message
    }
}
