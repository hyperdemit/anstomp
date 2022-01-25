package id.hyperdemit.anstomp.event;

import id.hyperdemit.anstomp.Client;
import id.hyperdemit.anstomp.Frame;
import okhttp3.WebSocket;

public class WebsocketConnectedEvent extends Event {
    private final Client client;
    private final Frame frame;
    private final WebSocket webSocket;

    public WebsocketConnectedEvent(Client client, Frame frame, WebSocket webSocket) {
        this.client = client;
        this.frame = frame;
        this.webSocket = webSocket;
    }

    public Client getClient() {
        return client;
    }

    public Frame getFrame() {
        return frame;
    }

    public WebSocket getWebSocket() {
        return webSocket;
    }
}
