package id.hyperdemit.anstomp.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventDispatcher {
    private final Map<String, Map<Integer, List<EventListener<? extends Event>>>> listeners = new HashMap<>();

    public void addListener(Class<? extends Event> event, EventListener<? extends Event> listener, int priority) {
        if (listeners.containsKey(event.getName())) {
            if (listeners.get(event.getName()).containsKey(priority)) {
                listeners.get(event.getName()).get(priority).add(listener);
            } else {
                List<EventListener<? extends Event>> list = new ArrayList<>();
                list.add(listener);
                listeners.get(event.getName()).put(priority, list);
            }
        } else {
            List<EventListener<? extends Event>> l = new ArrayList<>();
            Map<Integer, List<EventListener<? extends Event>>> p = new HashMap<>();
            l.add(listener);
            p.put(priority, l);
            listeners.put(event.getName(), p);
        }
    }

    public void addListener(Class<? extends Event> event, EventListener<? extends Event> listener) {
        addListener(event, listener, Event.DEFAULT_PRIORITY);
    }

    @SuppressWarnings("unchecked")
    public <T extends Event> void dispatch(T event) {
        String eventName = event.getClass().getName();
        if (listeners.containsKey(eventName)) {
            for(Map.Entry<Integer, List<EventListener<? extends Event>>> p: listeners.get(eventName).entrySet()) {
                for (EventListener listener: p.getValue()) {
                    listener.onEvent(event);

                    if (event.isPropagationStopped()) {
                        return;
                    }
                }
            }
        }
    }
}
