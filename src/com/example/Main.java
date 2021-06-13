package com.example;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        Board board = new Board();
        int k = 1;
        Integer currentPlayer;
        String currentLine;
        List<Integer> inputCoordinate;
        Integer winner;

        while(board.getWinner() == 0 & k <= 9){
            board.displayBoard();
            currentPlayer = k%2 == 1? 1: 2;
            System.out.println("Player " + currentPlayer.toString());
            currentLine = in.nextLine();
            inputCoordinate = Arrays.stream(currentLine.split(" ")).map(Integer::valueOf).collect(Collectors.toList());
            while(!board.setPlayer(inputCoordinate.get(0), inputCoordinate.get(1), currentPlayer)){
                System.out.println("Not correct square input! Try again");
                currentLine = in.nextLine();
                inputCoordinate = Arrays.stream(currentLine.split(" ")).map(Integer::valueOf).collect(Collectors.toList());
            }
            k++;
        }
        board.displayBoard();
        winner = board.getWinner();
        if (winner == 0){
            System.out.println("There is no winner!");
        }
        else{
            System.out.println("The winner is Player "+winner.toString());
        }
    }
}
