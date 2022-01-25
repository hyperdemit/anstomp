package id.hyperdemit.anstomp.event;

public class WebsocketClosingEvent extends Event {
    private final int code;
    private final String reason;

    public WebsocketClosingEvent(int code, String reason) {
        this.code = code;
        this.reason = reason;
    }

    public int getCode() {
        return code;
    }

    public String getReason() {
        return reason;
    }
}
