package id.hyperdemit.anstomp.event;

import okhttp3.WebSocket;

public class WebsocketOpenEvent extends Event {
    private final WebSocket webSocket;

    public WebsocketOpenEvent(WebSocket webSocket) {
        this.webSocket = webSocket;
    }

    public WebSocket getWebSocket() {
        return webSocket;
    }
}
