package com.company;

import java.util.ArrayList;
import java.util.Collections;

public class newAgent {
    public newAgent() {

    }

    public int minimax (Boolean player, Board board, int depth, int alpha, int beta) {
        if (depth == 0 || board.redWins() || board.whiteWins()) {
            return utility(board);
        }

        if (player) {
            Node[] humanMoves = getMoves(board, true);

            int min = Integer.MAX_VALUE;

            for (int i = 0; i < humanMoves.length; i++) {
                Board copy = new Board(board);
                Piece movePiece = copy.getTiles()[humanMoves[i].getPiece().getY()-1][humanMoves[i].getPiece().getX()-1].getPiece();

                simulateMove(copy, movePiece, humanMoves[i].getToBeMoved());

                int max = minimax(false, copy, depth-1, alpha, beta);
                min = Math.min(min, max);

                humanMoves[i].setScore(min);

                if (alpha > min)
                    break;

                beta = Math.min(min, beta);
            }

            return min;
        }
        else {
            Node[] aiMoves = getMoves(board, false);

            int max = Integer.MIN_VALUE;

            for (int i = 0; i < aiMoves.length; i++) {
                Board copy = new Board(board);
                Piece movePiece = copy.getTiles()[aiMoves[i].getPiece().getY()-1][aiMoves[i].getPiece().getX()-1].getPiece();

                simulateMove(copy, movePiece, aiMoves[i].getToBeMoved());


                int min = minimax(true, copy, depth-1, alpha, beta);

                aiMoves[i].setScore(min);

                max = Math.max(max, min);
                //System.out.println("At Depth #" + depth + ", max is " + max);

                if (beta < max)
                    break;

                alpha = Math.max(alpha, max);

            }

            return max;
        }
    }

    public void aiMove(Board board) {
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        Node[] availableMoves = getMoves(board, false);

        for (int i = 0; i < availableMoves.length; i++) {
            Board copy = new Board(board);
            Piece movePiece = copy.getTiles()[availableMoves[i].getPiece().getY()-1][availableMoves[i].getPiece().getX()-1].getPiece();

            simulateMove(copy, movePiece, availableMoves[i].getToBeMoved());

            /*
            System.out.println("Simulating possible Max move #" + (i+1));
            copy.printBoard();
            System.out.println("\n");


             */
            int score = minimax (true, copy, 4, alpha, beta);

            availableMoves[i].setScore(score);
        }

        int max = availableMoves[0].getuScore();
        Node nextMove = availableMoves[0];

        /*
        for (int k = 0; k < availableMoves.length; k++) {
            System.out.println("AFTER GETTING SCORE move #" + (k+1));
            System.out.println("Piece is at X:" + availableMoves[k].getPiece().getX() + " Y:" + availableMoves[k].getPiece().getY());
            System.out.println("Move to X:" + availableMoves[k].getToBeMoved().getX() + " Y:" + availableMoves[k].getToBeMoved().getY());
            System.out.println("Score is " + availableMoves[k].getuScore());
            System.out.print("\n");
        }

         */


        for (int j = 1; j < availableMoves.length; j++) {
            if (availableMoves[j].getuScore() > max) {
                max = Math.max(availableMoves[j].getuScore(), max);
                nextMove = availableMoves[j];
            }
        }

        int coordY = nextMove.getToBeMoved().getY() - 1;
        int coordX = nextMove.getToBeMoved().getX() - 1;

        System.out.println("White piece chosen is at X: " + nextMove.getPiece().getX() + " Y: " + nextMove.getPiece().getY());
        System.out.println("Chosen piece will move to X: " + nextMove.getToBeMoved().getX() + " Y: " + nextMove.getToBeMoved().getY());

        if (board.move(nextMove.getPiece(), board.getTiles()[coordY][coordX])) {
            board.canMultiJump(board.getTiles()[coordY][coordX].getPiece());
            board.changeTurn();
        }
    }

    public Node[] getMoves (Board board, boolean player) {
        Node[] moves = new Node[board.getAllMoves(player)];
        int j = 0;

        if (player) {
            for (int i = 0 ; i < board.getNumRed(); i++) {
                if (board.canMoveRightUp(board.getRed().get(i)) != null) {
                    moves[j] = new Node(board.getRed().get(i), board.canMoveRightUp(board.getRed().get(i)));
                    j++;
                }

                if (board.canMoveRightDown(board.getRed().get(i)) != null) {
                    moves[j] = new Node(board.getRed().get(i), board.canMoveRightDown(board.getRed().get(i)));
                    j++;
                }

                if (board.canMoveLeftUp(board.getRed().get(i)) != null) {
                    moves[j] = new Node(board.getRed().get(i), board.canMoveLeftUp(board.getRed().get(i)));
                    j++;
                }

                if (board.canMoveLeftDown(board.getRed().get(i)) != null) {
                    moves[j] = new Node(board.getRed().get(i), board.canMoveLeftDown(board.getRed().get(i)));
                    j++;
                }
            }
        }

        else {
            for (int i = 0 ; i < board.getNumWhite(); i++) {
                if (board.canMoveRightUp(board.getWhite().get(i)) != null) {
                    moves[j] = new Node(board.getWhite().get(i), board.canMoveRightUp(board.getWhite().get(i)));
                    j++;
                }

                if (board.canMoveRightDown(board.getWhite().get(i)) != null) {
                    moves[j] = new Node(board.getWhite().get(i), board.canMoveRightDown(board.getWhite().get(i)));
                    j++;
                }

                if (board.canMoveLeftUp(board.getWhite().get(i)) != null) {
                    moves[j] = new Node(board.getWhite().get(i), board.canMoveLeftUp(board.getWhite().get(i)));
                    j++;
                }

                if (board.canMoveLeftDown(board.getWhite().get(i)) != null) {
                    moves[j] = new Node(board.getWhite().get(i), board.canMoveLeftDown(board.getWhite().get(i)));
                    j++;
                }
            }
        }
        return moves;
    }

    public void simulateMove (Board copy, Piece copyPiece, Tile nextTile) {
        copy.getTiles()[nextTile.getY()-1][nextTile.getX()-1].setPiece(copyPiece);
        copy.getTiles()[copyPiece.getY() - 1][copyPiece.getX() - 1].deletePiece(); //Delete piece in old tile

        if (nextTile.getX() - copyPiece.getX() == 2 || nextTile.getX() - copyPiece.getX() == -2)
            copy.jumpedOver(copyPiece, nextTile);

        copyPiece.setX(nextTile.getX()); //set the new coordinates of the piece
        copyPiece.setY(nextTile.getY());

        if (copyPiece.getColor() && copyPiece.getY() == 1 && !copyPiece.getIsKing() || !copyPiece.getColor() && copyPiece.getY() == copy.getTiles().length && !copyPiece.getIsKing())
            copyPiece.setKing();
    }




    public int utility (Board board) {
        int diffPieces = board.getNumWhite() - board.getNumRed();
        int diffMoves = board.getAllMoves(false) - board.getAllMoves(true);
        int diffKings = board.getNumKings(false) - board.getNumKings(true);

        /*
        System.out.print("\n");
        System.out.println("Diff in Piece: " + diffPieces);
        System.out.println("Diff in Moves: " + diffMoves);
        System.out.println("Diff in Kings: " + diffKings);
        System.out.println("Returning: " + diffPieces*100 + " + " + diffMoves + " + " + diffKings*10 + " = " + (diffPieces*100 + diffMoves + diffKings*10));
        System.out.print("\n");

         */

        return diffPieces*100 + diffMoves + diffKings*10;
    }
}
