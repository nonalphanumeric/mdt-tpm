package org.example;

import io.jbotsim.core.Color;
import io.jbotsim.core.Message;
import io.jbotsim.core.Node;

public class MyNode extends Node {

    boolean informed;

    @Override
    public void onStart() {
        // JBotSim executes this method on each node upon initialization
        //set the icon of the node to "iconBlack.png"
        setIcon("src/main/resources/iconBlack.png");
        //change icon size
        setIconSize(20);
        informed = false;
    }

    @Override
    public void onSelection() {
        informed = true;
        setColor(Color.CYAN);
        sendAll(new Message("INFORMED"));
    }

    @Override
    public void onMessage(Message message) {
        if (message.getContent().equals("INFORMED") && !informed) {
            informed = true;
            setColor(Color.GREEN);
            sendAll(new Message("INFORMED"));
        }
    }
}

