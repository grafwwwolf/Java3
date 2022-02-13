package ru.home.service;

import ru.home.service.drivers.DBConnection;
import ru.home.service.interfaces.AuthenticationService;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class AuthenticationServiceImpl implements AuthenticationService {


    private static Connection connection;
    private static Statement statement;

    public AuthenticationServiceImpl() {

    }

    @Override
    public void start() {
        try {
            connection = DBConnection.getConnection();
            statement = DBConnection.getStatement();
        } catch (SQLException e) {
            e.printStackTrace();
            DBConnection.closeConnection();
        }
        System.out.println("Authentication service start");
    }

    @Override
    public void stop() {
        System.out.println("Authentication service stop");
        DBConnection.closeConnection();
    }


    @Override
    public String getNickNameByLoginAndPassword(String login, String password) {
        try {
            return DBConnection.getNickNameByLoginAndPassword(login, password);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
