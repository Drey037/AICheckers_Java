package com.company;

public class Piece {

    public Piece(boolean color, int x, int y) {
        this.color=color;
        this.x=x;
        this.y=y;
        isKing = false;
    }

    public Piece (Piece copy) {
        this.color= copy.color;
        this.x = copy.x;
        this.y = copy.y;
        this.isKing = copy.getIsKing();
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean getIsKing() {
        return isKing;
    }

    public boolean getColor() {
        return color;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setKing () {
        isKing = true;
    }

    private boolean isKing;
    private boolean color;
    private int x;
    private int y;
}

