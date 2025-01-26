package com.t360.game.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

public class NetworkClient {
    private final static Logger logger = LoggerFactory.getLogger(NetworkClient.class);
    String host;
    int port;
    Socket clientSocket;

    public NetworkClient(String host, int port) throws IOException {
        this.host = host;
        this.port = port;
        clientSocket = new Socket(host, port);
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public String sendAndWaitForResponse(String message) throws IOException {
        logger.debug("This message will send and wait for response: %s".formatted(message));
        Socket socket = getClientSocket();
        try {
            // Input and output streams for this client
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
//            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            InputStream input = socket.getInputStream();

            // Send to client
            output.println(message);
            logger.debug("Sent to client: " + message);

            // Read response from client
//            String clientResponse = input.readLine();
            byte buf[] = new byte[1024];
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
}
