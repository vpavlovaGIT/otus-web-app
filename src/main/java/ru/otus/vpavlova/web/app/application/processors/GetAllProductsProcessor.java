package ru.otus.vpavlova.web.app.application.processors;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.vpavlova.web.app.HttpRequest;
import ru.otus.vpavlova.web.app.application.Item;
import ru.otus.vpavlova.web.app.application.Storage;

public class GetAllProductsProcessor implements RequestProcessor {
    private static final Logger logger = LogManager.getLogger(GetAllProductsProcessor.class);
    @Override
    public void execute(HttpRequest httpRequest, OutputStream output) throws IOException {
        List<Item> items = Storage.getItems();
        Gson gson = new Gson();
        String result = "HTTP/1.1 200 OK\r\nContent-Type: application/json\r\n\r\n" + gson.toJson(items);
        output.write(result.getBytes(StandardCharsets.UTF_8));
        logger.info("Processing request to get all products");
    }
}
