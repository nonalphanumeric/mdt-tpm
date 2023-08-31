package org.example;

/*
    * This class aims to provide the interaction function between two nodes in aggregation
    * It is used in the aggregation algorithm
    *
    * it gets the distance between two nodes, and then compute the interaction
 */
public class InteractionComputer {
    //start
    public static double computeInteraction(MovingNode node1, MovingNode node2, double a, double b, double c) {
        //interaction should be a function of the distance between the two nodes
        //a is the force of attraction, b the force and repulsion, and it should cancel at value distance c

        //compute the distance between the two nodes
        double[] distanceVector = node1.distanceVector(node2);
        double distance = Math.sqrt(Math.pow(distanceVector[0], 2) + Math.pow(distanceVector[1], 2));
        //interaction value is a function of the distance, it is equal to the attraction minus repulsion
        //attraction and repulsion are linear functions of the distance

        //Attraction is proportional to distance
        double attraction = a * distance;

        //Repulsion starts high at 0 and decreases linearly with distance
        double repulsion = b * (c - distance);

        //compute the interaction
        double interaction = attraction - repulsion;

        return interaction;

    }


}
