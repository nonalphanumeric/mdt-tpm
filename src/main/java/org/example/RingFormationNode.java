package org.example;

import io.jbotsim.core.Color;
import io.jbotsim.core.Message;
import io.jbotsim.core.Node;

import java.util.ArrayList;

public class RingFormationNode extends Node {

    private Node leftNeighbor;
    private Node rightNeighbor;
    private boolean isInitiator = false;
    private boolean hasToken = false;
    private boolean isDone = false;

    @Override
    public void onStart() {
        // set the initial color of the node
        setColor(Color.BLUE);
    }

    @Override
    public void onSelection() {
        // set the node as the initiator
        isInitiator = true;
        hasToken = true;
        setColor(Color.RED);

        // send the token message to the right neighbor
        send(rightNeighbor, new Message("TOKEN"));
    }

    @Override
    public void onMessage(Message message) {
        //cast message content to String
        String content = (String) message.getContent();
        System.out.println("Node " + getID() + " received a message from node " + message.getSender().getID() + " with content " + message.getContent());
        if (content.equals("TOKEN")) {
            if (isInitiator && !hasToken) {
                // If the node is the initiator and hasn't received the token yet,
                // it means the ring is complete and the election can start
                System.out.println("Ring formation completed. Starting election.");
                hasToken = true;
                send(rightNeighbor, new Message("ELECTION:" + getID()));
            } else {
                // pass the token to the right neighbor
                hasToken = false;
                send(rightNeighbor, new Message("TOKEN"));
            }
        } else if (content.startsWith("ELECTION:")) {
            // start the election process
            int initiatorId = Integer.parseInt(content.substring(9));
            if (getID() == initiatorId) {
                // the node is the initiator, it means the election has completed
                isDone = true;
                System.out.println("Election completed. Node " + getID() + " is the leader.");
                return;
            }
            // if the node hasn't received the election message yet, pass it on to the right neighbor
            if (!content.contains(getID() + "")) {
                send(rightNeighbor, new Message("ELECTION:" + message.getContent() + "->" + getID()));
            } else {
                // if the node has already received the election message, discard it
                return;
            }
        }
    }

    public void onNeighborUpdate() {
        // initialize the left and right neighbors
        ArrayList<Node> neighbors = (ArrayList<Node>) getNeighbors();
        int numNeighbors = neighbors.size();
        int myIndex = neighbors.indexOf(this);
        leftNeighbor = neighbors.get((myIndex - 1 + numNeighbors) % numNeighbors);
        rightNeighbor = neighbors.get((myIndex + 1) % numNeighbors);
    }

    @Override
    public void onClock() {

        // if the election is done, stop the simulation
        if (isDone) {
            System.out.println("Simulation stopped.");
        }
    }
}
