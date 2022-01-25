package id.hyperdemit.anstomp.event;

public abstract class Event {
    public static final int DEFAULT_PRIORITY = 2;
    private boolean propagation = false;
    public boolean isPropagationStopped() {
        return propagation;
    }
    public void stopPropagation() {
        propagation = true;
    }
}
