package id.hyperdemit.anstomp;

import java.util.HashMap;
import java.util.Map;

public class FrameUnsubscribe extends Frame {
    public FrameUnsubscribe(String id) {
        super(Frame.UNSUBSCRIBE);

        Map<String, String> headers = new HashMap<>();
        headers.put("id", id);
        setHeaders(headers);
    }
}
