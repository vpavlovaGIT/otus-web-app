package ru.otus.vpavlova.web.app.application.processors;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.vpavlova.web.app.HttpRequest;
import ru.otus.vpavlova.web.app.application.Item;
import ru.otus.vpavlova.web.app.application.Storage;

public class CreateNewProductProcessor implements RequestProcessor {
    private static final Logger logger = LogManager.getLogger(CreateNewProductProcessor.class);

    @Override
    public void execute(HttpRequest httpRequest, OutputStream output) throws IOException {
        Gson gson = new Gson();
        try {
            Item item = gson.fromJson(httpRequest.getBody(), Item.class);
            boolean isSaved = Storage.save(item);

            if (isSaved) {
                String jsonOutItem = gson.toJson(item);
                String response = "HTTP/1.1 200 OK\r\nContent-Type: application/json\r\n\r\n" + jsonOutItem;
                output.write(response.getBytes(StandardCharsets.UTF_8));
                logger.info("New product creation request processed successfully");
            } else {
                String errorResponse = "HTTP/1.1 500 Internal Server Error\r\nContent-Type: text/plain\r\n\r\nFailed to save the new product";
                output.write(errorResponse.getBytes(StandardCharsets.UTF_8));
                logger.error("Failed to save the new product");
            }
        } catch (JsonSyntaxException e) {
            String errorResponse = "HTTP/1.1 400 Bad Request\r\nContent-Type: text/plain\r\n\r\nError processing the request: " + e.getMessage();
            output.write(errorResponse.getBytes(StandardCharsets.UTF_8));
            logger.error("Error processing new product creation request: " + e.getMessage());
        }
    }
}
