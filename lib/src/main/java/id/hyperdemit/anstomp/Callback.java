package id.hyperdemit.anstomp;

public interface Callback {
    void onConnected(Client client);
    void onFailure(Throwable t);
}
