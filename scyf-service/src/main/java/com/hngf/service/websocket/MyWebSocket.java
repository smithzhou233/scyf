package com.hngf.service.websocket;

import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Collections;

/**
 * websocket 服务端 - 使用java_websocket包
 */
public class MyWebSocket extends WebSocketServer {

    public MyWebSocket( int port ) throws UnknownHostException {
        super( new InetSocketAddress( port ) );
    }

    public MyWebSocket( InetSocketAddress address ) {
        super( address );
    }

    public MyWebSocket(int port, Draft_6455 draft) {
        super( new InetSocketAddress( port ), Collections.singletonList(draft));
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        webSocket.send("Welcome to the server!"); //This method sends a message to the new client
        broadcast( "new connection: " + clientHandshake.getResourceDescriptor() ); //This method sends a message to all clients connected
        System.out.println( webSocket.getRemoteSocketAddress().getAddress().getHostAddress() + " entered the room!" );
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        broadcast( webSocket + " has left the room!" );
        System.out.println( webSocket + " has left the room!" );
    }

    @Override
    public void onMessage(WebSocket webSocket, String message) {
        broadcast( message );
        System.out.println("WebSocketServer:" + webSocket + ": " + message );
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {
        broadcast( e.getMessage() );
        System.out.println( webSocket + ": " + e.getMessage() );
    }

    @Override
    public void onStart() {
        System.out.println("Server started!");
        setConnectionLostTimeout(0);
        setConnectionLostTimeout(100);
    }

    public static void main( String[] args ) throws InterruptedException , IOException {
        int port = 8887; // 843 flash policy port
        try {
            port = Integer.parseInt( args[ 0 ] );
        } catch ( Exception ex ) {
        }
        MyWebSocket s = new MyWebSocket( port );
        s.start();
        System.out.println( "ChatServer started on port: " + s.getPort() );

        BufferedReader sysin = new BufferedReader( new InputStreamReader( System.in ) );
        while ( true ) {
            String in = sysin.readLine();
            s.broadcast( in );
            if( in.equals( "exit" ) ) {
                s.stop(1000);
                break;
            }
        }
    }
}
