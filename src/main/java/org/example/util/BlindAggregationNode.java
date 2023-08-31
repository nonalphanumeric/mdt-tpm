package org.example.util;

import io.jbotsim.core.Color;
import io.jbotsim.core.Message;
import io.jbotsim.core.Node;
import io.jbotsim.core.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlindAggregationNode extends BaseAggregationNode {


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onSelection() {
        super.onSelection();
    }

    @Override
    public void sendPositionMessage() {
        Point position = new Point(getX(), getY());
        int numNeighbors = getNeighbors().size();
        String messageContent = String.format("%f,%f,%d", position.getX(), position.getY(), numNeighbors);
        sendAll(messageContent);
    }




}
