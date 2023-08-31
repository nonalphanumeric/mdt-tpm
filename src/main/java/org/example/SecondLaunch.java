package org.example;

import java.io.IOException;

public class SecondLaunch {
    public static void main(String[] args) throws IOException, InterruptedException {
        //Launch LaunchBatch and wait for it to finish

        String[] args2 = {};
        for(int i = 99; i <= 100; i++){
            //LaunchBatch lb = new LaunchBatch(Integer.toString(i), Integer.toString(100));
            LaunchBatch lb = new LaunchBatch("2","20");
            lb.start();
            //wait for the thread to finish
            lb.join();

        }
        //quit
        return;

    }
}
