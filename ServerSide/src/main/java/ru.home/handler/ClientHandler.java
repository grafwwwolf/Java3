package ru.home.handler;

import org.apache.logging.log4j.LogManager;
import ru.home.service.MyServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import org.apache.logging.log4j.Logger;

public class ClientHandler {

    private MyServer myServer;
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;
    private static final Logger LOGGER = LogManager.getLogger(ClientHandler.class);

    private String nickName;
    private boolean isNotDisconnected;

    public ClientHandler(MyServer myServer, Socket socket) {
        try {
            this.myServer = myServer;
            this.socket = socket;
            this.dis = new DataInputStream(socket.getInputStream());
            this.dos = new DataOutputStream(socket.getOutputStream());
            isNotDisconnected = false;
            // добавление таймера//
            myServer.getCachedService().execute(() -> {     //тут ExecutorService
                try {
                    Thread.sleep(120000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!isNotDisconnected) {
                    LOGGER.info("Client was disconnected without authentication by timeout");
                    sendMessage("/end");
                }
            });
            //конец добавления таймера
            myServer.getCachedService().execute(() -> {     //тут ExecutorService
                try {
                    authentication();
                    if (isNotDisconnected) {
                        receiveMessage();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    closeConnection();
                    isNotDisconnected = true;

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void authentication() throws Exception {
        while (true) {
            String message = dis.readUTF();
            if (message.startsWith("/end")) {
                LOGGER.info("Client without authentication send command '" + message + " '");
                sendMessage(message);
                isNotDisconnected = false;
                break;

            }
            if (message.startsWith("/start")) {
                String[] arr = message.split("-", 3);
                if (arr.length != 3) {
                    sendMessage("Вы ввели невреную команду для входа и будете отключены");
                    LOGGER.info("Client without authentication send wrong data for  authentication: '" + message + " '");
                    sendMessage("/end");
                    isNotDisconnected = false;
                    break;
                }
                final String nick = myServer
                        .getAuthenticationService()
                        .getNickNameByLoginAndPassword(arr[1].trim(), arr[2].trim());
                if (nick != null) {
                    if (!myServer.nickNameIsBusy(nick)) {
                        isNotDisconnected = true;
                        sendMessage("/start " + nick);
                        this.nickName = nick;
                        myServer.sendMessageToClients(nickName + " присоединился к чату.");
                        LOGGER.info(nickName + " присоединился к чату.");
                        myServer.subscribe(this);
                        return;
                    } else {
                        sendMessage("Your nickName is busy now. Try later.");
                        LOGGER.info("Client without authentication send data for authentication of logged user: '" + message + " '");
                    }
                } else {
                    sendMessage("Wrong login or password");
                    LOGGER.info("Client without authentication send wrong data for  authentication: '" + message + " '");
                }
            }
        }
    }

    public synchronized void sendMessage(String message) {
        try {
            dos.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receiveMessage() throws IOException {
        while (true) {
            boolean correctInstruction = true;

            String message = dis.readUTF();
            if (message.startsWith("/")) {
                if (message.startsWith("/end")) {
                    myServer.unSubscribe(this);
                    sendMessage(message);
                    LOGGER.info(this.nickName + "send message/command '" + message + "'");
                    myServer.sendMessageToClients(nickName + " вышел из чата.");
                    LOGGER.info(nickName + " вышел из чата.");
                    return;
                }
                if (message.startsWith("/nick")) {
                    String[] msgArr = message.split("-", 3);
                    if (msgArr.length != 3) {
                        sendMessage("Введена неверная команда для личного сообщения.");
                        sendMessage("Формат для отправки личных сообщений:\n" +
                                "/nick-*nickname*-*your message*");
                        correctInstruction = false;
                        LOGGER.info("Клиент ввел неверные данные для личного сообщения: '" + message + "'");
                    }
                    if (correctInstruction) {
                        myServer.sendMessageToClients(this, msgArr[1], msgArr[2]);
                        LOGGER.info(this.nickName + " отправил сообщение: '" + msgArr[2] + "' пользователю " + msgArr[1]);
                    }
                }
                if (message.startsWith("/online")) {
                    myServer.getOnlineUsers(this);
                    LOGGER.info(this.nickName + " отправил команду " + message);
                }
                continue;
            }
            myServer.sendMessageToClients(nickName + ": " + message);
            LOGGER.info(nickName + " отправил сообщение: " + message);
        }
    }

    private void closeConnection() {
        try {
            dis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            dos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getNickName() {

        return nickName;
    }
}
