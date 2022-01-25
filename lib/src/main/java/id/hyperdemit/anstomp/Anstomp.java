package id.hyperdemit.anstomp;

import java.net.URI;

public class Anstomp {
    private final String url;
    private final Option option;

    public Anstomp(String url, Option option) {
        this.url = url;
        this.option = option;
    }

    public Client connect(Callback callback) {
        Container container = new Initializer(option).init(callback);
        return new WebSocketAdaptor(createURI().toString(), container).create();
    }

    private URI createURI() {
        return new UrlBuilder(url, option.get(Option.KEY_SERVER_ID), option.get(Option.KEY_SESSION_ID)).build();
    }

    public static class Builder {
        private String url;
        private final Option option = new Option();

        public Builder() {}

        public Builder url(String url) {
            this.url = url;

            return this;
        }

        public Builder authorization(String authorization) {
            option.put(Option.KEY_AUTHORIZATION, authorization);

            return this;
        }

        public Builder sessionId(String id) {
            option.put(Option.KEY_SESSION_ID, id);

            return this;
        }

        public Builder serverId(String id) {
            option.put(Option.KEY_SERVER_ID, id);

            return this;
        }

        public Builder heartbeat(int in, int out) {
            option.put("heartbeat", out + "," + in);

            return this;
        }

        public Builder debug(boolean enable) {
            option.put("debug", enable ? "yes" : "no");

            return this;
        }

        public Anstomp build() {
            return new Anstomp(url, option);
        }
    }
}
