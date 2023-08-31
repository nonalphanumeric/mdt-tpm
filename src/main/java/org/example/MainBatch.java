package org.example;


import io.jbotsim.core.*;
import io.jbotsim.core.event.MessageListener;
import io.jbotsim.ui.JViewer;

import java.io.IOException;
import java.util.ArrayList;

public class MainBatch {


    public static void main(String[] args) throws IOException {
        //args are in the form: nbMalicious, nbNodes
        //parse them
        int nbMalicious = Integer.parseInt(args[0]);
        int nbNodes = Integer.parseInt(args[1]);

        Topology tp = new Topology(400,400);
        tp.setNodeModel("SpanningTreeNode", SpanningTree.class);
        tp.setNodeModel("MaliciousSTN", MaliciousSTN.class);

        int x;
        int y;


        tp.setNodeModel("SpanningTreeNode", SpanningTree.class);
        tp.setNodeModel("MaliciousSTN", MaliciousSTN.class);

        //if the first args is "malicious", create a malicious node

        for(int i = 0; i < nbMalicious; i++){
            x = (int) (Math.random() * 400);
            y = (int) (Math.random() * 400);
            tp.addNode(x,y, new MaliciousSTN());
        }



        //make 20 nodes at random location
        ArrayList<SpanningTree> nodes = new ArrayList<SpanningTree>();
        for(int i = 0; i < nbNodes; i++) {
            x = (int) (Math.random() * 400);
            y = (int) (Math.random() * 400);
            SpanningTree node = new SpanningTree();
            tp.addNode(x, y, node);
            nodes.add(node);
        }


        tp.setTimeUnit(100);



        //add an anonymous message listener
        tp.addMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                String[] content = ((String) message.getContent()).split(":");
                //if the message is a "CANDRAW" message
                if (content[0].equals("CANDRAW")) {
                    //Parse the message content to get pairs of nodes
                    String[] pairs = content[1].split(";");

                    //filter empty strings in the array
                    ArrayList<String> filteredPairs = new ArrayList<String>();
                    for (String pair : pairs) {
                        if (pair.length() > 0) {
                            filteredPairs.add(pair);
                        }

                    }
                    System.out.println(filteredPairs.size());


                }

            }
        });

        //on a random node, the first of the list, do onSelection
        JViewer jv = new JViewer(tp);

        tp.start();
        nodes.get(0).onSelection();

    }
}