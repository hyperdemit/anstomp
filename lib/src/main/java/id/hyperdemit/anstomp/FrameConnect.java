package id.hyperdemit.anstomp;

import java.util.Map;

public class FrameConnect extends Frame {
    public FrameConnect(Map<String, String> headers) {
        super(Frame.CONNECT, headers);
    }
}
