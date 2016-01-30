package com.sprout.game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class Client extends Thread {

    private Socket socket = null;
    private ObjectOutputStream outputStream = null;
    private ObjectInputStream inStream = null;

    public Client() throws IOException {
        socket = new Socket("localhost", 4445);
        System.out.println("Connected");
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        inStream = new ObjectInputStream(socket.getInputStream());
    }

    public void communicate(GameLogic gl) throws ClassNotFoundException, IOException {
        outputStream.reset();
        outputStream.writeObject(gl);
        System.out.println("Object send = " + gl.toString());
    }

    @Override
    public void run() {
        while (true) {
            try {
                SproutGame.gl = (GameLogic) inStream.readObject();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            System.out.println("Object received = " + SproutGame.gl.toString());
        }
    }
}