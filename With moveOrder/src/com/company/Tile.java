package com.company;

public class Tile {
    public Tile(boolean dark, int x, int y) {
        hasPiece = false;
        piece = null;
        this.dark=dark;
        this.x = x;
        this.y = y;
    }

    public Tile (Tile copy) {
        hasPiece = copy.hasPiece;

        if (hasPiece)
            piece = new Piece (copy.piece);

        else
            piece = null;

        this.dark = copy.dark;
        this.x = copy.x;
        this.y = copy.y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Piece getPiece() {
        return piece;
    }

    public boolean getHasPiece() {
        return hasPiece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
        hasPiece = true;
    }

    public boolean getDark() {
        return dark;
    }

    public void deletePiece() {
        piece = null;
        hasPiece = false;
    }

    private boolean dark;
    private boolean hasPiece;
    private Piece piece;
    final private int x;
    final private int y;
}

