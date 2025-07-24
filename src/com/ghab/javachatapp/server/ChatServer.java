package com.ghab.javachatapp.server;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {

    // Shared list to keep track of all connected clients
    private static List<ClientHandler> clients = new ArrayList<>();

    // Main method to start the server
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(5000);
        System.out.println("Server started. Waiting for clients...");

        // Accept clients forever
        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket);

            // Create handler for each client and start it in a new thread
            ClientHandler handler = new ClientHandler(clientSocket, clients);
            clients.add(handler);
            new Thread(handler).start();
        }
    }

    // Inner class to handle communication with each client
    static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private List<ClientHandler> clients;
        private PrintWriter out;
        private BufferedReader in;

        public ClientHandler(Socket socket, List<ClientHandler> clients) throws IOException {
            this.clientSocket = socket;
            this.clients = clients;
            this.out = new PrintWriter(clientSocket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        }

        @Override
        public void run() {
            try {
                String inputLine;
                // Read messages and broadcast to all connected clients
                while ((inputLine = in.readLine()) != null) {
                    for (ClientHandler aClient : clients) {
                        aClient.out.println(inputLine);
                    }
                }
            } catch (IOException e) {
                System.out.println("An error occurred: " + e.getMessage());
            } finally {
                try {
                    // Clean up on disconnect
                    in.close();
                    out.close();
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
