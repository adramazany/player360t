package com.t360.game.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

/**
 * @author adelramezani.jd@gmail.com
 * Utility that facilitate sending message over network and waiting for response to return to caller
 */
public class NetworkClient {
    private final static Logger logger = LoggerFactory.getLogger(NetworkClient.class);
    private String host;
    private int port;
    private Socket clientSocket;

    /**
     * Create a network socket to connect to a server
    */
    public NetworkClient(String host, int port) throws IOException {
        this.host = host;
        this.port = port;
        clientSocket = new Socket(host, port);
    }

    /**
     * Sending message over network and waiting for response to return to caller
    */
    public String sendAndWaitForResponse(String message) throws IOException {
        logger.debug("This message will send and wait for response: %s".formatted(message));
        Socket socket = getClientSocket();
        try {
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            InputStream input = socket.getInputStream();

            output.println(message);
            logger.debug("Sent to client: " + message);

            byte[] buf = new byte[1024];
            int c = input.read(buf);
            String clientResponse = new String( buf );
            logger.debug("Received from client: " + clientResponse);
            return clientResponse;
        } catch (IOException e) {
            logger.error("Error in communication with client: " + e.getMessage());
            throw e;
        }finally{
            try {
                socket.close();
            } catch (IOException e) {
                logger.error("Error closing client socket: " + e.getMessage());
            }
        }
    }

    public Socket getClientSocket() {
        return clientSocket;
    }
}
