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

/**
 * Обработчик запроса для получения всех продуктов.
 */
public class GetAllProductsProcessor implements RequestProcessor {
    private static final Logger logger = LogManager.getLogger(GetAllProductsProcessor.class);

    /**
     * Метод execute обрабатывает GET запрос для получения всех продуктов.
     *
     * @param httpRequest HTTP запрос (для GET запроса не требуется тело).
     * @param output выходной поток для отправки ответа.
     * @throws IOException если возникают проблемы с чтением или записью данных.
     */
    @Override
    public void execute(HttpRequest httpRequest, OutputStream output) throws IOException {
        Storage storage = new Storage();
        List<Item> allItems = Storage.getItems();

        Gson gson = new Gson();
        String allItemsJson = gson.toJson(allItems);

        String response = "HTTP/1.1 200 OK\r\nContent-Type: application/json\r\n\r\n" + allItemsJson;
        output.write(response.getBytes(StandardCharsets.UTF_8));

        logger.info("All items retrieval request processed successfully");
    }
}
