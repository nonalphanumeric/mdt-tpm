package org.example;

import io.jbotsim.core.Message;
import io.jbotsim.core.Topology;
import io.jbotsim.core.event.ClockListener;
import io.jbotsim.core.event.MessageListener;

import java.io.FileWriter;
import java.io.IOException;

public class MessageRecorder implements MessageListener, ClockListener {

    private final String filename = "MessageRecorder" + System.currentTimeMillis() + ".txt";
    private int clockCounter = 0;

    public MessageRecorder(Topology tp) throws IOException {
        tp.addMessageListener(this);

        //create or overwrite the file
        try {
            FileWriter writer = new FileWriter(this.filename);
            writer.write("");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessage(Message message) {
        //open the file in append mode
        FileWriter writer = null;
        try {

            writer = new FileWriter(this.filename, true);


            //write a line string in this format
            //[TIMESTAMP] [CLOCK_CYCLE] [SENDER_NODE_ID] [RECEIVER_NODE_ID] [MESSAGE_CONTENT]
            //Format [TIMESTAMP] like this: [2022-12-19T10:15:30.456789Z]
            //Format [CLOCK_CYCLE] like this: [CC:1]
            //Format [SENDER_NODE_ID] like this: [SD:1]
            //Format [RECEIVER_NODE_ID] like this: [DST:2]
            //Format [MESSAGE_CONTENT] like this: [MC:INFORMED]

            String line = String.format("[%s] [CC:%d] [SD:%d] [DST:%d] [MC:%s]%n",
                    java.time.Instant.now(),
                    this.clockCounter,
                    message.getSender().getID(),
                    message.getDestination().getID(),
                    message.getContent());

            //write the line in the file
            writer.write(line);
            writer.close();


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onClock() {
        this.clockCounter++;
    }

}

