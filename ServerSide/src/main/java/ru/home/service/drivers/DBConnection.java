package ru.home.service.drivers;

import java.sql.*;

public class DBConnection {

    private static Connection connection;
    private static Statement statement;

    private DBConnection() {

    }

    public static Connection getConnection() throws SQLException {
        if (connection == null) {
            return connection = DriverManager.getConnection("jdbc:sqlite:serverchat.db");
        }
        return connection;
    }

    public static Statement getStatement() throws SQLException {
        if (statement == null) {
            return statement = connection.createStatement();
        }
        return statement;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void createTableUsers() throws SQLException {
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS userentities (\n"
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, \n"
                + "login TEXT, \n"
                + "password TEXT, \n"
                + "nickName TEXT \n"
                + ");");
    }

    public static void insertUser(String login, String password, String nickName) throws SQLException {
        statement.executeUpdate("INSERT INTO userentities(login, password, nickName)\n"
                + "VALUES ('" + login + "', '" + password + "', '" + nickName + "');");
    }

    public static String getNickNameByLoginAndPassword(String login, String password) throws SQLException {
        String log;
        String pass;

        ResultSet set = statement.executeQuery("SELECT login, password, nickName FROM userentities WHERE login='" + login + "';");
        try {
            log = set.getString("login");
            pass = set.getString("password");
        } catch (SQLException e) {
            return null;
        }

        if (pass.equals(password)) {
            return set.getString("nickName");
        }
        return null;
    }
}

