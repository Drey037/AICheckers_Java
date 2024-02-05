package com.company;

import java.util.ArrayList;
import java.util.Collections;

public class Agent {

    public Agent() {
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

        if (nextTile.getX() - copyPiece.getX() == 2)
            copy.jumpedOver(copyPiece, nextTile);

        copyPiece.setX(nextTile.getX()); //set the new coordinates of the piece
        copyPiece.setY(nextTile.getY());

        if (copyPiece.getColor() && copyPiece.getY() == 1 && !copyPiece.getIsKing() || !copyPiece.getColor() && copyPiece.getY() == copy.getTiles().length && !copyPiece.getIsKing())
            copyPiece.setKing();
    }

    //Gets best move for computer
    public Node minimax (Board board, int depth) {
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        Node[] aiMoves = getMoves(board, false);

        int max = maxVal(board, depth, alpha, beta, aiMoves);

        ArrayList<Node> bestMoves = new ArrayList<Node>();

        System.out.println("Max value is " + max);

        for (int i = 0; i < aiMoves.length; i++) {
            System.out.println("Possible move# " + (i+1));
            System.out.println("Piece is at X:" + aiMoves[i].getPiece().getX() + " Y:" + aiMoves[i].getPiece().getY());
            System.out.println("Piece will move to X:" + aiMoves[i].getToBeMoved().getX() + " Y:" + aiMoves[i].getToBeMoved().getY());
            System.out.println("Utility value is " + aiMoves[i].getuScore());
            System.out.println("\n");
        }

        for (int i = 0; i < aiMoves.length; i++) {
            if (aiMoves[i].getuScore() == max)
                bestMoves.add(aiMoves[i]);
        }

        if (bestMoves.size() > 1)
            Collections.shuffle(bestMoves);
        return bestMoves.get(0);
    }

    public int maxVal (Board board, int depth, int alpha, int beta, Node[] aiMoves) {
        int maximum = Integer.MIN_VALUE;

        if (depth == 0 || board.redWins() || board.whiteWins())
            return utility(board);

        for (int i = 0; i < aiMoves.length; i++) {
            Board copy = new Board(board);
            Piece movePiece = copy.getTiles()[aiMoves[i].getPiece().getY()-1][aiMoves[i].getPiece().getX()-1].getPiece();

            simulateMove(copy, movePiece, aiMoves[i].getToBeMoved());

            Node[] humanMoves = getMoves(copy, true);
            int min = minVal(copy, depth-1, alpha, beta, humanMoves);

            aiMoves[i].setScore(min);
            maximum = Math.max(maximum, min);

            if (beta < maximum)
                break;

            alpha = Math.max(alpha, maximum);
        }

        return maximum;
    }

    public int minVal (Board board, int depth, int alpha, int beta, Node[] humanMoves) {
        int min = Integer.MAX_VALUE;

        if (depth == 0 || board.redWins() || board.whiteWins())
            return utility(board);

        for (int i = 0; i < humanMoves.length; i++) {
            Board copy = new Board(board);
            Piece movePiece = copy.getTiles()[humanMoves[i].getPiece().getY()-1][humanMoves[i].getPiece().getX()-1].getPiece();
            simulateMove(copy, movePiece, humanMoves[i].getToBeMoved());

            Node[] aimove = getMoves(copy, false);

            int max = maxVal(copy, depth-1, alpha, beta, aimove);

            min = Math.min(min, max);

            humanMoves[i].setScore(min);

            if (alpha > min)
                break;

            beta = Math.min(min, beta);
        }

        return min;
    }



    public void aiMove(Board board) {
        Board copy = new Board(board);
        Node nextMove = minimax(copy, 4);
        int coordY = nextMove.getToBeMoved().getY() - 1;
        int coordX = nextMove.getToBeMoved().getX() - 1;
        Piece piece = board.getTiles()[nextMove.getPiece().getY()-1][nextMove.getPiece().getX()-1].getPiece();

        System.out.println("Piece chosen is at X: " + nextMove.getPiece().getX() + " Y: " + nextMove.getPiece().getY());
        System.out.println("Piece will move to X: " + nextMove.getToBeMoved().getX() + " Y: " + nextMove.getToBeMoved().getY());

        if (board.move(piece, board.getTiles()[coordY][coordX])) {
            board.canMultiJump(board.getTiles()[coordY][coordX].getPiece());
            board.changeTurn();
            board.printBoard();
        }
    }


    //This method calculates the utility weight of the board
    //Difference in Pieces
    public int utility (Board board) {

        int diffPieces = board.getNumWhite() - board.getNumRed();
        int diffMoves = board.getAllMoves(false) - board.getAllMoves(true);
        int diffKings = board.getNumKings(false) - board.getNumKings(true);

        return diffPieces + diffMoves + diffKings;
    }




}
