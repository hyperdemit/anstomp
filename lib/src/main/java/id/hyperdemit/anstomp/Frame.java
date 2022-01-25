package id.hyperdemit.anstomp;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Frame {
    public static final String CONNECT = "CONNECT";
    public static final String CONNECTED = "CONNECTED";
    public static final String SEND = "SEND";
    public static final String MESSAGE = "MESSAGE";
    public static final String SUBSCRIBE = "SUBSCRIBE";
    public static final String UNSUBSCRIBE = "UNSUBSCRIBE";
    public static final String UNKNOWN = "UNKNOWN";
    public static final String ERROR = "ERROR";

    public static final String TERMINATE_MESSAGE = "\u0000";
    public static final String BYTE_LF = "\n";
    public static final String BYTE_NULL = "\u0000";

    private final String command;
    private Map<String, String> headers = new HashMap<>();
    private String body;

    public Frame(String command) {
        this.command = command;
    }

    public Frame(String command, Map<String, String> headers) {
        this.command = command;
        this.headers = headers;
    }

    public Frame(String command, Map<String, String> headers, String body) {
        this.command = command;
        this.headers = headers;
        this.body = body;
    }

    public static Frame of(String strFrame) {
        if (null == strFrame || strFrame.trim().isEmpty()) {
            return new Frame(UNKNOWN);
        }

        Pattern patternHeader = Pattern.compile("([^:\\s]+)\\s*:\\s*([^:\\s]+)");
        Scanner scanner = new Scanner(new StringReader(strFrame.replaceAll("[\\n\\r]+", BYTE_LF)));
        scanner.useDelimiter(BYTE_LF);
        String command = scanner.next();
        Map<String, String> headers = new HashMap<>();
        while (scanner.hasNext(patternHeader)) {
            Matcher matcher = patternHeader.matcher(scanner.next());
            if (matcher.find()) {
                headers.put(matcher.group(1), matcher.group(2));
            }
        }

        scanner.skip(BYTE_LF);
        scanner.useDelimiter(BYTE_NULL);
        String body = scanner.hasNext() ? scanner.next() : null;

        return new Frame(command, headers, body);
    }

    public boolean isCommand(String name) {
        return command.equals(name);
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(command);
        builder.append(BYTE_LF);
        for (Map.Entry<String, String> header : headers.entrySet()) {
            builder.append(header.getKey()).append(':').append(header.getValue()).append(BYTE_LF);
        }

        builder.append(BYTE_LF);
        if (body != null) {
            builder.append(body);
            // builder.append("\n\n");
        }
        builder.append(BYTE_NULL);

        return builder.toString();
    }
}
