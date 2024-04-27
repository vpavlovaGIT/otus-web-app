package ru.otus.vpavlova.web.app.application.processors;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import ru.otus.vpavlova.web.app.HttpRequest;
import ru.otus.vpavlova.web.app.application.Item;
import ru.otus.vpavlova.web.app.application.Storage;


public class CreateNewProductProcessor implements RequestProcessor {
        @Override
        public void execute(HttpRequest httpRequest, OutputStream output) throws IOException {
            Gson gson = new Gson();
            Item item = gson.fromJson(httpRequest.getBody(), Item.class);
            Storage.save(item);
            String jsonOutItem = gson.toJson(item);

            String response = "HTTP/1.1 200 OK\r\nContent-Type: application/json\r\n\r\n" + jsonOutItem;
            output.write(response.getBytes(StandardCharsets.UTF_8));
        }
    }
