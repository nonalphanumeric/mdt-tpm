package org.example;


import io.jbotsim.core.*;
import io.jbotsim.core.event.MessageListener;
import io.jbotsim.gen.basic.generators.RandomLocationsGenerator;
import io.jbotsim.gen.basic.generators.TopologyGenerator;
import io.jbotsim.ui.JViewer;

import java.io.IOException;
import java.util.Random;

public class Main {


    public static void main(String[] args) throws IOException {
        Topology tp = new Topology(711,400);




        tp.setNodeModel("SpanningTreeNode", SpanningTree.class);
        tp.setNodeModel("MaliciousSTN", MaliciousSTN.class);

        tp.setTimeUnit(600);

        MessageRecorder mr = new MessageRecorder(tp);

        tp.addClockListener(mr);


        JViewer jv = new JViewer(tp);

        //add an anonymous message listener
        tp.addMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                String[] content = ((String) message.getContent()).split(":");
                //if the message is a "CANDRAW" message
                if (content[0].equals("CANDRAW")) {
                    //Parse the message content to get pairs of nodes
                    String[] pairs = content[1].split(";");
                    //each pair is in the form "id1->id2"
                    //the link between id1 and id2 is redrawn but in red
                    for (String pair : pairs) {
                        //discard empty strings
                        if (pair.length() == 0)
                            continue;
                        String[] nodes = pair.split(">");
                        //get the nodes from the topology
                        Node n1 = this.getNodeFromTopology(tp, Integer.parseInt(nodes[0]));
                        Node n2 = this.getNodeFromTopology(tp, Integer.parseInt(nodes[1]));
                        //if the nodes are found
                        if (n1 != null && n2 != null) {
                            //get the link between the two nodes (n1, n2) return null, try (n2, n1)
                            Link l = tp.getLink(n1, n2);
                            //if the link is found
                            if (l != null) {
                                //set the color of the link to red
                                l.setColor(Color.RED);
                                l.setWidth(5);
                            }
                        }

                    }
                }
            }

            private Node getNodeFromTopology(Topology tp, int parseInt) {
                for (Node n : tp.getNodes()) {
                    if (n.getID() == parseInt) {
                        return n;
                    }
                }
                return null;
            }
        });
        tp.start();
    }
}