package id.hyperdemit.anstomp;

import java.util.HashMap;
import java.util.Map;

public class FrameSend extends Frame {
    public FrameSend(String destination, String body) {
        super(Frame.SEND);

        Map<String, String> headers = new HashMap<>();
        headers.put("destination", destination);
        headers.put("content-length", String.valueOf(body.length()));
        setHeaders(headers);
        setBody(body);
    }
}
