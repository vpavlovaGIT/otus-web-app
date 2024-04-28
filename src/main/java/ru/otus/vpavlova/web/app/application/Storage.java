package ru.otus.vpavlova.web.app.application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Storage {
    private static final Logger logger = LogManager.getLogger(Storage.class);
    private static List<Item> items;

    public static void init() {
        System.out.println("Хранилище проинициализировано");
        items = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            items.add(new Item("item " + i, 100 + (int)(Math.random() * 1000)));
        }
    }

    public static List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }

    public static void save(Item item) {
        item.setId(UUID.randomUUID());
        items.add(item);
        logger.info("Item saved to storage: " + item.getTitle());
    }
}
