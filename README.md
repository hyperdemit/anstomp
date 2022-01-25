# Anstomp
Java stomp spring websocket

#### This current library version is unstable for now, but you can test and report bug in here. Thank u ^^

## Spring Boot (Backend)
``` java

@Configuration
@EnableWebSocketMessageBroker
class YourConfig implements WebSocketMessageBrokerConfigurer {
// ...

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/web-socket")
                // ...
                .withSockJS();
    }
    
// ...
}
```

## Example Usage

``` java

// ...
import id.hyperdemit.anstomp.Anstomp;
import id.hyperdemit.anstomp.Client;
import id.hyperdemit.anstomp.Callback;
 
 // ...
 
 Anstomp anstomp = new Anstomp.Builder()
                .url("ws://localhost:8080/web-socket/")
                .build();

        Client anstompClient = anstomp.connect(new Callback() {
            @Override
            public void onConnected(Client client) {
                client.send("/post/send-message", "{\"json key\": \"your json content\"}");

                String subId = client.subscribe("/topic/receive-message", frame -> {
                    System.out.println("receive from main app. Message is: " + frame.getBody());
                });

                client.unsubscribe(subId);

                // close connection
                client.close();
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });

        // Or use this for close connection
        anstompClient.close();

```
