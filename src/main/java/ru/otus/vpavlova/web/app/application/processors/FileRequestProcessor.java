package ru.otus.vpavlova.web.app.application.processors;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import ru.otus.vpavlova.web.app.HttpRequest;

/**
 * Обработчик запроса для получения файла из папки /static.
 */
public class FileRequestProcessor implements RequestProcessor {

    /**
     * Выполняет запрос к файлу, считывая содержимое запрашиваемого файла
     * и записывая его в указанный выходной поток, либо отправляя сообщение об ошибке,
     * если файл не найден или имя файла некорректно.
     *
     * @param httpRequest HTTP-объект запроса с параметрами.
     * @param outputStream Выходной поток для записи ответа.
     * @throws IOException Если произошла ошибка ввода-вывода.
     */
    @Override
    public void execute(HttpRequest httpRequest, OutputStream outputStream) throws IOException {
        String folderName = "src/main/java/static";
        String fileName = null;
        String nameParam = httpRequest.getParameter("name");

        if (nameParam != null) {
            fileName = nameParam;
        }
        System.out.println(nameParam);

        if (fileName != null) {
            Path filePath = Paths.get(folderName, fileName);

            File file = filePath.toFile();
            if (file.exists() && !file.isDirectory()) {
                try (FileInputStream fis = new FileInputStream(file);
                     InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
                     BufferedReader reader = new BufferedReader(isr)) {

                    String line;
                    while ((line = reader.readLine()) != null) {
                        outputStream.write(line.getBytes(StandardCharsets.UTF_8));
                    }
                }
            } else {
                String notFoundFile = "HTTP/1.1 404 Not Found\r\nContent-Type: text/plain\r\n\r\nFile not found";
                outputStream.write(notFoundFile.getBytes(StandardCharsets.UTF_8));
            }
        } else {
            String invalidFileName = "HTTP/1.1 500 Internal Server Error\r\nContent-Type: text/plain\r\n\r\nInvalid file name";
            outputStream.write(invalidFileName.getBytes(StandardCharsets.UTF_8));
        }
    }
}