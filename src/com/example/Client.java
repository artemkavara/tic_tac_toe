package com.example;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client
{
    final static int ServerPort = 5000;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;

    public Client() throws IOException {
        Scanner scn = new Scanner(System.in);

        // getting localhost ip
        InetAddress ip = InetAddress.getByName("localhost");

        // establish the connection
        Socket s = new Socket(ip, ServerPort);

        // obtaining input and out streams
        dataInputStream =  new DataInputStream(s.getInputStream());
        dataOutputStream = new DataOutputStream(s.getOutputStream());

        // sendMessage thread
        Thread sendMessage = new Thread(new Runnable()
        {
            @Override
            public void run() {
                String msg;
                while (true) {

                    // read the message to deliver.
                    if(scn.hasNext()){
                        msg = scn.nextLine();


                    try {
                        // write on the output stream
                        dataOutputStream.writeUTF(msg);
                        //System.out.println(msg);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                }
            }
        });

        // readMessage thread
        Thread readMessage = new Thread(new Runnable()
        {
            @Override
            public void run() {
                String msg;
                while (true) {
                    try {
                        // read the message sent to this client
                        if (dataInputStream.available() > 0){
                            msg = dataInputStream.readUTF();
                            System.out.println(msg);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                        //break;
                    }
                }
            }
        });

        sendMessage.start();
        readMessage.start();
    }

    public static void main(String args[]) throws IOException
    {
        Client client = new Client();
    }
}

