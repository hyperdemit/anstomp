package id.hyperdemit.anstomp;

import java.util.HashMap;
import java.util.Map;

public class FrameSubscribe extends Frame {
    public FrameSubscribe(String destination, String id) {
        super(Frame.SUBSCRIBE);

        Map<String, String> headers = new HashMap<>();
        headers.put("id", id);
        headers.put("destination", destination);
        setHeaders(headers);
    }
}
