package ru.home.service;


import org.apache.logging.log4j.LogManager;

import org.apache.logging.log4j.Logger;
import ru.home.handler.ClientHandler;
import ru.home.service.interfaces.AuthenticationService;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyServer {

    private static final Integer PORT = 8880;

    private AuthenticationService authenticationService;
    private List<ClientHandler> handlerList;
    private ExecutorService cachedService;    // добавил //тут ExecutorService
    private static final Logger LOGGER = LogManager.getLogger(MyServer.class);

    public MyServer() {
        System.out.println("Server started.");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            cachedService = Executors.newCachedThreadPool();        // сделал Executors.newCachedThreadPool();, т.к. у нас есть таймер отключения незалогированного пользователя в 120 сек
            authenticationService = new AuthenticationServiceImpl();
            authenticationService.start();
            handlerList = new ArrayList<>();
            LOGGER.info("Сервер запущен");
            while (true) {
                System.out.println("Server wait connection...");
                Socket socket = serverSocket.accept();
                System.out.println("Client connected.");
                LOGGER.info("Client connected.");
                new ClientHandler(this, socket);
            }
        } catch (Exception e) {
            LOGGER.error("Произошло ошибка");
            LOGGER.info("Сервер завершил работу");
            e.printStackTrace();
        } finally {
            cachedService.shutdown();
            authenticationService.stop();
        }
    }

    public synchronized boolean nickNameIsBusy(String nickName) {
        return handlerList
                .stream()
                .anyMatch(clientHandler -> clientHandler.getNickName().equalsIgnoreCase(nickName));
    }

    public synchronized void sendMessageToClients(String message) {
        handlerList.forEach(clientHandler -> clientHandler.sendMessage(message));
    }

    public synchronized void sendMessageToClients(ClientHandler sender, String recipient, String message) {
        boolean check = true;
        for (ClientHandler clientHandler : handlerList) {
            if (clientHandler.getNickName().equals(recipient)) {
                clientHandler.sendMessage("---------Вам личное сообщение от  " + sender.getNickName() + ": " + message);
                sender.sendMessage("Личное сообщение контакту: " + recipient + ": " + message);
                check = false;
                return;
            }
        }
        if (check) {
            sender.sendMessage("Контакт " + recipient + " не в сети.");
        }
    }

    public synchronized void getOnlineUsers(ClientHandler clientHandler) {
        String onlineContants = new String("Now online:\n" + "*** ");
        for (ClientHandler userOnline : handlerList) {
            if (userOnline.getNickName().equals(clientHandler.getNickName())) {
                continue;
            }
            onlineContants = onlineContants.concat(userOnline.getNickName() + " *** ");
        }
        clientHandler.sendMessage(onlineContants);
    }

    public synchronized void subscribe(ClientHandler clientHandler) {
        handlerList.add(clientHandler);
    }

    public synchronized void unSubscribe(ClientHandler clientHandler) {
        handlerList.remove(clientHandler);
    }

    public AuthenticationService getAuthenticationService() {
        return this.authenticationService;
    }

    public ExecutorService getCachedService() {
        return cachedService;
    }
}


