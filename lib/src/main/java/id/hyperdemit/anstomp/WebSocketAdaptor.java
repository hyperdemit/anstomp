package id.hyperdemit.anstomp;

import com.google.gson.Gson;
import id.hyperdemit.anstomp.event.EventDispatcher;
import id.hyperdemit.anstomp.event.WebsocketClosingEvent;
import id.hyperdemit.anstomp.event.WebsocketFailureEvent;
import id.hyperdemit.anstomp.event.WebsocketOpenedEvent;
import okhttp3.*;

import java.util.concurrent.TimeUnit;

public class WebSocketAdaptor extends WebSocketListener {
    private final String url;
    private MessageHandler messageHandler;
    private final Option option;
    private final Debug debug;
    private final EventDispatcher dispatcher;
    private final Gson gson;
    private final Subscriber subscriber;

    public WebSocketAdaptor(String url, Container container) {
        this.url = url;
        dispatcher = container.require(EventDispatcher.class);
        debug = container.require(Debug.class);
        option = container.require(Option.class);
        gson = container.require(Gson.class);
        subscriber = container.require(Subscriber.class);
    }

    public Client create() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .build();

        Request.Builder builder = new Request.Builder();
        builder.url(url);
        if (option.containsKey(Option.KEY_AUTHORIZATION)) {
            builder.addHeader(Option.KEY_AUTHORIZATION, option.get(Option.KEY_AUTHORIZATION));
        }

        WebSocket webSocket = okHttpClient.newWebSocket(builder.build(), this);
        okHttpClient.dispatcher().executorService().shutdown();

        int maxWebsocketFrameSize = Integer.parseInt(option.getOrDefault("maxWebsocketFrameSize", String.valueOf(16 * 1024)));
        Client client = new Client(gson, webSocket, subscriber, maxWebsocketFrameSize, debug);
        this.messageHandler = new MessageHandler(gson, dispatcher, subscriber, client, debug);

        return client;
    }

    private void getInfo() {
        // check websocket is available to this server
        // if websocket not support return error "'disabled from server', 'websocket'"
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        iDebug("Websocket opened.");
        dispatcher.dispatch(new WebsocketOpenedEvent(webSocket));
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        messageHandler.handleMessage(text, webSocket);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        dispatcher.dispatch(new WebsocketFailureEvent(t));
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        iDebug(String.format("onClosed. code:%d, reason: %s.", code, reason));
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        dispatcher.dispatch(new WebsocketClosingEvent(code, reason));
        iDebug(String.format("onClosing. code:%d, reason: %s.", code, reason));
    }

    private void iDebug(String message) {
        debug.iPrint(this.getClass().getName(), message);
    }
}
