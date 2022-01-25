package id.hyperdemit.anstomp.event;

public class WebsocketFailureEvent extends Event{
    private final Throwable throwable;

    public WebsocketFailureEvent(Throwable throwable) {
        this.throwable = throwable;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}
