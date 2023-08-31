package org.example;

import io.jbotsim.core.Color;
import io.jbotsim.core.Message;
import io.jbotsim.core.Node;
import io.jbotsim.core.Point;
import org.example.util.BaseAggregationNode;
import org.example.util.BlindAggregationNode;

import java.util.HashMap;
import java.util.Map;

public class MaliciousBlindAggregationNode extends BaseAggregationNode {

    private static final int HIGH_NEIGHBOR_COUNT = 100;

    private Map<Node, Point> positionToSend = new HashMap<>();

    @Override
    public void onStart() {
        super.onStart();
        //SET Color to red
        setColor(Color.RED);
        //print ID of this node
        System.out.println("ID: " + this.getID());
    }

    @Override
    public void onSelection() {
        super.onSelection();
    }



    @Override
    public void sendPositionMessage() {


        for (Node neighbor : getNeighbors()) {

            //get position in the positionToSend map
            Point position = positionToSend.get(neighbor);
            //if the position doesn't exist, generate a random one and add it to the map
            if (position == null){
                position = new Point(Math.random()*500, Math.random()*500);
                positionToSend.put(neighbor, position);
            }

            String messageContent = String.format("%f,%f,%d", position.getX(), position.getY(), HIGH_NEIGHBOR_COUNT);
            send(neighbor, messageContent);
        }
    }



}
