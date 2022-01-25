# Anstomp
### Android java stomp for connect with spring websocket
![](https://img.shields.io/badge/VERSION-0.1-informational?style=flat&logo=<LOGO_NAME>&logoColor=white&color=2bbc8a)

### <img src="https://raw.githubusercontent.com/MartinHeinz/MartinHeinz/master/wave.gif" width="17px"> Hello World
This library is built based on the requirement in my project. Might also be useful in my next project. If any of you need it, you can use it **for free**. Thank u ^^

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
                .withSockJS(); // <===
    }
    
// ...
}
```

## Example Usage
For Android Note: Write this code outside of your main thread.

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

## Installation
I am sorry. For now it **can only be used** using **JAR files**. You can import the jar file in your project.
If you are too busy to build the jar file from this repository, you can download it [**here**](https://github.com/hyperdemit/anstomp/releases).