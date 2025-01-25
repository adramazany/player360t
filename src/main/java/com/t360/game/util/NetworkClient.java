package com.t360.game.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class NetworkClient {
    private final static Logger logger = LoggerFactory.getLogger(NetworkClient.class);

    public static String sendAndWaitForResponse(String message, String host, int port) throws IOException {
        logger.debug("This message will send and wait for response: %s".formatted(message));
        Socket clientSocket = new Socket(host, port);
        try {
            // Input and output streams for this client
            PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // Send to client
            output.println(message);
            logger.debug("Sent to client: " + message);

            // Read response from client
            String clientResponse = input.readLine();
            logger.debug("Received from client: " + clientResponse);
            return clientResponse;
        } catch (IOException e) {
            logger.error("Error in communication with client: " + e.getMessage());
            throw e;
        }finally{
            try {
                clientSocket.close();
            } catch (IOException e) {
                logger.error("Error closing client socket: " + e.getMessage());
            }
        }
    }
}
