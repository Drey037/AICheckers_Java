package com.company;

import java.util.ArrayList;
import java.util.Scanner;

public class Board {

    public Board() {
        height = 8;
        width = 8;
        initPieces();
        initBoard();

        turn = true;
    }

    public Board(Board copy) {
        height = 8;
        width = 8;
        red = cloneArray(copy.red);
        white = cloneArray(copy.white);
        //red = new ArrayList<Piece>(copy.red);
        //white = new ArrayList<Piece>(copy.white);
        initBoard();
        turn = copy.turn;
    }

    public void initPieces() {
        red = new ArrayList<>();
        white = new ArrayList<>();

        red.add(new Piece(true, 1, 8));
        red.add(new Piece(true, 3, 8));
        red.add(new Piece(true, 5, 8));
        red.add(new Piece(true, 7, 8));
        red.add(new Piece(true, 2, 7));
        red.add(new Piece(true, 4, 7));
        red.add(new Piece(true, 6, 7));
        red.add(new Piece(true, 8, 7));
        red.add(new Piece(true, 1, 6));
        red.add(new Piece(true, 3, 6));
        red.add(new Piece(true, 5, 6));
        red.add(new Piece(true, 7, 6));

        white.add(new Piece(false, 2, 1));
        white.add(new Piece(false, 4, 1));
        white.add(new Piece(false, 6, 1));
        white.add(new Piece(false, 8, 1));
        white.add(new Piece(false, 1, 2));
        white.add(new Piece(false, 3, 2));
        white.add(new Piece(false, 5, 2));
        white.add(new Piece(false, 7, 2));
        white.add(new Piece(false, 2, 3));
        white.add(new Piece(false, 4, 3));
        white.add(new Piece(false, 6, 3));
        white.add(new Piece(false, 8, 3));
    }

    public void initBoard() {
        tiles = new Tile[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if ((i % 2 == 0 && j % 2 == 0) || (i % 2 == 1 && j % 2 == 1))
                    tiles[i][j] = new Tile(true, j+1, i+1);
                else
                    tiles[i][j] = new Tile(false, j+1, i+1);
            }
        }

        for (Piece value : white) {
            tiles[value.getY() - 1][value.getX() - 1].setPiece(value);
        }

        for (Piece piece : red) {
            tiles[piece.getY() - 1][piece.getX() - 1].setPiece(piece);
        }
    }

    public ArrayList<Piece> cloneArray(ArrayList<Piece> pieces) {
        ArrayList<Piece> clone = new ArrayList<Piece>(pieces.size());
        for (int i = 0; i < pieces.size(); i++) {
            clone.add(new Piece(pieces.get(i)));
        }
        return clone;
    }


    public boolean move(Piece piece, Tile nextTile) {
        int coordX = nextTile.getX() - 1; //Next X coordinate
        int coordY = nextTile.getY() - 1; //Next Y coordinate

        int oldCoordX = piece.getX() - 1; //Old X coordinate
        int oldCoordY = piece.getY() - 1; //Old Y coordinate


        if (canMoveRightUp(piece) == nextTile || canMoveRightDown(piece) == nextTile || canMoveLeftUp(piece) == nextTile || canMoveLeftDown(piece) == nextTile) {
            tiles[coordY][coordX].setPiece(piece); //Set piece in new tile
            tiles[oldCoordY][oldCoordX].deletePiece(); //Delete piece in old tile

            jumpedOver(piece, nextTile); //Check if Piece jumped over an opponent piece


            piece.setX(nextTile.getX()); //set the new coordinates of the piece
            piece.setY(nextTile.getY());

            if (piece.getColor() && piece.getY() == 1 && !piece.getIsKing() || !piece.getColor() && piece.getY() == height && !piece.getIsKing())
                piece.setKing();

            return true;
        }
        else {
            System.out.println("Invalid move");
            return false;
        }
    }

    public void jumpedOver (Piece piece, Tile nextTile) {
        if (nextTile.getX() - piece.getX() == 2 && nextTile.getY() - piece.getY() == 2 ) { //If the piece jumped downward right
            if (piece.getColor())
                white.remove(tiles[nextTile.getY() - 2][nextTile.getX() - 2].getPiece()); //Remove eaten piece from list
            else
                red.remove(tiles[nextTile.getY() - 2][nextTile.getX() - 2].getPiece()); //Remove eaten piece from list

            tiles[nextTile.getY() - 2][nextTile.getX() - 2].deletePiece(); //Delete piece in tile
        }

        else if (nextTile.getX() - piece.getX() == 2 && nextTile.getY() - piece.getY() == -2 ) { //If the piece jumped upward right
            if (piece.getColor())
                white.remove(tiles[nextTile.getY()][nextTile.getX() - 2].getPiece()); //Remove eaten piece from list
            else
                red.remove(tiles[nextTile.getY()][nextTile.getX() - 2].getPiece()); //Remove eaten piece from list

            tiles[nextTile.getY()][nextTile.getX() - 2].deletePiece(); //Delete piece in tile
        }

        else if (nextTile.getX() - piece.getX() == -2 && nextTile.getY() - piece.getY() == 2 ) { //If the piece jumped downward left
            if (piece.getColor())
                white.remove(tiles[nextTile.getY() - 2][nextTile.getX()].getPiece()); //Remove eaten piece from list
            else
                red.remove(tiles[nextTile.getY() - 2][nextTile.getX()].getPiece()); //Remove eaten piece from list

            tiles[nextTile.getY() - 2][nextTile.getX()].deletePiece(); //Delete piece in tile
        }

        else if (nextTile.getX() - piece.getX() == -2 && nextTile.getY() - piece.getY() == -2 ) { //If the piece jumped upward left
            if (piece.getColor())
                white.remove(tiles[nextTile.getY()][nextTile.getX()].getPiece()); //Remove eaten piece from list
            else
                red.remove(tiles[nextTile.getY()][nextTile.getX()].getPiece()); //Remove eaten piece from list

            tiles[nextTile.getY()][nextTile.getX()].deletePiece(); //Delete piece in tile
        }
    }

    public Tile canMoveRightUp(Piece piece) {
        int coordX = piece.getX(); //Going right
        int coordY = piece.getY() - 2; //Going up

        int jumpCoordY = piece.getY() - 3; //Jumping over tile upwards
        int jumpCoordX = piece.getX() + 1;

        //Invalid moves
        if  ((!piece.getColor() && !piece.getIsKing()) || coordY < 0 || coordY > height-1 || coordX < 0 || coordX > width-1 ||
                (tiles[coordY][coordX].getPiece() != null && tiles[coordY][coordX].getPiece().getColor() == piece.getColor())) {

            //System.out.println("Cannot Move upwards to the right");
            return null;
        }

        //Next tile is empty
        else if (!(tiles[coordY][coordX].getHasPiece())) {

            //System.out.println("Upward right tile is empty");
            return tiles[coordY][coordX];
        }

        //Next tile is opponent's piece and the next tile after jumping is empty
        else if (tiles[coordY][coordX].getHasPiece() && tiles[coordY][coordX].getPiece().getColor() != piece.getColor() &&
                jumpCoordX < width && jumpCoordX >= 0 && jumpCoordY < height && jumpCoordY >= 0 && !tiles[jumpCoordY][jumpCoordX].getHasPiece()) {

            //System.out.println("Piece can jump over upward right tile");
            return tiles[jumpCoordY][jumpCoordX];
        }
        else {

            //System.out.println("Cannot Move upwards to the right");
            return null;
        }
    }

    public Tile canMoveLeftUp(Piece piece) {
        int coordX = piece.getX() - 2; //Going left
        int coordY = piece.getY() - 2; //Going up

        int jumpCoordY = piece.getY() - 3; //Jumping over tile upwards
        int jumpCoordX = piece.getX() - 3;

        //Invalid moves
        if  ((!piece.getColor() && !piece.getIsKing()) || coordY < 0 || coordY > height-1 || coordX < 0 || coordX > width-1 ||
                (tiles[coordY][coordX].getPiece() != null && tiles[coordY][coordX].getPiece().getColor() == piece.getColor())) {

           //System.out.println("Cannot Move upwards to the left");
            return null;
        }

        //Next tile is empty
        else if (!(tiles[coordY][coordX].getHasPiece())) {

            //System.out.println("Upward left tile is empty");
            return tiles[coordY][coordX];
        }

        //Next tile is opponent's piece and the next tile after jumping is empty
        else if (tiles[coordY][coordX].getHasPiece() && tiles[coordY][coordX].getPiece().getColor() != piece.getColor() &&
                jumpCoordX < width && jumpCoordX >= 0 && jumpCoordY < height && jumpCoordY >= 0 && !tiles[jumpCoordY][jumpCoordX].getHasPiece()) {

            //System.out.println("Piece can jump over upward left tile");
            return tiles[jumpCoordY][jumpCoordX];
        }
        else {

            //System.out.println("Cannot Move upwards to the left");
            return null;
        }
    }

    public Tile canMoveRightDown(Piece piece) {
        int coordX = piece.getX(); //Going right
        int coordY = piece.getY(); //Going down

        int jumpCoordY = piece.getY() + 1; //Jumping over tile downwards
        int jumpCoordX = piece.getX() + 1;

        //Invalid moves
        if  ((piece.getColor() && !piece.getIsKing()) || coordY < 0 || coordY > height-1 || coordX < 0 || coordX > width-1 ||
                (tiles[coordY][coordX].getPiece() != null && tiles[coordY][coordX].getPiece().getColor() == piece.getColor())) {

            //System.out.println("Cannot Move downwards to the right");
            return null;
        }

        //Next tile is empty
        else if (!(tiles[coordY][coordX].getHasPiece())) {

            //System.out.println("Downward right tile is empty");
            return tiles[coordY][coordX];
        }

        //Next tile is opponent's piece and the next tile after jumping is empty
        else if (tiles[coordY][coordX].getHasPiece() && tiles[coordY][coordX].getPiece().getColor() != piece.getColor() &&
                jumpCoordX < width && jumpCoordX >= 0 && jumpCoordY < height && jumpCoordY >= 0 && !tiles[jumpCoordY][jumpCoordX].getHasPiece()) {

            //System.out.println("Piece can jump over downward right tile");
            return tiles[jumpCoordY][jumpCoordX];
        }
        else {
            //System.out.println("Cannot Move downwards to the right");
            return null;
        }
    }

    public Tile canMoveLeftDown(Piece piece) {
        int coordX = piece.getX() - 2; //Going left
        int coordY = piece.getY(); //Going down

        int jumpCoordY = piece.getY() + 1; //Jumping over tile downwards
        int jumpCoordX = piece.getX() - 3;

        //Invalid moves
        if  ((piece.getColor() && !piece.getIsKing()) || coordY < 0 || coordY > height-1 || coordX < 0 || coordX > width-1 ||
                (tiles[coordY][coordX].getPiece() != null && tiles[coordY][coordX].getPiece().getColor() == piece.getColor())) {

            //System.out.println("Cannot Move downwards to the left");
            return null;
        }

        //Next tile is empty
        else if (!(tiles[coordY][coordX].getHasPiece())) {

            //System.out.println("Downward left tile is empty");
            return tiles[coordY][coordX];
        }

        //Next tile is opponent's piece and the next tile after jumping is empty
        else if (tiles[coordY][coordX].getHasPiece() && tiles[coordY][coordX].getPiece().getColor() != piece.getColor() &&
                jumpCoordX < width && jumpCoordX >= 0 && jumpCoordY < height && jumpCoordY >= 0 && !tiles[jumpCoordY][jumpCoordX].getHasPiece()) {

            //System.out.println("Piece can jump over downward left tile");
            return tiles[jumpCoordY][jumpCoordX];
        }
        else {
            //System.out.println("Cannot Move downwards to the left");
            return null;
        }
    }

    public boolean canMultiJump (Piece piece) {

        return piece.getY() - 3 >= 0 && piece.getY() - 3 < height && piece.getX() + 1 >= 0 && piece.getX() + 1 < width && canMoveRightUp(piece) == tiles[piece.getY() - 3][piece.getX() + 1]
                || piece.getY() - 3 >= 0 && piece.getY() - 3 < height && piece.getX() - 3 >= 0 && piece.getX() - 3 < width && canMoveLeftUp(piece) == tiles[piece.getY() - 3][piece.getX() - 3]
                || piece.getY() + 1 >= 0 && piece.getY() + 1 < height && piece.getX() + 1 >= 0 && piece.getX() + 1 < width && canMoveRightDown(piece) == tiles[piece.getY() + 1][piece.getX() + 1]
                || piece.getY() + 1 >= 0 && piece.getY() + 1 < height && piece.getX() - 3 >= 0 && piece.getX() - 3 < width && canMoveLeftDown(piece) == tiles[piece.getY() + 1][piece.getX() - 3];
    }

    public boolean noMoves(boolean turn) {
        if (turn) {
            for (int i = 0; i < red.size(); i++) {
                if (canMoveRightUp(red.get(i)) != null || canMoveLeftUp(red.get(i)) != null || canMoveRightDown(red.get(i)) != null || canMoveLeftDown(red.get(i)) != null)
                    return false;
            }
        }
        else {
            for (int j = 0; j < white.size(); j++) {
                if (canMoveRightUp(white.get(j)) != null || canMoveLeftUp(white.get(j)) != null || canMoveRightDown(white.get(j)) != null || canMoveLeftDown(white.get(j)) != null)
                    return false;
            }
        }
        return true;
    }



    public boolean redWins() {

        if (white.isEmpty())
            System.out.println("White has no more pieces. Red wins!");

        if (noMoves(false))
            System.out.println("White has no more moves. Red wins!");

        return white.isEmpty() || noMoves(false);
    }

    public boolean whiteWins() {
        if (red.isEmpty())
            System.out.println("Red has no more pieces. White wins!");

        if (noMoves(true))
            System.out.println("Red has no more moves. White wins!");

        return red.isEmpty() || noMoves(true);
    }



    public void printBoard () {

        for (int i = 0; i < height; i++) {

            for (int j = 0; j < width; j++) {
                if (!tiles[i][j].getHasPiece() && tiles[i][j].getDark())
                    System.out.print("■ ");
                else if (!tiles[i][j].getHasPiece() && !tiles[i][j].getDark())
                    System.out.print("□ ");
                else if (tiles[i][j].getPiece().getColor() && !tiles[i][j].getPiece().getIsKing())
                    System.out.print("r ");
                else if (!tiles[i][j].getPiece().getColor() && !tiles[i][j].getPiece().getIsKing())
                    System.out.print("w ");
                else if (tiles[i][j].getPiece().getColor() && tiles[i][j].getPiece().getIsKing())
                    System.out.print("R ");
                else if (!tiles[i][j].getPiece().getColor() && tiles[i][j].getPiece().getIsKing())
                    System.out.print("W ");
            }
            System.out.print("\n");
        }
    }

    public int getAllMoves(boolean player) {
        ArrayList<Tile> result = new ArrayList<>();

        if (player) { //True is human
            for (int i = 0; i < red.size(); i++) {
                if (canMoveRightUp(red.get(i)) != null)
                    result.add(canMoveRightUp(red.get(i)));
                if (canMoveLeftUp(red.get(i)) != null)
                    result.add(canMoveLeftUp(red.get(i)));
                if (canMoveRightDown(red.get(i)) != null)
                    result.add(canMoveRightDown(red.get(i)));
                if (canMoveLeftDown(red.get(i)) != null)
                    result.add(canMoveLeftDown(red.get(i)));
            }
        } else {
            for (int i = 0; i < white.size(); i++) {
                if (canMoveRightUp(white.get(i)) != null)
                    result.add(canMoveRightUp(white.get(i)));
                if (canMoveLeftUp(white.get(i)) != null)
                    result.add(canMoveLeftUp(white.get(i)));
                if (canMoveRightDown(white.get(i)) != null)
                    result.add(canMoveRightDown(white.get(i)));
                if (canMoveLeftDown(white.get(i)) != null)
                    result.add(canMoveLeftDown(white.get(i)));
            }
        }
        return result.size();
    }

    public ArrayList<Tile> getPieceMoves(Piece piece) {
        ArrayList<Tile> result = new ArrayList<>();

        if (piece.getColor()) { //True is human
            for (int i = 0; i < red.size(); i++) {
                if (canMoveRightUp(red.get(i)) != null)
                    result.add(canMoveRightUp(red.get(i)));
                if (canMoveLeftUp(red.get(i)) != null)
                    result.add(canMoveLeftUp(red.get(i)));
                if (canMoveRightDown(red.get(i)) != null)
                    result.add(canMoveRightDown(red.get(i)));
                if (canMoveLeftDown(red.get(i)) != null)
                    result.add(canMoveLeftDown(red.get(i)));
            }
        } else {
            for (int i = 0; i < white.size(); i++) {
                if (canMoveRightUp(white.get(i)) != null)
                    result.add(canMoveRightUp(white.get(i)));
                if (canMoveLeftUp(white.get(i)) != null)
                    result.add(canMoveLeftUp(white.get(i)));
                if (canMoveRightDown(white.get(i)) != null)
                    result.add(canMoveRightDown(white.get(i)));
                if (canMoveLeftDown(white.get(i)) != null)
                    result.add(canMoveLeftDown(white.get(i)));
            }
        }
        return result;
    }

    public int getNumRed () {
        return red.size();
    }

    public int getNumWhite () {
        return white.size();
    }

    public ArrayList<Piece> getRed () {
        return red;
    }

    public ArrayList<Piece> getWhite () {
        return white;
    }

    public int getNumKings(boolean player) {
        int result = 0;

        if (player) {
            for (int i = 0; i < red.size(); i++) {
                if (red.get(i).getIsKing())
                    result += 1;
            }
        }
        else {
            for (int i = 0; i < white.size(); i++) {
                if (white.get(i).getIsKing())
                    result += 1;
            }
        }
        return result;
    }

    public void changeTurn() {
        turn = !turn;
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public boolean getTurn() {
        return turn;
    }

    public void setPieces(ArrayList<Piece> red, ArrayList<Piece> white) {
        red = red;
        white = white;
    }

    private Tile[][] tiles;
    private ArrayList<Piece> red;
    private ArrayList<Piece> white;
    private boolean turn;
    private final int height;
    private final int width;
}
