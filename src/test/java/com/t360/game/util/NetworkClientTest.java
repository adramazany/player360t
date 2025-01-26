package com.t360.game.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;
import org.powermock.api.mockito.PowerMockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NetworkClientTest {

    String expected = "Mocked input stream response.";

    @BeforeEach
    void setUp() throws IOException {
    }

    @Test
    @Timeout(value=5, unit= TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
    void sendAndWaitForResponse() throws Exception {
        NetworkClient mockNetworkClient = mock(NetworkClient.class);
//        NetworkClient mockNetworkClient = spy(new NetworkClient("localhost", 8000));
//        Socket mockSocket = spy(Socket.class);
        Socket mockSocket = mock(Socket.class);
        InputStream mockInputStream = mock(InputStream.class);
        OutputStream mockOutputStream = mock(OutputStream.class);

        when(mockNetworkClient.sendAndWaitForResponse(any(String.class))).thenCallRealMethod();
/*
        PowerMockito.whenNew(Socket.class)
                .withParameterTypes(String.class,int.class)
                .withArguments(any(String.class), any(int.class))
                .thenReturn(mockSocket);
*/
//        PowerMockito.when(mockSocket.getInputStream()).thenReturn(mockInputStream);

        when(mockNetworkClient.getClientSocket()).thenReturn(mockSocket);
        when(mockSocket.getInputStream()).thenReturn(mockInputStream);
        when(mockSocket.getOutputStream()).thenReturn(mockOutputStream);
//        doReturn(29).when(mockInputStream).read(any(byte[].class));

        ByteArrayInputStream bais = new ByteArrayInputStream(expected.getBytes());
//        when(mockInputStream.readLine()).thenAnswer( invocation ->  bais.read() );
        when(mockInputStream.read()).thenAnswer( invocation ->  bais.read() );

//        doReturn(-1).when(mockInputStream).read(new byte[1]);
//        doThrow(new RuntimeException()).when(mockInputStream).close();

//        byte[] buf = new byte[1024];
//        when(mockInputStream.read(buf)).thenAnswer( invocation ->  {bais.read(buf); invocation.getArgument<byte[]>(0) } );
//        when(mockInputStream.read(any(byte[].class), any(int.class), any(int.class))).thenAnswer( invocation ->  bais.read() );

//        String actual = new NetworkClient("localhost", 8000).sendAndWaitForResponse("test mocking socket");
        String actual = mockNetworkClient.sendAndWaitForResponse("test mocking socket");
        assertEquals(expected, actual);
    }
}