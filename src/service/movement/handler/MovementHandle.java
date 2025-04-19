package service.movement.handler;

import models.Board;
import models.Piece;
import models.Square;
import java.util.ArrayList;
import java.util.List;

public class MovementHandle {
    private static final int[][] HORIZONTAL_VERTICAL = {
            {-1, 0}, {1, 0}, {0, -1}, {0, 1}
    };
    private static final int[][] DIAGONAL = {
            {-1, -1}, {1, -1}, {1, 1}, {-1, 1}
    };

    public static List<Square> diagonalMoves(Board board, Piece piece) {
        List<Square> legalMoves = new ArrayList<>(14); // Max 13 possible diagonal moves
        Square[][] currentBoard = board.getSquareArray();
        int x = piece.getPosition().getXNum();
        int y = piece.getPosition().getYNum();

        for (int[] direction : DIAGONAL) {
            int currentX = x + direction[0];
            int currentY = y + direction[1];

            while (isValidPosition(currentX, currentY)) {
                Square target = currentBoard[currentY][currentX];
                if (target.isOccupied()) {
                    if (target.getOccupyingPiece().getColor() != piece.getColor()) {
                        legalMoves.add(target);
                    }
                    break;
                }
                legalMoves.add(target);
                currentX += direction[0];
                currentY += direction[1];
            }
        }
        return legalMoves;
    }

    public static List<Square> getHorizontalVerticalMoves(Board board, Piece piece) {
        List<Square> legalMoves = new ArrayList<>(14); // Max 14 possible straight moves
        Square[][] currentBoard = board.getSquareArray();
        int x = piece.getPosition().getXNum();
        int y = piece.getPosition().getYNum();

        for (int[] direction : HORIZONTAL_VERTICAL) {
            int currentX = x + direction[0];
            int currentY = y + direction[1];
            while (isValidPosition(currentX, currentY)) {
                Square target = currentBoard[currentY][currentX];
                if (target.isOccupied()) {
                    if (target.getOccupyingPiece().getColor() != piece.getColor()) {
                        legalMoves.add(target);
                    }
                    break;
                }
                legalMoves.add(target);
                currentX += direction[0];
                currentY += direction[1];
            }
        }
        return legalMoves;
    }
    public static List<Square> getCombinedMoves(Board chessBoard, Piece piece) {
        // Pre-allocate with approximate capacity (28 = 14 straight + 14 diagonal)
        List<Square> moves = new ArrayList<>(28);
        moves.addAll(getHorizontalVerticalMoves(chessBoard, piece));
        moves.addAll(diagonalMoves(chessBoard, piece));
        return moves;
    }

    public static boolean isValidPosition(int x, int y) {
        return x >= 0 && y >= 0 && x < 8 && y < 8;
    }
}