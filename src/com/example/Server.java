package com.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server {
    Socket socket;
    ServerSocket serverSocket;


    public static Vector<ClientHandler> clients = new Vector<>();
    public static int countClients;
    public static Board board;
    boolean sendRegMessage;

    public Server(int port)
    {
        System.out.println("The server has started");
        board = new Board();
        try {

            serverSocket = new ServerSocket(port);
            while (true){
                if(clients.size() < 2){
                    if(serverSocket.isClosed()){
                        serverSocket = new ServerSocket(port);
                        clients.get(0).count = 1;
                    }

                    socket = serverSocket.accept();
                    countClients = clients.size()+1;

                    ClientHandler clientHandler = new ClientHandler(
                            countClients,
                            socket);

                    Thread thread = new Thread(clientHandler);

                    System.out.println("Player "+countClients+" is logged in");
                    clients.add(clientHandler);

                    thread.start();
                    sendRegMessage = true;
                }

                if(clients.size() == 2){
                    if(sendRegMessage) {
                        System.out.println("All players are ready to start the game!");

                        for (ClientHandler client: clients){

                            client.objectOutputStream.writeUTF("All players are ready to start the game!");
                            client.objectOutputStream.writeUTF("In this game you are Player "+client.count);
                            if(client.count == 2){
                                client.state = 1;
                            }
                        }
                        sendRegMessage = false;
                    }
                    serverSocket.close();
                }
            }

        }


        catch(IOException i)
        {
            System.out.println(i);
        }


    }

    public static void main(String args[])
    {
        Server server = new Server(5000);
    }
}
