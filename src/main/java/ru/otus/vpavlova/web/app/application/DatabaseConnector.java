package ru.otus.vpavlova.web.app.application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DatabaseConnector {
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static boolean insertItem(Connection connection, Item item) {
        String query = "INSERT INTO items (id, title, price) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, item.getId());
            statement.setString(2, item.getTitle());
            statement.setInt(3, item.getPrice());
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static List<Item> getAllItems() {
        List<Item> items = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT id, title, price FROM items");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Item item = new Item();
                item.setId(UUID.fromString(resultSet.getString("id")));
                item.setTitle(resultSet.getString("title"));
                item.setPrice(resultSet.getInt("price"));
                items.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public static Item getItemById(String id) {
        String query = "SELECT id, title, price FROM items WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, UUID.fromString(id));
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Item item = new Item();
                item.setId(UUID.fromString(resultSet.getString("id")));
                item.setTitle(resultSet.getString("title"));
                item.setPrice(resultSet.getInt("price"));
                return item;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean updateItem(Item item) {
        try (Connection connection = getConnection()) {
            String query = "UPDATE items SET title = ?, price = ? WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, item.getTitle());
                statement.setInt(2, item.getPrice());
                statement.setObject(3, item.getId());
                int rowsUpdated = statement.executeUpdate();
                return rowsUpdated > 0;
            }
        } catch (SQLException e) {
            return false;
        }
    }
}
