package ru.otus.vpavlova.web.app.application.processors;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.vpavlova.web.app.HttpRequest;
import ru.otus.vpavlova.web.app.application.Item;
import ru.otus.vpavlova.web.app.application.Storage;

/**
 * Этот класс обрабатывает запрос на получение продукта по его идентификатору.
 */
public class GetItemByIdProcessor implements RequestProcessor {

    private static final Logger logger = LogManager.getLogger(GetItemByIdProcessor.class);

    private Storage storage;

    /**
     * Конструктор класса.
     *
     * @param storage хранилище, из которого будет получен продукт по идентификатору
     */
    public GetItemByIdProcessor(Storage storage) {
        this.storage = storage;
    }

    /**
     * Метод для выполнения запроса на получение продукта по ID.
     *
     * @param httpRequest объект HTTP-запроса
     * @param output поток вывода, в который будет отправлен ответ
     * @throws IOException если возникают проблемы с вводом/выводом
     */
    @Override
    public void execute(HttpRequest httpRequest, OutputStream output) throws IOException {
        String id = httpRequest.getParameter("id");
        Item item = storage.getItemById(id);
        if (item != null) {
            String jsonResponse = new Gson().toJson(item);
            byte[] responseBytes = jsonResponse.getBytes(StandardCharsets.UTF_8);
            httpResponse(output, 200, responseBytes);
        } else {
            logger.error("Продукт с указанным id не найден: {}", id);
            String errorMsg = "Продукт с указанным id не найден";
            byte[] errorBytes = errorMsg.getBytes(StandardCharsets.UTF_8);
            httpResponse(output, 404, errorBytes);
        }
    }

    /**
     * Метод для отправки HTTP-ответа с заданным статусом и содержимым.
     *
     * @param output поток вывода, в который будет отправлен ответ
     * @param status статус HTTP-ответа
     * @param responseBytes массив байт содержимого ответа
     * @throws IOException если возникают проблемы с вводом/выводом
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
     * Метод для получения текстового представления статуса HTTP-ответа.
     *
     * @param status статус HTTP-ответа
     * @return текстовое представление статуса
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