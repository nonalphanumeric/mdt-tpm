package org.example.util;

import io.jbotsim.core.Message;
import io.jbotsim.core.Node;
import io.jbotsim.core.Point;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseAggregationNode extends Node {

    private static final double PROXIMITY_THRESHOLD = 50; // distance to detect other robots
    private static final double WAIT_PROBABILITY = 0.4; // probability to switch to wait state
    private static final double PROBABILITY_THRESHOLD = 0.7; // threshold to switch to approach state
    private static final double SAFETY_DISTANCE = 40; // distance to keep from other robots

    protected abstract void sendPositionMessage();

    private enum State {
        RANDOM_WALK,
        WAIT,
        APPROACH
    }

    private State state = State.RANDOM_WALK;
    private int remainingTime = 0;
    private Map<Node, Point> knownNeighbors = new HashMap<>(); // list of known neighbors and their last known positions
    private Map<Node, Integer> neighborCounts = new HashMap<>(); // list of neighbor counts for each neighbor

    public void onStart() {
        setCommunicationRange(300);
    }
    @Override
    public void onSelection() {
        state = State.RANDOM_WALK;
        remainingTime = (int) (Math.random() * 50) + 50;
        setDirection(Math.random() * 2 * Math.PI);
    }

    @Override
    public void onClock() {
        sendPositionMessage(); // send position message to all neighbors
        updateKnownNeighbors(); // update the list of known neighbors

        // show states
        if (state == State.RANDOM_WALK) {
            remainingTime--;
            if (remainingTime <= 0) {
                // check if an aggregate is in proximity
                boolean hasAggregate = false;
                for (Node neighbor : knownNeighbors.keySet()) {
                    hasAggregate = true;
                    break;
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
                        remainingTime = (int) (Math.random() * 50) + 500;
                        setDirection(Math.random() * 2 * Math.PI);
                    }
                }
            } else {
                move(1);
            }
        } else if (state == State.WAIT) {
            remainingTime--;
            if (remainingTime <= 0) {
                // check if an aggregate is in proximity
                boolean hasAggregate = false;
                for (Node neighbor : knownNeighbors.keySet()) {
                    hasAggregate = true;
                    break;
                }
                if (hasAggregate && Math.random() < PROBABILITY_THRESHOLD) {
                    state = State.APPROACH;
                } else {
                    // continue the random walk
                    state = State.RANDOM_WALK;
                    remainingTime = (int) (Math.random() * 50) + 100;
                    setDirection(Math.random() * 2 * Math.PI);
                }
            }
        } else if (state == State.APPROACH) {
            // move towards the aggregate with the highest number of neighbors
            Node bestAggregate = null;
            int highestNumNeighbors = -1;
            for (Node neighbor : knownNeighbors.keySet()) {
                //print the ID of the neighbor
                if (neighbor != null) {
                    int numNeighbors = neighborCounts.getOrDefault(neighbor, 0);
                    if (numNeighbors > highestNumNeighbors) {
                        bestAggregate = neighbor;
                        highestNumNeighbors = numNeighbors;
                    }
                }
            }

            if (bestAggregate != null) {
                Point bestAggregatePosition = knownNeighbors.get(bestAggregate);

                setDirection(bestAggregatePosition);
                //print info about the aggregate
                System.out.println("ID: " + getID() + " bestAggregate: " + bestAggregate.getID() + " bestAggregatePosition: " + bestAggregatePosition + " numNeighbors: " + highestNumNeighbors);

                move(1);

                // check if the closest aggregate is too close, if so, enter WAIT state
                if (distance(bestAggregatePosition) < SAFETY_DISTANCE) {
                    state = State.WAIT;
                    remainingTime = (int) (Math.random() *10) + 50;
                }
            } else {
                // no aggregates in proximity, switch to random walk
                state = State.RANDOM_WALK;
                remainingTime = (int) (Math.random() * 50) + 100;
                setDirection(Math.random() * 2 * Math.PI);
            }
        }

    }



    private void updateKnownNeighbors() {
        knownNeighbors.clear();


        neighborCounts.clear();
        for (Message message : getMailbox()) {
            //Check if the message comes from ID:20

            if (message.getContent() instanceof String) {
                String messageContent = (String) message.getContent();
                String[] parts = messageContent.split(",");
                if (parts.length == 3) {
                    Point position = new Point(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]));
                    int numNeighbors = Integer.parseInt(parts[2]);
                    Node sender = message.getSender();
                    knownNeighbors.put(sender, position);
                    neighborCounts.put(sender, numNeighbors);
                    //print sender, position and numNeighbors


                }


            }
        }


    }


}