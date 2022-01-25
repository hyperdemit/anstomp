package id.hyperdemit.anstomp.event;

import okhttp3.WebSocket;

public class WebsocketHeartbeatEvent extends Event {
    private final WebSocket webSocket;

    public WebsocketHeartbeatEvent(WebSocket webSocket) {
        this.webSocket = webSocket;
    }

    public WebSocket getWebSocket() {
        return webSocket;
    }
}
