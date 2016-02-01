package com.sprout.game;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client extends Thread {

    private Socket socket = null;
    private ObjectOutputStream outputStream = null;
    private ObjectInputStream inStream = null;

    public Client() throws IOException {
        socket = new Socket("localhost", 4445);
        System.out.println("Connected");
    }

    public void communicate(GameLogic gl) throws ClassNotFoundException, IOException {
        outputStream.writeObject(gl);
        System.out.println("Object send = " + gl.toString());
    }

    public List<Integer> getGames() throws IOException {
        socket.getOutputStream().write(("games\n").getBytes());
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String curr;
        ArrayList<Integer> res = new ArrayList<>();
        while (!(curr = br.readLine()).equals("endofgames"))
        {
            res.add(Integer.parseInt(curr));
        }
        return res;
    }

    public void joinGame(int id) throws IOException {
        socket.getOutputStream().write(("join\n" + id + "\n").getBytes());
    }

    public void createGame() throws IOException {
        socket.getOutputStream().write("create\n".getBytes());
    }

    @Override
    public void run() {
        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            try {

                GameScreen.gl = (GameLogic) inStream.readObject();
                GameScreen.MYMOVE = true;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            System.out.println("Object received = " + GameScreen.gl.toString());
        }
    }
}