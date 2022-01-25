package id.hyperdemit.anstomp;

import java.util.HashMap;
import java.util.Map;

public class Subscriber {
    private final Map<String, SubscribeListener> subscriptions = new HashMap<>();

    public void addListener(String id, SubscribeListener listener) {
        subscriptions.put(id, listener);
    }

    public void removeListener(String id) {
        subscriptions.remove(id);
    }

    public void invokeListener(String id, Frame frame) {
        if (subscriptions.containsKey(id)) {
            subscriptions.get(id).onMessage(frame);
        }
    }
}
