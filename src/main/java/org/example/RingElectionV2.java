package org.example;

import io.jbotsim.core.Color;
import io.jbotsim.core.Message;
import io.jbotsim.core.Node;

import java.util.ArrayList;

public class RingElectionV2 extends Node {

    private int leaderID = -1;
    private int largestID = -1;
    private boolean started = false;
    private int nbNeighbors;
    private int receivedMessage = 0;

    @Override
    public void onStart() {
        setIconSize(20);
        nbNeighbors = getNeighbors().size();
    }

    @Override
    public void onSelection() {
        started = true;
        leaderID = -1;
        receivedMessage = 0;
        largestID = this.getID();

        // Send message to the right neighbor
        Node neighbor = getRightNeighbor();
        send(neighbor, new Message(this.getID()));
    }

    private Node getRightNeighbor() {
        Node rightNeighbor = null;
        double smallestDistance = Double.MAX_VALUE;

        for (Node neighbor : getNeighbors()) {
            double dx = neighbor.getX() - getX();
            double dy = neighbor.getY() - getY();
            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance < smallestDistance) {
                smallestDistance = distance;
                rightNeighbor = neighbor;
            }
        }

        return rightNeighbor;
    }

    @Override
    public void onMessage(Message message) {
        System.out.println("Node " + this.getID() + " received a message from node " + message.getSender().getID() + " with content " + message.getContent());
        if (message.getContent() instanceof Integer) {
            int content = (int) message.getContent();

            if (content > largestID) {
                largestID = content;
            }

            if (receivedMessage == nbNeighbors - 1) {
                if (this.getID() == largestID) {
                    leaderID = this.getID();
                    setColor(Color.GREEN);
                    System.out.println("Node " + this.getID() + " is the leader.");
                } else {
                    Node neighbor = getRightNeighbor();
                    send(neighbor, new Message(largestID));
                }
            } else {
                Node neighbor = getRightNeighbor();
                send(neighbor, new Message(content));
            }

            receivedMessage++;
        }
    }

    @Override
    public void onClock() {
        if (started && leaderID == -1) {
            if (receivedMessage == nbNeighbors) {
                if (this.getID() == largestID) {
                    leaderID = this.getID();
                    setColor(Color.GREEN);
                    System.out.println("Node " + this.getID() + " is the leader.");
                } else {
                    Node neighbor = getRightNeighbor();
                    send(neighbor, new Message(largestID));
                }
            }
        }
    }
}