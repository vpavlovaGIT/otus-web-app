package ru.otus.vpavlova.web.app.application.processors;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.vpavlova.web.app.HttpRequest;

public class UnknownOperationRequestProcessor implements RequestProcessor {
    private static final Logger logger = LogManager.getLogger(UnknownOperationRequestProcessor.class);
    @Override
    public void execute(HttpRequest httpRequest, OutputStream output) throws IOException {
        String response = "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n<html><body><h1>UNKNOWN OPERATION REQUEST!!!</h1></body></html>";
        output.write(response.getBytes(StandardCharsets.UTF_8));
        logger.warn("Unknown operation requested");
        logger.trace("Some trace information");
        logger.error("Error handling unknown operation");
    }
}
