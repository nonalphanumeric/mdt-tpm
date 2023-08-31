package org.example;

import io.jbotsim.core.Color;
import io.jbotsim.core.Message;
import io.jbotsim.core.Node;

import java.util.ArrayList;

public class SmartSTN extends Node {

    private Node parent = null;
    private final ArrayList<Integer> children = new ArrayList<Integer>();
    private int expectedMessage = 0;

    private String valset = "";


    public void reinit() {
        	this.parent = null;
        	this.children.clear();
        	this.expectedMessage = 0;
            this.valset = "";
    }
    @Override
    public void onStart() {
        setIcon("src/main/resources/iconBlack.png");
        setIconSize(20);
        System.out.println(this.getCommunicationRange());

    }

    @Override
    public void onSelection() {
        //only the root node executes onSelection

        this.parent = this;
        //get the number of neighbors
        int nbNeighbors = getNeighbors().size();
        this.expectedMessage = nbNeighbors;

        //for each neighbor, send a message to it
        for (Node neighbor : getNeighbors()) {
            //send a message to the neighbor
            //the message contains the id of the sender
            send(neighbor, new Message("GO:" + this.getID()));
        }
    }

    @Override
    public void onMessage(Message message) {
        //Message received is either GO or BACK
        //First we find the type of message by splitting the content
        String[] content = ((String) (message.getContent())).split(":");
        //check if its a GO message
        if (content[0].equals("GO")) {
            //Set color to red
            setColor(Color.RED);
            //if the process has already a parent, send a BACK message to the sender
            if (this.parent != null) {
                send(message.getSender(), new Message("BACK:" + "NULL"));
            }
            //else, set the sender as the parent, and expected message as neighbors -1
            else {
                this.parent = message.getSender();
                this.expectedMessage = getNeighbors().size() - 1;
                //if expectedMessage == 0
                if (this.expectedMessage == 0) {
                    //send a BACK message to the sender
                    setColor(Color.GREEN);
                    send(message.getSender(), new Message("BACK:" + ";" + this.getID() + ">" + this.parent.getID()));

                } else {
                    //for each neighbor, send a message to it
                    for (Node neighbor : getNeighbors()) {
                        //if not the sender
                        if (neighbor.getID() != message.getSender().getID()) {
                            //send a message to the neighbor
                            //the message contains the id of the sender
                            send(neighbor, new Message("GO:" + this.getID()));
                        }
                    }
                }
            }

        } else if (content[0].equals("BACK")) {
            expectedMessage = expectedMessage - 1;
            //check if the second argument is NULL
            if (!content[1].equals("NULL")) {
                //add the id of the sender to the children list
                children.add(message.getSender().getID());
                this.valset = content[1] + this.valset;
            }
            //if expectedMessage == 0
            if (this.expectedMessage == 0) {
                //add (id) to the valset
                this.valset = this.valset + ";" + this.getID() + ">" + this.parent.getID();
                //if the node is not the root (parent = id)
                if (this.parent != this) {
                    //send a BACK message to the parent
                    //set color to green
                    setColor(Color.GREEN);

                    send(this.parent, new Message("BACK:" + this.valset));
                } else {
                    //set color to blue
                    setColor(Color.BLUE);
                    //print the valset
                    //send a broadcast CANDRAW message with the valset as content

                    send(getNeighbors().get(0), new Message("CANDRAW:" + this.valset));
                }

            }


        } else {
            //sSystem.out.println("Unknown message type");
        }
    }
}