package ru.otus.vpavlova.web.app;

public enum HttpMethod {
    GET(false),
    POST(true),
    PUT(true),
    DELETE(false),
    PATCH(true);

    private final boolean canHaveBody;

    HttpMethod(boolean canHaveBody) {
        this.canHaveBody = canHaveBody;
    }

    public boolean isCanHaveBody() {
        return canHaveBody;
    }
}

