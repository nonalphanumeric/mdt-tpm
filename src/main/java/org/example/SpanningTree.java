package org.example;

import io.jbotsim.core.Color;
import io.jbotsim.core.Message;
import io.jbotsim.core.Node;

import java.util.ArrayList;
import java.util.List;

public class SpanningTree extends Node {

    // The parent node of this node in the spanning tree
    private Node parent = null;

    // The list of children nodes in the spanning tree
    private final List<Integer> children = new ArrayList<>();

    // The number of "BACK" messages this node is expecting to receive
    private int expectedMessageCount = 0;

    // The set of values associated with this node in the spanning tree
    private String valSet = "";

    @Override
    public void onStart() {
        // Set the icon and icon size of this node

        // Print the communication range of this node
        System.out.println(getCommunicationRange());
    }

    @Override
    public void onSelection() {
        // This method is only called on the root node

        // Set the parent of the root node to itself
        parent = this;

        // Get the number of neighbors of this node
        int neighborCount = getNeighbors().size();

        // Set the expected message count to the number of neighbors
        expectedMessageCount = neighborCount;

        // For each neighbor, send a "GO" message
        for (Node neighbor : getNeighbors()) {
            send(neighbor, new Message("GO:" + getID()));
        }
    }

    @Override
    public void onMessage(Message message) {
        // This method is called when a message is received

        // Split the message content into parts
        String[] content = ((String) message.getContent()).split(":");

        // Check the type of message
        if (content[0].equals("GO")) {
            // If this is a "GO" message:

            // Set the color of this node to red
            setColor(Color.RED);

            if (parent != null) {
                // If this node already has a parent, send a "BACK" message with null content
                send(message.getSender(), new Message("BACK:NULL"));
            } else {
                // Otherwise, set the sender as the parent, and expected message count to neighbors - 1
                parent = message.getSender();
                expectedMessageCount = getNeighbors().size() - 1;

                if (expectedMessageCount == 0) {
                    // If expected message count is zero, this is a leaf node

                    // Set the color of this node to green
                    setColor(Color.GREEN);

                    // Add the parent-child relationship to the valSet
                    StringBuilder sb = new StringBuilder();
                    sb.append(";");
                    sb.append(getID());
                    sb.append(">");
                    sb.append(parent.getID());
                    valSet = sb.toString();

                    // Send a "BACK" message to the parent
                    send(parent, new Message("BACK:" + valSet));
                } else {
                    // Otherwise, send a "GO" message to each neighbor
                    for (Node neighbor : getNeighbors()) {
                        if (neighbor.getID() != message.getSender().getID()) {
                            send(neighbor, new Message("GO:" + getID()));
                        }
                    }
                }
            }
        } else if (content[0].equals("BACK")) {
            // If this is a "BACK" message:

            // Decrease the expected message count
            expectedMessageCount--;

            if (!content[1].equals("NULL")) {
                // If the second part of the message is not null, this is not a leaf node

                // Add the parent-child relationship and the valSet of the child to the valSet of the parent
                StringBuilder sb = new StringBuilder();
                sb.append(content[1]);
                sb.append(valSet);
                valSet = sb.toString();

                // Add the child node to the list of children
                children.add(message.getSender().getID());
            }

            if (expectedMessageCount == 0) {
                // If expected message count is zero, all "BACK" messages have been received

                // Add the parent-child relationship of this node to the valSet
                StringBuilder sb = new StringBuilder();
                sb.append(";");
                sb.append(getID());
                sb.append(">");
                sb.append(parent.getID());
                valSet = sb.toString() + valSet;

                if (parent != this) {
                    // If this node is not the root:

                    // Set the color of this node to green
                    setColor(Color.GREEN);

                    // Send a "BACK" message to the parent with the updated valSet
                    send(parent, new Message("BACK:" + valSet));
                } else {
                    // If this node is the root:

                    // Set the color of this node to blue
                    setColor(Color.BLUE);

                    // Send a "CANDRAW" message with the valSet to one of the neighbors
                    send(getNeighbors().get(0), new Message("CANDRAW:" + valSet));
                }
            }
        } else {
            // Unknown message type
        }
    }
}



