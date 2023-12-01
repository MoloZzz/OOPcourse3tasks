package tasks;

import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;

public class Server {

    public static void startServer(int port) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

            server.createContext("/", new MyHandler());

            server.setExecutor(null);
            server.start();

            System.out.println("Сервер запущено на порту " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


