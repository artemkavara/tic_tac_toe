package com.example;

public class Square {

    private int playerChoice;

    public void setPlayerChoice(int player){
        this.playerChoice = player;
    }

    public int getPlayerChoice() {
        return playerChoice;
    }

    public Square(){
        this.playerChoice = 0;
    }

    public String getStringOfSquare(){
        if (this.playerChoice == 0){
            return "-";
        }
        else{
            return playerChoice == 1?"X":"0";
        }
    }

}
