package ru.otus.vpavlova.web.app;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import ru.otus.vpavlova.web.app.processors.CalculatorRequestProcessor;
import ru.otus.vpavlova.web.app.processors.HelloWorldRequestProcessor;
import ru.otus.vpavlova.web.app.processors.RequestProcessor;
import ru.otus.vpavlova.web.app.processors.UnknownOperationRequestProcessor;

public class Dispatcher {
    private Map<String, RequestProcessor> router;
    private RequestProcessor unknownOperationRequestProcessor;

    public Dispatcher() {
        this.router = new HashMap<>();
        this.router.put("/calc", new CalculatorRequestProcessor());
        this.router.put("/hello", new HelloWorldRequestProcessor());
        this.unknownOperationRequestProcessor = new UnknownOperationRequestProcessor();
    }

    public void execute(HttpRequest httpRequest, OutputStream outputStream) throws IOException {
        if (!router.containsKey(httpRequest.getUri())) {
            unknownOperationRequestProcessor.execute(httpRequest, outputStream);
            return;
        }
        router.get(httpRequest.getUri()).execute(httpRequest, outputStream);
    }
}
