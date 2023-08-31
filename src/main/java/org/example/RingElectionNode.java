package org.example;

import io.jbotsim.core.Color;
import io.jbotsim.core.Message;
import io.jbotsim.core.Node;

import java.util.*;

public class RingElectionNode extends Node {

    private boolean hasElectedLeader = false;
    private int leaderID = -1;

    @Override
    public void onStart() {
        setIcon("src/main/resources/iconBlack.png");
        setIconSize(20);
    }

    @Override
    public void onSelection() {
        //change the color of the node to red
        setColor(Color.RED);
        // Send an election message to the right neighbor
        List<Node> neighbors = getNeighbors();
        Node rightNeighbor = neighbors.get(0);
        send(rightNeighbor, new Message("ELECTION:" + this.getID()));
    }

    @Override
    public void onMessage(Message message) {
        String[] content = ((String) message.getContent()).split(":");
        String messageType = content[0];
        int senderID = Integer.parseInt(content[1]);
        System.out.println("Node " + this.getID() + " received a message of type " + messageType + " from node " + senderID);

        if (messageType.equals("ELECTION")) {
            // If the message comes back to the original sender, the election is over
            if (senderID == this.getID()) {
                this.hasElectedLeader = true;
                this.leaderID = this.getID();
                System.out.println("Node " + this.getID() + " has elected itself as leader.");
                return;
            }

            int candidateID = senderID;
            int currentLeaderID = this.leaderID;
            if (candidateID > currentLeaderID) {
                this.leaderID = candidateID;
            }

            // Forward the election message to the right neighbor
            List<Node> neighbors = getNeighbors();
            Node rightNeighbor = neighbors.get(0);
            send(rightNeighbor, new Message("ELECTION:" + this.leaderID));
        }
    }

    @Override
    public void onClock() {
        // If the election is over, print the leader
        if (this.hasElectedLeader) {
            System.out.println("Node " + this.getID() + " has elected node " + this.leaderID + " as leader.");
        }
    }
}
