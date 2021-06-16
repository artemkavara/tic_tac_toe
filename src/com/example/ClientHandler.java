package com.example;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class ClientHandler implements Runnable{

    private String name;
    int count;
    int state = 0;

    final DataInputStream objectInputStream;
    final DataOutputStream objectOutputStream;
    final Socket socket;
    List<Integer> inputCoordinate;
    String currentLine;



    public void scanCoordinates() throws IOException {
        int controlLoop = 0;
        while (controlLoop<1000){
            try{
                currentLine = this.objectInputStream.readUTF();
                if (this.state == 1){
                    objectOutputStream.writeUTF("Wait until the other player make his move!");
                    continue;
                }

                if (currentLine.equals("logout")) {

                    try
                    {
                        // closing resources
                        this.objectOutputStream.close();
                        this.objectInputStream.close();
                        this.socket.close();
                        System.out.println(name+" (Player "+count+") has logged out");

                    }
                    catch(IOException e){
                        e.printStackTrace();
                    }
                    break;
                }
                inputCoordinate = Arrays.stream(currentLine.split(" ")).map(Integer::valueOf).collect(Collectors.toList());
                if (inputCoordinate.size() != 2){
                    this.objectOutputStream.writeUTF("Not correct square input! Try again");
                }
                else{
                    break;
                }
            }
            catch(NumberFormatException | IOException e){
                this.objectOutputStream.writeUTF("Not correct square input! Try again!");
            }
            controlLoop++;
    }
    }

    public ClientHandler(
                         int count,
                         Socket socket
    ) throws IOException {
        this.count = count;
        this.socket = socket;
        this.objectOutputStream = new DataOutputStream(socket.getOutputStream());
        this.objectOutputStream.flush();
        this.objectInputStream = new DataInputStream(socket.getInputStream());
        this.objectOutputStream.writeUTF("Enter your name: ");
        this.name = objectInputStream.readUTF();
        this.objectOutputStream.writeUTF("Welcome aboard, "+ this.name);
        this.objectOutputStream.writeUTF("To exit the game (in any time) type \"logout\"");
    }


    @Override
    public void run() {

        while (true) {
            try {

                scanCoordinates();
                if (currentLine.equals("logout")) {
                    Server.clients.remove(this);
                    Server.clients.get(0).objectOutputStream.writeUTF(
                            "Game was stopped by another player.\nIf you want to logout, type \"logout\" ");
                    Server.clients.get(0).state = 0;
                    Server.board = new Board();
                    return;
                }
                this.state = 1;
                if (!Server.board.setPlayer(inputCoordinate.get(0), inputCoordinate.get(1), this.count)) {
                    this.state = 0;
                    objectOutputStream.writeUTF("This square is in use! Try again!");
                    continue;
                }

                for (ClientHandler client : Server.clients) {

                    client.objectOutputStream.writeUTF("Move " + Server.board.countMoves);

                    client.objectOutputStream.writeUTF(Server.board.getBoardString());
                    if (Server.board.getWinner()!=0){
                        int winner = Server.board.getWinner();
                        String winnerName = Server.clients.get(winner-1).name;
                        client.objectOutputStream.writeUTF("The winner is Player "+ winner + " (" + winnerName + ")");
                        client.state = 0;
                        client.objectOutputStream.writeUTF("To start the new game enter your input or wait another player!\nTo exit the game type logout");
                    }
                    if (!(client.count == this.count) && Server.board.getWinner()==0) {
                        client.state = 0;
                        client.objectOutputStream.writeUTF("Now it is your turn!");
                        this.objectOutputStream.writeUTF("Now waiting for " + client.name);
                        //client.objectOutputStream.writeObject(board);
                    }
                }

                if(Server.board.getWinner()!=0 | Server.board.countMoves == 9){

                    if (Server.board.getWinner() == 0){
                        for (ClientHandler client : Server.clients) {
                            client.objectOutputStream.writeUTF("No one wins!\nTo start the new game enter your input or wait another player!\nTo exit the game type logout");
                        }
                    }
                    int winner = Server.board.getWinner();
                    System.out.println(winner == 0? "No one won" : "The winner is Player "+ winner);
                    System.out.println("Starting new game");
                    Server.board = new Board();
                }

            } catch (IOException e) {
                e.printStackTrace();
                //break;
            }
        }
    }
}
