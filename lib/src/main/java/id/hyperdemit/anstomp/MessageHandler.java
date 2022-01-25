package id.hyperdemit.anstomp;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import id.hyperdemit.anstomp.event.*;
import okhttp3.WebSocket;

import java.util.ArrayList;
import java.util.List;

public class MessageHandler {
    private final Client client;
    private final Gson gson;
    private final EventDispatcher dispatcher;
    private final Subscriber subscriber;
    private final Debug debug;

    public MessageHandler(Gson gson, EventDispatcher dispatcher, Subscriber subscriber, Client client, Debug debug) {
        this.client = client;
        this.gson = gson;
        this.dispatcher = dispatcher;
        this.subscriber = subscriber;
        this.debug = debug;
    }

    public void handleMessage(String text, WebSocket webSocket) {
        // message open
        if (text.equals("o")) {
            iDebug("<<< o");
            dispatcher.dispatch(new WebsocketOpenEvent(webSocket));
        }
        // message heartbeat
        else if(text.equals("h")) {
            iDebug("<<< Heartbeat");
            dispatcher.dispatch(new WebsocketHeartbeatEvent(webSocket));
        }
        // other
        else {
            String type = text.substring(0, 1);
            String content = text.substring(1);
            List<Object> payload = gson.fromJson(content, new TypeToken<ArrayList<Object>>(){}.getType());

            if (type.equals("a")) {
                for(Object message: payload) {
                    frameHandler((String) message, webSocket);
                }
            } else if(type.equals("m")) {
                iDebug("Unhandled frame type m.");
            } else if(type.equals("c") && payload.size() == 2) {
                iDebug("Websocket message type c: " + payload.get(0) + payload.get(1));
            } else {
                iDebug("Unhandled message: " + text);
            }
        }
    }

    private void frameHandler(String message, WebSocket webSocket) {
        Frame frame = Frame.of(message);

        if (frame.isCommand(Frame.CONNECTED)) {
            dispatcher.dispatch(new WebsocketConnectedEvent(client, frame, webSocket));
        } else if(frame.isCommand(Frame.MESSAGE)) {
            String subscription = frame.getHeaders().get("subscription");
            if (null != subscription) {
                subscriber.invokeListener(subscription, frame);
            } else {
                iDebug("Unhandled message: " + message);
            }
        } else if(frame.isCommand(Frame.ERROR)) {
            iDebug(message);
        } else {
            iDebug("Unhandled frame: " + message);
        }
    }

    private void iDebug(String message) {
        debug.iPrint(this.getClass().getName(), message);
    }
}
