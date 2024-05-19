package ru.otus.vpavlova.web.app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpRequest {
    private String rawRequest;
    private String uri;
    private HttpMethod method;
    private Map<String, String> parameters;
    private String body;

    public String getRouteKey() {
        return String.format("%s %s", method, uri);
    }

    public String getUri() {
        return uri;
    }

    public String getParameter(String key) {
        return parameters.get(key);
    }

    public String getBody() {
        return body;
    }

    public HttpRequest(String rawRequest) {
        this.rawRequest = rawRequest;
        this.parseRequestLine();
        if (method != HttpMethod.GET) {
            this.tryToParseBody();
        }
    }

    public void tryToParseBody() {
        if (method.isCanHaveBody()) {
            List<String> lines = rawRequest.lines().collect(Collectors.toList());
            int splitLine = -1;
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).isEmpty()) {
                    splitLine = i;
                    break;
                }
            }
            if (splitLine > -1) {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = splitLine + 1; i < lines.size(); i++) {
                    stringBuilder.append(lines.get(i));
                }
                this.body = stringBuilder.toString();
            }
        }
    }

    public void parseRequestLine() {
        int startIndex = rawRequest.indexOf(' ');
        if (startIndex == -1) {
            System.out.println("Ошибка: не найден пробел в начале строки запроса.");
            return;
        }

        int endIndex = rawRequest.indexOf(' ', startIndex + 1);
        if (endIndex == -1) {
            System.out.println("Ошибка: не найден второй пробел в строке запроса.");
            return;
        }

        this.uri = rawRequest.substring(startIndex + 1, endIndex);

        try {
            this.method = HttpMethod.valueOf(rawRequest.substring(0, startIndex));
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: некорректный метод HTTP.");
            return;
        }

        this.parameters = new HashMap<>();
        if (uri.contains("?")) {
            String[] elements = uri.split("[?]");
            this.uri = elements[0];

            if (elements.length > 1) {
                String[] keysValues = elements[1].split("&");
                for (String keyValue : keysValues) {
                    String[] pair = keyValue.split("=");
                    if (pair.length == 2) {
                        this.parameters.put(pair[0], pair[1]);
                    }
                }
            }
        }
    }

    public void info(boolean showRawRequest) {
        if (showRawRequest) {
            System.out.println(rawRequest);
        }
        System.out.println("URI: " + uri);
        System.out.println("HTTP-method: " + method);
        System.out.println("Parameters: " + parameters);
        System.out.println("Body: " + body);
    }
}
