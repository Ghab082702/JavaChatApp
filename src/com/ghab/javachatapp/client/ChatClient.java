package com.ghab.javachatapp.client;

import java.io.*;
import java.net.*;
import java.util.function.Consumer;

public class ChatClient {

    // Network socket to connect to the server
    private Socket socket = null;

    // For reading input from the console (user input)
    private BufferedReader inputConsole = null;

    // For sending messages to the server
    private PrintWriter out = null;

    // For receiving messages from the server
    private BufferedReader in = null;

    // Constructor: connects to server and handles message exchange
    public ChatClient(String address, int port) {
        try {
            // Connect to the server at given address and port
            socket = new Socket(address, port);
            System.out.println("Connected to chat server");

            // Set up input stream to read from the console (user typing)
            inputConsole = new BufferedReader(new InputStreamReader(System.in));

            // Set up output stream to send messages to the server
            out = new PrintWriter(socket.getOutputStream(), true);

            // Set up input stream to receive messages from the server
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Start the message exchange loop
            String line = "";
            while (!line.equals("exit")) {
                // Read user's input from the console
                line = inputConsole.readLine();

                // Send the input to the server
                out.println(line);

                // Read and print the server's response
                System.out.println(in.readLine());
            }

            // Clean up: close all streams and socket
            socket.close();
            inputConsole.close();
            out.close();

        } catch (UnknownHostException u) {
            System.out.println("Host Unknown: " + u.getMessage());
        } catch (IOException i) {
            System.out.println("Unexpected exception: " + i.getMessage());
        }
    }

    // Main method: entry point of the client program
    public static void main(String[] args) {
        // Create and start the chat client connecting to localhost on port 5000
        ChatClient client = new ChatClient("127.0.0.1", 5000);
    }

}
