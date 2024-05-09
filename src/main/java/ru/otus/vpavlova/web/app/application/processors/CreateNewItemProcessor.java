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

/**
 * Обработчик запроса для создания новых продуктов.
 */
public class CreateNewItemProcessor implements RequestProcessor {

    private static final Logger logger = LogManager.getLogger(CreateNewItemProcessor.class);

    /**
     * Метод execute обрабатывает POST запросы для создания одного или нескольких продуктов (Items).
     *
     * @param httpRequest HTTP запрос с информацией о новых продуктах.
     * @param output выходной поток для отправки ответа.
     * @throws IOException если возникают проблемы с чтением или записью данных.
     */
    @Override
    public void execute(HttpRequest httpRequest, OutputStream output) throws IOException {
        Gson gson = new Gson();
        try {
            if (httpRequest.getBody().startsWith("[")) {
                Item[] newItemsArray = gson.fromJson(httpRequest.getBody(), Item[].class);

                Storage storage = new Storage();
                boolean allSaved = true;

                for (Item newItem : newItemsArray) {
                    if (!storage.save(newItem)) {
                        allSaved = false;
                        break;
                    }
                }

                if (allSaved) {
                    String createdItemsJson = gson.toJson(newItemsArray);
                    String response = "HTTP/1.1 201 Created\r\nContent-Type: application/json\r\n\r\n" + createdItemsJson;
                    output.write(response.getBytes(StandardCharsets.UTF_8));
                    logger.info("New product creation request processed successfully");
                } else {
                    String errorResponse = "HTTP/1.1 500 Internal Server Error\r\nContent-Type: text/plain\r\n\r\nFailed to save all new items to the database";
                    output.write(errorResponse.getBytes(StandardCharsets.UTF_8));
                    logger.error("Failed to save all new items to the database");
                }
            } else {
                Item newItem = gson.fromJson(httpRequest.getBody(), Item.class);
                Storage storage = new Storage();

                if (storage.save(newItem)) {
                    String createdItemJson = gson.toJson(newItem);
                    String response = "HTTP/1.1 201 Created\r\nContent-Type: application/json\r\n\r\n" + createdItemJson;
                    output.write(response.getBytes(StandardCharsets.UTF_8));
                    logger.info("New product creation request processed successfully");
                } else {
                    String errorResponse = "HTTP/1.1 500 Internal Server Error\r\nContent-Type: text/plain\r\n\r\nFailed to save the new item to the database";
                    output.write(errorResponse.getBytes(StandardCharsets.UTF_8));
                    logger.error("Failed to save the new item to the database");
                }
            }
        } catch (JsonSyntaxException | IllegalStateException e) {
            String errorResponse = "HTTP/1.1 400 Bad Request\r\nContent-Type: text/plain\r\n\r\nInvalid JSON format in the request body";
            output.write(errorResponse.getBytes(StandardCharsets.UTF_8));
            logger.error("Invalid JSON format in the request body");
        }
    }
}
