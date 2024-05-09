package ru.otus.vpavlova.web.app.application;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Storage {
    private static final Logger logger = LogManager.getLogger(Storage.class);

    public static void init() {
        System.out.println("Хранилище проинициализировано");
    }

    public static List<Item> getItems() {
        List<Item> items = new ArrayList<>();
        items = DatabaseConnector.getAllItems();
        return items;
    }

    public static boolean save(Item item) {
        try (Connection connection = DatabaseConnector.getConnection()) {
            boolean isSaved = DatabaseConnector.insertItem(connection, item);
            if (isSaved) {
                logger.info("Item saved to storage: " + item.getTitle());
                return true;
            } else {
                logger.error("Failed to save item to storage: " + item.getTitle());
                return false;
            }
        } catch (SQLException e) {
            logger.error("Error while saving item to storage: " + item.getTitle(), e);
            return false;
        }
    }

}
