package service.movement;

import models.Board;
import models.Piece;
import models.Rook;
import models.Square;
import models.enums.Color_Piece;
import service.movement.Interfaces_MoveAndPlay.MovementLogic;

import java.util.ArrayList;
import java.util.List;

import static models.enums.Color_Piece.WHITE;
import static service.movement.handler.MovementHandle.isValidPosition;

public class StandardKingMovement implements MovementLogic {
    private static final int[][] KING_DIRECTIONS = {
            {-1, -1}, {-1, 0}, {-1, 1},
            {0, -1}, {0, 1},
            {1, -1}, {1, 0}, {1, 1}
    };

    private final Piece king;

    public StandardKingMovement(Piece piece) {
        this.king = piece;
    }

    @Override
    public List<Square> getLegalMoves(Board chessBoard) {
        List<Square> legalMoves = new ArrayList<>(10); // 8 normal + 2 castling moves
        Square[][] board = chessBoard.getSquareArray();
        int x = king.getPosition().getXNum();
        int y = king.getPosition().getYNum();

        // Add normal king moves
        for (int[] direction : KING_DIRECTIONS) {
            int newX = x + direction[0];
            int newY = y + direction[1];

            if (isValidPosition(newX, newY)) {
                Square target = board[newY][newX];
                if (!target.isOccupied() || target.getOccupyingPiece().getColor() != king.getColor()) {
                    legalMoves.add(target);
                }
            }
        }

        // Add castling moves if king hasn't moved
        if (!king.isHasMoved) {
            addCastlingMoves(chessBoard, legalMoves);
        }

        return legalMoves;
    }

    private void addCastlingMoves(Board board, List<Square> legalMoves) {
        String kingPos = king.getCurrentSquare().getPosition().toAlgebraic();
        Color_Piece color = king.getColor();

        // Kingside castling
        if (canCastle(board, kingPos, color, true)) {
            legalMoves.add(board.getSquare(color == WHITE ? "g1" : "g8"));
        }

        // Queenside castling
        if (canCastle(board, kingPos, color, false)) {
            legalMoves.add(board.getSquare(color == WHITE ? "c1" : "c8"));
        }
    }

    private boolean canCastle(Board board, String kingPos, Color_Piece color, boolean kingside) {
        if (!kingPos.equals(color == WHITE ? "e1" : "e8")) {
            return false;
        }

        String rookPos = kingside ? (color == WHITE ? "h1" : "h8")
                : (color == WHITE ? "a1" : "a8");
        String[] betweenSquares = kingside
                ? (color == WHITE ? new String[]{"f1", "g1"} : new String[]{"f8", "g8"})
                : (color == WHITE ? new String[]{"d1", "c1", "b1"} : new String[]{"d8", "c8", "b8"});

        Square rookSquare = board.getSquare(rookPos);
        Piece rook = rookSquare.getOccupyingPiece();
        // Check rook exists, hasn't moved, and is correct type
        if (rook == null || !(rook instanceof Rook) || rook.isHasMoved) {
            return false;
        }
        // Check path is clear
        for (String square : betweenSquares) {
            if (board.getSquare(square).isOccupied()) {
                return false;
            }
        }

        return true;
    }
}
