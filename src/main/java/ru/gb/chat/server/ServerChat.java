package ru.gb.chat.server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Created by Artem Kropotov on 17.05.2021
 */
public class ServerChat {
    private static final Logger logger = Logger.getLogger("admin");
    private final CopyOnWriteArrayList<ClientHandler> clients = new CopyOnWriteArrayList<>();

    public static void main(String[] args) throws UnsupportedEncodingException {
        new ServerChat().start();
    }

    public void start() throws UnsupportedEncodingException {
        FileHandler fileHandler = null;
        try {
            fileHandler = new FileHandler("MyLOG.log");
            fileHandler.setEncoding("UTF-8");
            fileHandler.setFormatter(new SimpleFormatter());

        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.addHandler(fileHandler);
        try(ServerSocket serverSocket = new ServerSocket(8189)) {
//            System.out.println("Сервер запущен");
            logger.info("Сервер запущен");
            while (true) {
                Socket socket = serverSocket.accept();
//                System.out.println("Клиент подключился");
                logger.info("Клиент подключился");
                new ClientHandler(socket, this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println();
    }

    public void broadcastMsg(String msg) {
        for (ClientHandler client : clients) {
            client.sendMessage(msg);
        }
    }

    public void subscribe(ClientHandler client) {
        clients.add(client);
        broadcastClientList();
    }

    public void unsubscribe(ClientHandler clientHandler) {
        clients.remove(clientHandler);
        broadcastClientList();
    }

    public boolean isNickBusy(String nickname) {
        for (ClientHandler c : clients) {
            if (c != null) {
                if (c.getUser().getNickname().equals(nickname)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void privateMsg(ClientHandler sender, String nick, String message) {
        if (sender.getUser().getNickname().equals(nick)) {
            sender.sendMessage("Заметка для себя: " + message);
            logger.info(sender.getUser().getNickname() + "Заметка для себя: " + message);
        }

        for (ClientHandler receiver : clients) {
            if (receiver.getUser().getNickname().equals(nick)) {
                receiver.sendMessage("от " + sender.getUser().getNickname() + ": " + message);
                sender.sendMessage("для " + nick + ": " + message);
                logger.info(sender.getUser().getNickname() + "Написал сообщение :" + message + " для : " + nick);

                return;
            }
        }
        sender.sendMessage("Клиент " + nick + " не найден");
        logger.info("Клиент " + nick + " не найден");
    }

    public void broadcastClientList() {
        StringBuilder stringBuilder = new StringBuilder(9 + 15 * clients.size());
        stringBuilder.append("/clients ");
        // '/clients '
        for (ClientHandler c : clients) {
            stringBuilder.append(c.getUser().getNickname()).append(" ");
        }
        // '/clients nick1 nick2 '
        stringBuilder.setLength(stringBuilder.length() - 1);
        // '/clients nick1 nick2'
        String nickList = stringBuilder.toString();
        for (ClientHandler c : clients) {
            c.sendMessage(nickList);
        }
    }
}
