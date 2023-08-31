package org.example;

import io.jbotsim.core.Message;
import io.jbotsim.core.Node;

public interface NodeStrategy {
    public void onStart(Node originNode);
    public void onSelection(Node originNode);
    public void onMessage(Message message, Node originNode);
    public void onClock(Node originNode);
}
