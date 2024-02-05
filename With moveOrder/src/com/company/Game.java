package com.company;

import java.util.Scanner;

public class Game {

    public Game() {
        board = new Board();
        //agent = new newAgent();
        //agent = new Agent();
        agent = new moveOrder();
    }

    public void game() {
        Scanner scan = new Scanner(System.in);
        String input;
        String[] nextTile;
        String[] piece;
        int pieceX;
        int pieceY;
        int coordX;
        int coordY;

        board.printBoard();

        while (!board.redWins() && !board.whiteWins()) {

            if (board.getTurn()) {
                System.out.println("Red's turn");
                System.out.println("Enter the piece you want to move (X Y): ");

                do {
                    input = scan.nextLine();
                    piece = input.split(" ");
                    pieceX = Integer.parseInt(piece[0]) - 1;
                    pieceY = Integer.parseInt(piece[1]) - 1;
                }
                while (!board.getTiles()[pieceY][pieceX].getHasPiece() || !board.getTiles()[pieceY][pieceX].getPiece().getColor());

                if (board.getTiles()[pieceY][pieceX].getHasPiece() && board.getTiles()[pieceY][pieceX].getPiece().getColor()) {
                    Piece toBeMoved = board.getTiles()[pieceY][pieceX].getPiece();

                    do {
                        System.out.println("Enter the tile where you want to move the red piece (X Y): ");
                        input = scan.nextLine();
                        nextTile = input.split(" ");
                        coordX = Integer.parseInt(nextTile[0]) - 1;
                        coordY = Integer.parseInt(nextTile[1]) - 1;

                        if (board.move(toBeMoved, board.getTiles()[coordY][coordX]))
                            board.printBoard();
                        System.out.println("\n");
                    }
                    while (board.canMultiJump(toBeMoved));

                    board.changeTurn();
                }

            }
            else {
                System.out.println("White's turn");
                agent.aiMove(board);
                System.out.println("\n");
                board.printBoard();
            }
        }
    }

    private moveOrder agent;
    //private Agent agent;
    private Board board;
}
