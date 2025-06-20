package com.mycompany.gamemultiplayer;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    static final int PORT = 12345;
    static List<ClientHandler> clients = new ArrayList<>();
    static Random rand = new Random();
    static int dotX = rand.nextInt(400);
    static int dotY = rand.nextInt(400);

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server started...");

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                dotX = rand.nextInt(400);
                dotY = rand.nextInt(400);
                broadcast("DOT:" + dotX + "," + dotY);
            }
        }, 0, 5000);

        while (true) {
            Socket socket = serverSocket.accept();
            ClientHandler handler = new ClientHandler(socket);
            clients.add(handler);
            handler.start();
        }
    }

    static void broadcast(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    static class ClientHandler extends Thread {
        Socket socket;
        PrintWriter out;
        BufferedReader in;
        Player player;

        public ClientHandler(Socket socket) throws IOException {
            this.socket = socket;
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.player = new Player();
        }

        public void run() {
            try {
                String msg;
                while ((msg = in.readLine()) != null) {
                    if (msg.startsWith("POS:")) {
                        String[] parts = msg.substring(4).split(",");
                        int x = Integer.parseInt(parts[0]);
                        int y = Integer.parseInt(parts[1]);
                        player.setPosition(x, y);
                        if (Math.abs(dotX - x) < 20 && Math.abs(dotY - y) < 20) {
                            player.score++;
                            broadcast("SCORE:" + clients.indexOf(this) + "," + player.score);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        void sendMessage(String msg) {
            out.println(msg);
        }
    }
}
