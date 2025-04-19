package service.movement;

import models.Board;
import models.Piece;
import models.Square;
import service.movement.Interfaces_MoveAndPlay.MovementLogic;

import java.util.ArrayList;
import java.util.List;

public class StandardKnightMovement implements MovementLogic {
    private static final int[][] KNIGHT_MOVES = {
            {-2, -1}, {-2, 1},
            {-1, -2}, {-1, 2},
            {1, -2}, {1, 2},
            {2, -1}, {2, 1}
    };
    private final Piece knight;
    public StandardKnightMovement(Piece piece) {
        this.knight = piece;
    }
    @Override
    public List<Square> getLegalMoves(Board chessBoard) {
        List<Square> legalMoves = new ArrayList<>();
        Square[][] board = chessBoard.getSquareArray();
        int x = knight.getPosition().getXNum();
        int y = knight.getPosition().getYNum();
        for (int[] move : KNIGHT_MOVES) {
            int newY = y + move[0]; // Note: y first for row
            int newX = x + move[1]; // x second for column

            // Check if move is within board bounds
            if (newX >= 0 && newX < 8 && newY >= 0 && newY < 8) {
                Square targetSquare = board[newY][newX]; // Access as board[y][x]

                // Square is empty or contains opponent's piece
                if (!targetSquare.isOccupied() ||
                        targetSquare.getOccupyingPiece().getColor() != knight.getColor()) {
                    legalMoves.add(targetSquare);
                }
            }
        }
        return legalMoves;
    }
}