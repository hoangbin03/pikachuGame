
package controller;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class database {
    private Connection connection;

    public database() {
        try {
            String url = "jdbc:sqlserver://LAPTOPCUAHOANG\\TESTDB:1433;databaseName=game";
            String username = "sa";
            String password = "12345";
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveScore(int time, int score) {
        try {
            String sql = "INSERT INTO highscores (time, score) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, time);
            statement.setInt(2, score);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Score> getHighScores() {
        List<Score> highScores = new ArrayList<>();
        try {
            String sql = "SELECT * FROM highscores";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int time = resultSet.getInt("time");
                int score = resultSet.getInt("score");
                highScores.add(new Score(time, score));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return highScores;
    }

    public void createTable() {
        try {
            String sql = "CREATE TABLE highscores (id INTEGER PRIMARY KEY,time INTEGER, score INTEGER)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
