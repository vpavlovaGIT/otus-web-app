package ru.otus.vpavlova.web.app.application.processors;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.vpavlova.web.app.HttpRequest;
import ru.otus.vpavlova.web.app.application.Storage;

/**
 * Обработчик запроса для удаления товара по идентификатору.
 */
public class DeleteItemProcessor implements RequestProcessor {
    private static final Logger logger = LogManager.getLogger(DeleteItemProcessor.class);
    private Storage storage;

    /**
     * Конструктор класса DeleteItemProcessor.
     * @param storage хранилище, в котором хранятся товары
     */
    public DeleteItemProcessor(Storage storage) {
        this.storage = storage;
    }

    /**
     * Выполняет запрос на удаление товара по идентификатору.
     *
     * @param httpRequest объект HttpRequest, содержащий параметры запроса
     * @param output поток вывода, в который будет записан результат выполнения запроса
     * @throws IOException если произошла ошибка ввода-вывода при обработке запроса
     */
    @Override
    public void execute(HttpRequest httpRequest, OutputStream output) throws IOException {
        String itemId = httpRequest.getParameter("id");

        boolean isDeleted = storage.deleteItem(itemId);
        String successResponse = "HTTP/1.1 500 Internal Server Error\r\nContent-Type: text/plain\r\n\r\nItem successfully deleted";
        String failResponse = "HTTP/1.1 500 Internal Server Error\r\nContent-Type: text/plain\r\n\r\nFailed to delete item";

        if (isDeleted) {
            output.write(successResponse.getBytes(StandardCharsets.UTF_8));
            logger.error("Item successfully deleted with ID: " + itemId);
        } else {
            output.write(failResponse.getBytes(StandardCharsets.UTF_8));
            logger.error("Failed to delete item with ID: " + itemId);
        }
    }
}
