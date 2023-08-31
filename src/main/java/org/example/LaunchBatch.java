package org.example;

import java.io.FileNotFoundException;
import java.io.IOException;

public class LaunchBatch extends Thread {
    String nbMalicious;
    String nbNodes;

    public LaunchBatch(String nbMalicious, String nbNodes){
        this.nbMalicious = nbMalicious;
        this.nbNodes = nbNodes;
    }
    public void run() {

        //args in the form: nbMalicious, nbNodes
        //parse them
        String nbMalicious = this.nbMalicious;
        String nbNodes = this.nbNodes;


        //outputfile is in the form: out_nbMalicious_nbNodes_random5digit number

        String outputFile = "out_" +  nbMalicious + "_" + nbNodes + "_" + (int)(Math.random()*100000);

        //redirect stdout to a file
        try {
            System.setOut(new java.io.PrintStream(new java.io.FileOutputStream(outputFile)));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        //launch MainBatch with the first argument "malicious" x times
        for (int i = 0; i < 100; i++) {
            String[] args2 = {nbMalicious, nbNodes};
            try {
                MainBatch.main(args2);
                //wait for the thread to finish
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        //wait for the last thread to finish
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return;
    }
}
