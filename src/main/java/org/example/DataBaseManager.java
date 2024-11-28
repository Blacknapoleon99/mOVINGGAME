package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataBaseManager {
    private static final String DB_URL = "jdbc:sqlite:angel_demon_game.db"; // Name of the SQLite database file

    public DataBaseManager() {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection(DB_URL);
            createTablesIfNotExist(connection);
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createTablesIfNotExist(Connection connection) {
        String createGameStateTable = "CREATE TABLE IF NOT EXISTS game_state (" +
                "id INTEGER PRIMARY KEY," +
                "state BLOB" +
                ")";

        try {
            PreparedStatement statement = connection.prepareStatement(createGameStateTable);
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveGameState(AngelDemonGame.GameState gameState) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);

            String saveStateSQL = "INSERT INTO game_state (state) VALUES (?)";
            PreparedStatement statement = connection.prepareStatement(saveStateSQL);

            // Convert gameState to a byte array
            byte[] gameStateBytes = serializeGameState(gameState);
            statement.setBytes(1, gameStateBytes);

            statement.executeUpdate();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public AngelDemonGame.GameState loadLatestGameState() {
        AngelDemonGame.GameState gameState = null;

        try {
            Connection connection = DriverManager.getConnection(DB_URL);

            String loadStateSQL = "SELECT state FROM game_state ORDER BY id DESC LIMIT 1";
            PreparedStatement statement = connection.prepareStatement(loadStateSQL);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                byte[] gameStateBytes = resultSet.getBytes("state");
                gameState = deserializeGameState(gameStateBytes);
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return gameState;
    }

    private byte[] serializeGameState(AngelDemonGame.GameState gameState) {
        // TODO: Implement the logic to serialize gameState to a byte array
        // You can use Java's ObjectOutputStream and ByteArrayOutputStream for this
        return null;
    }

    private AngelDemonGame.GameState deserializeGameState(byte[] gameStateBytes) {
        // TODO: Implement the logic to deserialize a byte array back to a GameState object
        // You can use Java's ObjectInputStream and ByteArrayInputStream for this
        return null;
    }
}
