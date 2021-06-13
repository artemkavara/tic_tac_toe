package com.example;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private List<List<Square>> squares;

    public Board(){
        squares = new ArrayList<>();
        for(int i = 0; i<3; i++) {
            List<Square> squareRow = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                squareRow.add(new Square());
            }
            this.squares.add(squareRow);
        }
    }

    public Square getSquareByCoordinates(int row, int column){
        return this.squares.get(row).get(column);
    }

    public boolean setPlayer(int row, int column, int player){
        Square currentSquare = this.getSquareByCoordinates(row, column);
        if (currentSquare.getPlayerChoice() == 0){
            currentSquare.setPlayerChoice(player);
            return true;
        }
        return false;
    }

    public int getSquarePlayer(int row, int column){
        return this.getSquareByCoordinates(row, column).getPlayerChoice();
    }

    public int getWinner(){

        for (int i = 0; i<3; i++) {
            if ((this.getSquarePlayer(i, 0) == this.getSquarePlayer(i, 1)) &
                    (this.getSquarePlayer(i, 1) == this.getSquarePlayer(i, 2))) {
                return this.getSquarePlayer(i, 0);
            }
            if ((this.getSquarePlayer(0, i) == this.getSquarePlayer(1, i)) &
                    (this.getSquarePlayer(1, i) == this.getSquarePlayer(2, i))) {
                return this.getSquarePlayer(0, i);
            }
        }

        if((this.getSquarePlayer(0, 0) == this.getSquarePlayer(1, 1)) &
                (this.getSquarePlayer(1, 1)== this.getSquarePlayer(2, 2))){
            return this.getSquarePlayer(0, 0);
        }

        if((this.getSquarePlayer(0, 2) == this.getSquarePlayer(1, 1)) &
                (this.getSquarePlayer(1, 1)== this.getSquarePlayer(2, 0))){
            return this.getSquarePlayer(0, 2);
        }
        return 0;
    }

    public void displayBoard(){
        for (int i = 0; i<3; i++){
            for (Square square: squares.get(i)){
                System.out.print(square.getStringOfSquare()+" ");
            }
            System.out.print("\n");
        }
    }
}
