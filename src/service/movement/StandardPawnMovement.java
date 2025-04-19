package service.movement;

import models.Board;

import models.Piece;
import models.Square;
import models.enums.Color_Piece;
import service.movement.Interfaces_MoveAndPlay.MovementLogic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class StandardPawnMovement implements MovementLogic {
    public final Piece pawn;
    public boolean wasMoved;
    public StandardPawnMovement(Piece piece, boolean wasMoved){
        this.pawn=piece;
        this.wasMoved=wasMoved;
    }
    @Override
    public List<Square> getLegalMoves(Board board) {
        List<Square> legalMoves = new ArrayList<>();
        Square[][] squares = board.getSquareArray();
        int x = this.pawn.getPosition().getXNum();
        int y = this.pawn.getPosition().getYNum();
        Color_Piece color = this.pawn.getColor();

        // Determine movement direction based on color
        int forwardDir = (color == Color_Piece.WHITE) ? -1 : 1;

        // Forward moves
        addForwardMoves(legalMoves, squares, x, y, forwardDir);

        // Capture moves
        addCaptureMoves(legalMoves, squares, x, y, forwardDir, color);

        // En passant (would need board to track last move)
        // addEnPassantMoves(legalMoves, board, x, y, forwardDir, color);
        return legalMoves;
    }

    private void addForwardMoves(List<Square> legalMoves, Square[][] squares,
                                 int x, int y, int forwardDir) {
        // Single move forward
        int newY = y + forwardDir;
        if (isValidSquare(x, newY) && !squares[newY][x].isOccupied()) {
            legalMoves.add(squares[newY][x]);

            // Double move from starting position
            if (!wasMoved) {
                newY = y + 2 * forwardDir;
                if (isValidSquare(x, newY) && !squares[newY][x].isOccupied()) {
                    legalMoves.add(squares[newY][x]);
                }
            }
        }
    }

    private void addCaptureMoves(List<Square> legalMoves, Square[][] squares,
                                 int x, int y, int forwardDir, Color_Piece color) {
        // Diagonal captures
        int[] captureXs = {x - 1, x + 1};
        int newY = y + forwardDir;

        for (int captureX : captureXs) {
            if (isValidSquare(captureX, newY)) {
                Square target = squares[newY][captureX];
                if (target.isOccupied() && target.getOccupyingPiece().getColor() != color) {
                    legalMoves.add(target);
                }
                // Here you could also check en passant
            }
        }
    }

    private boolean isValidSquare(int x, int y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }
}
