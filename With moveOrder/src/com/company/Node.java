package com.company;

public class Node {

    public Node (Piece piece, Tile toBeMoved) {
        this.piece = piece;
        this.toBeMoved = toBeMoved;
        uScore = 0;
    }

    public Node (Node copy) {
        this.piece = new Piece(copy.piece);
        this.toBeMoved = new Tile(copy.toBeMoved);
        uScore = copy.uScore;
    }

    public Piece getPiece () {
        return piece;
    }

    public Tile getToBeMoved () {
        return toBeMoved;
    }

    public int getuScore() {
        return uScore;
    }

    public void setScore(int score) {
        uScore = score;
    }

    private Piece piece;
    private Tile toBeMoved;
    private int uScore;
}
