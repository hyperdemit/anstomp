package id.hyperdemit.anstomp;

import java.util.HashMap;

public class Option extends HashMap<String, String> {
    public static final String KEY_AUTHORIZATION = "Authorization";
    public static final String KEY_SERVER_ID = "serverId";
    public static final String KEY_SESSION_ID = "sessionId";

    public Option() {
        super();

        put("heartbeat", "10000,10000");
        put("debug", "no");
    }
}
