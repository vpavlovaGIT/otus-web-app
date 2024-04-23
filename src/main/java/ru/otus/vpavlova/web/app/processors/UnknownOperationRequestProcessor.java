package ru.otus.vpavlova.web.app.processors;

import java.io.IOException;
import java.io.OutputStream;
import ru.otus.vpavlova.web.app.HttpRequest;

public class UnknownOperationRequestProcessor implements RequestProcessor {
    @Override
    public void execute(HttpRequest httpRequest, OutputStream outputStream) throws IOException {
        String response = "HTTP/1.1 404 Not Found\r\nContent-Type: text/html\r\n\r\n<h1>Not Found</h1>";
        outputStream.write(response.getBytes());
    }
}
