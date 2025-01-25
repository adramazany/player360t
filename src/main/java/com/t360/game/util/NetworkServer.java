package com.t360.game.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Function;

public class NetworkServer {
    private final static Logger logger = LoggerFactory.getLogger(NetworkServer.class);
    private final int port;
    private final Function<String, String> reply;

    public NetworkServer(int port, Function<String, String> reply){
        this.port = port;
        this.reply = reply;
    }

    public void listen()  {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("Server is listening on port %d ...".formatted(port));

            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    logger.info("New client connected: %s:%d".formatted(clientSocket, clientSocket.getPort())); // .getInetAddress()

                    // Create a new thread to handle the client
                    new ClientHandler(clientSocket, reply).start();
                } catch (IOException e) {
                    logger.error("Error handling client connection: " + e.getMessage(),e);
                }
            }
        } catch (IOException e) {
            logger.error("Server error: " + e.getMessage(),e);
        }
    }
}
class ClientHandler extends Thread {
    private final static Logger logger = LoggerFactory.getLogger(ClientHandler.class);
    private final Socket clientSocket;
    private final Function<String, String> reply;

    public ClientHandler(Socket clientSocket, Function<String, String> reply) {
        this.clientSocket = clientSocket;
        this.reply = reply;
    }

    @Override
    public void run() {
        try {
            // Input and output streams for this client
            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);

            // Read request from client
            String clientRequest = input.readLine();
            logger.debug("Received from client: " + clientRequest);

            // Respond to client
            String response = reply.apply(clientRequest) ;
            output.println(response);
            logger.debug("Sent to client: " + response);

        } catch (IOException e) {
            logger.error("Error in communication with client: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                logger.error("Error closing client socket: " + e.getMessage());
            }
        }
    }
}