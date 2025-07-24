package com.ghab.javachatapp.client;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatClientGUI extends JFrame {

    private JTextArea messageArea;
    private JTextField textField;
    private ChatClient client;
    private JButton exitButton;
    private String name;  // <-- moved to class-level so all methods can access it

    public ChatClientGUI() {
        super("Chat Application");
        setSize(400, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Initialize components
        messageArea = new JTextArea();
        messageArea.setEditable(false);
        add(new JScrollPane(messageArea), BorderLayout.CENTER);

        textField = new JTextField();
        exitButton = new JButton("Exit");

        // Prompt username
        name = JOptionPane.showInputDialog(this, "Enter your name:", "Name Entry", JOptionPane.PLAIN_MESSAGE);
        setTitle("Chat Application - " + name);

        // Send message on Enter key press
        textField.addActionListener(e -> {
            String message = "[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "] "
                    + name + ": " + textField.getText();
            client.sendMessage(message);
            textField.setText("");
        });

        // Exit button logic with leave message
        exitButton.addActionListener(e -> {
            String departureMessage = "[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "] "
                    + name + " has left the chat.";
            client.sendMessage(departureMessage);
            try {
                Thread.sleep(1000); // optional pause
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            System.exit(0);
        });

        // Bottom panel with text field and exit button
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(textField, BorderLayout.CENTER);
        bottomPanel.add(exitButton, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        // Try to connect to server
        try {
            client = new ChatClient("127.0.0.1", 5000, this::onMessageReceived);
            client.startClient();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error connecting to server", "Connection error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void onMessageReceived(String message) {
        SwingUtilities.invokeLater(() -> messageArea.append(message + "\n"));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChatClientGUI().setVisible(true));
    }
}
