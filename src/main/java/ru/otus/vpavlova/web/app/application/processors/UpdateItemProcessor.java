package ru.otus.vpavlova.web.app.application.processors;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.vpavlova.web.app.HttpRequest;
import ru.otus.vpavlova.web.app.application.Item;
import ru.otus.vpavlova.web.app.application.Storage;

/**
 * Обработчик запроса для обновления информации о товаре.
 */
public class UpdateItemProcessor implements RequestProcessor {
    private static final Logger logger = LogManager.getLogger(UpdateItemProcessor.class);

    private Storage storage;

    /**
     * Конструктор класса UpdateItemProcessor.
     * @param storage хранилище, в котором хранятся товары
     */
    public UpdateItemProcessor(Storage storage) {
        this.storage = storage;
    }

    /**
     * Выполняет обновление информации о товаре на основе параметров запроса.
     *
     * @param httpRequest HTTP запрос, содержащий информацию о товаре для обновления
     * @param output поток вывода, в который будет записан результат выполнения запроса
     * @throws IOException если происходит ошибка ввода-вывода при работе с потоками
     */
    @Override
    public void execute(HttpRequest httpRequest, OutputStream output) throws IOException {
        String itemId = httpRequest.getParameter("id");
        String encodedTitle = httpRequest.getParameter("title");
        String decodedTitle = URLDecoder.decode(encodedTitle, StandardCharsets.UTF_8.toString());
        double price = Double.parseDouble(httpRequest.getParameter("price"));

        logger.error("Обновление товара с ID: " + itemId);

        Item itemToUpdate = storage.getItemById(itemId);

        if (itemToUpdate != null) {
            itemToUpdate.setTitle(decodedTitle);
            itemToUpdate.setPrice((int) price);

            boolean isUpdated = storage.updateItem(itemToUpdate);

            if (isUpdated) {
                logger.error("Товар успешно обновлен: " + itemToUpdate);
                String jsonResponse = new Gson().toJson(itemToUpdate);
                byte[] responseBytes = jsonResponse.getBytes(StandardCharsets.UTF_8);
                httpResponse(output, 200, responseBytes);
            } else {
                logger.error("Не удалось обновить товар");
                String errorMsg = "Не удалось обновить товар";
                byte[] errorBytes = errorMsg.getBytes(StandardCharsets.UTF_8);
                httpResponse(output, 500, errorBytes);
            }
        } else {
            logger.error("Товар с ID " + itemId + " не найден");
            String errorMsg = "Товар с указанным ID не найден";
            byte[] errorBytes = errorMsg.getBytes(StandardCharsets.UTF_8);
            httpResponse(output, 404, errorBytes);
        }
    }

    /**
     * Отправляет HTTP-ответ.
     * @param output поток вывода, в который отправляется ответ
     * @param status статус код HTTP-ответа
     * @param responseBytes массив байт для отправки
     * @throws IOException если возникают проблемы с выводом данных
     */
    private void httpResponse(OutputStream output, int status, byte[] responseBytes) throws IOException {
        String statusMessage = getStatusMessage(status);
        String httpResponse = "HTTP/1.1 " + status + " " + statusMessage + "\r\n"
                + "Content-Type: application/json\r\n" + "Content-Length: " + responseBytes.length + "\r\n"
                + "\r\n";
        output.write(httpResponse.getBytes(StandardCharsets.UTF_8));
        output.write(responseBytes);
    }

    /**
     * Возвращает текстовое сообщение соответствующее статусу HTTP-ответа.
     * @param status статус код HTTP-ответа
     * @return текстовое сообщение статуса
     */
    private String getStatusMessage(int status) {
        switch (status) {
            case 200:
                return "OK";
            case 404:
                return "Not Found";
            default:
                return "Unknown Status";
        }
    }
}