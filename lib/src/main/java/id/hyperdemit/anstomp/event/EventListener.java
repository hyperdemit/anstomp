package id.hyperdemit.anstomp.event;

public interface EventListener<T extends Event> {
    void onEvent(T event);
}
