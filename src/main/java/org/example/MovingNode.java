package org.example;

import io.jbotsim.core.Node;

public class MovingNode extends Node {

    private boolean aggregate = false;
    private double speed = 1;

    @Override
    public void onStart() {
        setDirection(Math.random() * 2 * Math.PI);
        setCommunicationRange(100);
    }

    @Override
    public void onClock() {

        move(speed);
        if (getX() < 0 || getX() > getTopology().getWidth())
            setDirection(Math.PI - getDirection());
        if (getY() < 0 || getY() > getTopology().getHeight())
            setDirection(-getDirection());
    }



    //compute the distance vector between this node and another
    public double[] distanceVector(Node other) {
        double[] distanceVector = new double[2];
        distanceVector[0] = other.getX() - this.getX();
        distanceVector[1] = other.getY() - this.getY();
        return distanceVector;
    }


    // Generic getter and setter section
    public boolean isAggregate() {
        return aggregate;
    }

    public void setAggregate(boolean aggregate) {
        this.aggregate = aggregate;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
}

