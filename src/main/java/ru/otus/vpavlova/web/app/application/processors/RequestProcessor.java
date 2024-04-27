package ru.otus.vpavlova.web.app.application.processors;

import java.io.IOException;
import java.io.OutputStream;
import ru.otus.vpavlova.web.app.HttpRequest;

public interface RequestProcessor {
    void execute(HttpRequest httpRequest, OutputStream output) throws IOException;
}
