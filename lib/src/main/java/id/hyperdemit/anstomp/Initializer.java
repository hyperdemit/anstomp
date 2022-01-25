package id.hyperdemit.anstomp;

import com.google.gson.Gson;
import id.hyperdemit.anstomp.event.*;
import id.hyperdemit.anstomp.event.EventListener;

import java.util.*;
import java.util.stream.Collectors;

public class Initializer {
    private final Option option;
    private final Timer pingTimer = new Timer();
    private final Timer pongTimer = new Timer();

    public Initializer(Option option) {
        this.option = option;

        option.put("maxWebsocketFrameSize", String.valueOf(16 * 1024));
        // option.put("maxWebsocketFrameSize", String.valueOf(65536));
        option.put("acceptVersion", "1.2");
    }

    public Container init(Callback userCallback) {
        Gson gson = new Gson();
        EventDispatcher dispatcher = new EventDispatcher();
        Debug debug = new Debug(option.getOrDefault("debug", "no").equalsIgnoreCase("yes"));
        Container container = new Container(new Subscriber(), dispatcher, debug, gson, option);

        initEventListeners(userCallback, debug, dispatcher, gson);

        return container;
    }

    private void initEventListeners(Callback userCallback, Debug debug, EventDispatcher dispatcher, Gson gson) {
        long initTimeMillis = System.currentTimeMillis();

        dispatcher.addListener(WebsocketOpenedEvent.class, (EventListener<WebsocketOpenedEvent>) event -> {
            Map<String, String> headers = new HashMap<>();
            headers.put("accept-version", option.getOrDefault("acceptVersion", "1.2"));
            headers.put("heart-beat", option.getOrDefault("heartbeat", "10000,10000"));

            String frame = gson.toJson(List.of(new FrameConnect(headers).toString()));
            debug.iPrint(this.getClass().getName(), frame);
            event.getWebSocket().send(frame);
            event.stopPropagation();
        }, 1);

        dispatcher.addListener(WebsocketConnectedEvent.class, (EventListener<WebsocketConnectedEvent>) event -> {
            userCallback.onConnected(event.getClient());

            // heartbeat first index is outgoing and second index is incoming
            Map<String, String> headers = event.getFrame().getHeaders();
            List<Integer> clientHeartbeat = Arrays.stream(option.getOrDefault("heartbeat", "0,0").split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            List<Integer> serverHeartbeat = Arrays.stream(headers.getOrDefault("heart-beat", "0,0").split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());

            if (!(clientHeartbeat.get(0).equals(0) || serverHeartbeat.get(1).equals(0))) {
                int ttl = Math.max(clientHeartbeat.get(0), serverHeartbeat.get(1));
                debug.iPrint(Anstomp.class.getName(), String.format("send PING every %dms", ttl));
                pingTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        event.getWebSocket().send(Frame.BYTE_LF);
                        debug.iPrint(Anstomp.class.getName(), ">>> PING");
                    }
                }, new Date(), ttl);
            }

            if (!(clientHeartbeat.get(1).equals(0) || serverHeartbeat.get(0).equals(0))) {
                int ttl = Math.max(clientHeartbeat.get(1), serverHeartbeat.get(0));
                debug.iPrint(Anstomp.class.getName(), String.format("check PONG every %dms", ttl));
                pongTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        long delta = (System.currentTimeMillis() - initTimeMillis);

                        if (delta > (long) ttl * 2) {
                            debug.iPrint(Anstomp.class.getName(), String.format("did not receive server activity for the last %dms", delta));
                            event.getWebSocket().close(1000, "Normal closure");
                        }
                    }
                }, new Date(), ttl);
            }
        });

        dispatcher.addListener(WebsocketClosingEvent.class, (EventListener<WebsocketClosingEvent>) event -> {
            // stop ping pong timer
            pingTimer.cancel();
            pingTimer.purge();
            pongTimer.cancel();
            pongTimer.purge();

            debug.iPrint(Anstomp.class.getName(), "clean timer");
        });

        dispatcher.addListener(WebsocketFailureEvent.class, (EventListener<WebsocketFailureEvent>) event -> {
            debug.ePrint(this.getClass().getName(), "Websocket failure: " + event.getThrowable().getMessage());
            userCallback.onFailure(event.getThrowable());
        }, 1);
    }
}
