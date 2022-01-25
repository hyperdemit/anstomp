package id.hyperdemit.anstomp.event;

import okhttp3.WebSocket;

public class WebsocketOpenedEvent extends Event {
    private final WebSocket webSocket;

    public WebsocketOpenedEvent(WebSocket webSocket) {
        this.webSocket = webSocket;
    }

    public WebSocket getWebSocket() {
        return webSocket;
    }
}
