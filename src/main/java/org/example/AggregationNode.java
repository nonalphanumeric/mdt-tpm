package org.example;

import io.jbotsim.core.Node;
import io.jbotsim.core.Point;

import java.util.ArrayList;
import java.util.List;

public class AggregationNode extends Node {

    private static final double PROXIMITY_THRESHOLD = 50; // distance to detect other robots
    private static final double WAIT_PROBABILITY = 0; // probability to switch to wait state
    private static final double PROBABILITY_THRESHOLD = 0.5; // threshold to switch to approach state
    private static final double SAFETY_DISTANCE = 25; // distance to keep from other robots
    private enum State {
        RANDOM_WALK,
        WAIT,
        APPROACH
    }

    private State state = State.RANDOM_WALK;
    private int remainingTime = 0;

    private List<Node> getNeighborsBelow(double distance) {
        List<Node> neighborsBelowDistance = new ArrayList<>();
        for (Node neighbor : getNeighbors()) {
            if (distance(neighbor) <= distance) {
                neighborsBelowDistance.add(neighbor);
            }
        }
        return neighborsBelowDistance;
    }


    @Override
    public void onSelection() {
        state = State.RANDOM_WALK;
        remainingTime = (int) (Math.random() * 50) + 50;
        setDirection(Math.random() * 2 * Math.PI);
    }

    @Override
    public void onClock() {
        // show states
        if (state == State.RANDOM_WALK) {
            remainingTime--;
            if (remainingTime <= 0) {
                // check if an aggregate is in proximity
                boolean hasAggregate = false;
                for (Node neighbor : getNeighborsBelow(PROXIMITY_THRESHOLD)) {
                    if (neighbor instanceof AggregationNode) {
                        hasAggregate = true;
                        break;
                    }
                }
                if (hasAggregate) {
                    state = State.APPROACH;
                } else {
                    // check if the node should switch to the wait state
                    if (Math.random() < WAIT_PROBABILITY) {
                        state = State.WAIT;
                        remainingTime = (int) (Math.random() * 100) + 50;
                    } else {
                        // continue the random walk
                        remainingTime = (int) (Math.random() * 50) + 50;
                        setDirection(Math.random() * 2 * Math.PI);
                    }
                }
            }
            else{
                move(1);
            }
        } else if (state == State.WAIT) {
            remainingTime--;
            if (remainingTime <= 0) {
                // check if an aggregate is in proximity
                boolean hasAggregate = false;
                for (Node neighbor : getNeighborsBelow(PROXIMITY_THRESHOLD)) {
                    if (neighbor instanceof AggregationNode) {
                        hasAggregate = true;
                        break;
                    }
                }
                if (hasAggregate && Math.random() < PROBABILITY_THRESHOLD) {
                    state = State.APPROACH;
                } else {
                    // continue the random walk
                    state = State.RANDOM_WALK;
                    remainingTime = (int) (Math.random() * 50) + 50;
                    setDirection(Math.random() * 2 * Math.PI);
                }
            }
        } else if (state == State.APPROACH) {
            // move towards the closest aggregate
            Node closestAggregate = null;
            double closestDistance = Double.MAX_VALUE;
            for (Node neighbor : getNeighborsBelow(PROXIMITY_THRESHOLD)) {
                if (neighbor instanceof AggregationNode) {
                    double distance = distance(neighbor);
                    if (distance < closestDistance) {
                        closestAggregate = neighbor;
                        closestDistance = distance;
                    }
                }
            }
            if (closestAggregate != null) {
                Point closestAggregatePosition = new Point(closestAggregate.getX(), closestAggregate.getY());
                setDirection(closestAggregatePosition);
                move(1);
                // check if the closest aggregate is too close, if so, enter WAIT state
                if (closestDistance < SAFETY_DISTANCE) {
                    state = State.WAIT;
                    remainingTime = (int) (Math.random() * 50 * getNeighbors().size()) + 50;

                }

            } else {
                // no aggregates in proximity, switch to random walk
                state = State.RANDOM_WALK;
                remainingTime = (int) (Math.random() * 50) + 50;
                setDirection(Math.random() * 2 * Math.PI);
            }
        }
    }
}
