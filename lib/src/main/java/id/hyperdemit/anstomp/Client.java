package id.hyperdemit.anstomp;

import com.google.gson.Gson;
import okhttp3.WebSocket;

import java.util.List;

public class Client {
    private final Gson gson;
    private final WebSocket webSocket;
    private final Subscriber subscriber;
    private final int maxWebsocketFrameSize;
    private final Debug debug;

    public Client(Gson gson, WebSocket webSocket, Subscriber subscriber, int maxWebsocketFrameSize, Debug debug) {
        this.webSocket = webSocket;
        this.gson = gson;
        this.subscriber = subscriber;
        this.maxWebsocketFrameSize = maxWebsocketFrameSize;
        this.debug = debug;
    }

    public void send(String destination, String data) {
        transit(new FrameSend(destination, data).toString());
    }

    public String subscribe(String destination, SubscribeListener listener) {
        String id = Utils.strRandomTimeMs("sub");
        transit(new FrameSubscribe(destination, id).toString());
        subscriber.addListener(id, listener);

        return id;
    }

    public void unsubscribe(String id) {
        transit(new FrameUnsubscribe(id).toString());
        subscriber.removeListener(id);
    }

    private boolean transit(String frame) {
        String jFrame = gson.toJson(List.of(frame));
        debug.iPrint("Frame Transit", ">>> length: " + jFrame.length());

        while (true) {
            if (jFrame.length() > maxWebsocketFrameSize) {
                webSocket.send(jFrame.substring(0, maxWebsocketFrameSize));
                jFrame = jFrame.substring(maxWebsocketFrameSize);
                debug.iPrint("Frame Transit", "remaining = " + jFrame.length());
            } else{
                return webSocket.send(jFrame);
            }
        }
    }

    public void close() {
        webSocket.close(1000, "Close by client.");
    }
}
