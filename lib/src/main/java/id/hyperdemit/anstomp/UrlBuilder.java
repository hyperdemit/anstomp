package id.hyperdemit.anstomp;

import id.hyperdemit.anstomp.exception.InvalidUrlException;

import java.net.URI;
import java.util.Random;

public class UrlBuilder {
    private final URI uri;
    private final String serverId;
    private final String sessionId;

    public UrlBuilder(String url, String serverId, String sessionId) {
        this.uri = URI.create(url);
        this.serverId = serverId;
        this.sessionId = sessionId;
    }

    public URI build() {
        if (!isValidScheme()) {
            throw new InvalidUrlException(String.format("Expected scheme ws or wss, but %s given.", uri.getScheme()));
        }

        return URI.create(uri.getScheme() + "://" + uri.getAuthority() + getPath() + getQmQuery());
    }

    private String getPath() {
        String path = uri.getPath();

        return path + (path.endsWith("/") ? "" : "/") + getServerId() + "/" + getSessionId() + "/websocket";
    }

    private String getQmQuery() {
        String query = uri.getQuery();

        return query != null ? "?" + query : "";
    }

    private boolean isValidScheme() {
        return uri.getScheme().equals("ws") || uri.getScheme().equals("wss");
    }

    private String getServerId() {
        if (null != serverId) {
            return serverId;
        }

        int max = 1000;
        int t = String.valueOf(max-1).length();
        int floor = (int) Math.floor(Math.random() * max);
        String p = new String(new char[t]).replace("\0", "0");
        String com = (p+floor);

        return new StringBuilder(com).reverse().substring(0, t);
    }

    private String getSessionId() {
        if (null != sessionId) {
            return sessionId;
        }

        Random r = new Random();
        String alphabet = "abcdefghijklmnopqrstuvwxyz012345";
        int maxLen = alphabet.length();
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            builder.append(alphabet.charAt(r.nextInt(maxLen)));
        }

        return builder.toString();
    }
}
