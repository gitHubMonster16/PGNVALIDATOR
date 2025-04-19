package service.movement.PlayMove;

import models.Board;
import models.Piece;
import models.Square;
import service.movement.PlayMove.PlayMoveLogic;

import java.util.Map;
import java.util.HashMap;

public class CastleHandle implements PlayMoveLogic {
    private static final Map<String, CastlingData> CASTLING_MOVES = new HashMap<>();

    static {
        // White castling
        CASTLING_MOVES.put("g1", new CastlingData("e1", "g1", "h1", "f1"));
        CASTLING_MOVES.put("c1", new CastlingData("e1", "c1", "a1", "d1"));
        // Black castling
        CASTLING_MOVES.put("g8", new CastlingData("e8", "g8", "h8", "f8"));
        CASTLING_MOVES.put("c8", new CastlingData("e8", "c8", "a8", "d8"));
    }

    private final Piece king;
    public CastleHandle(Piece king) {
        this.king = king;
    }
    @Override
    public boolean playMoveTo(Board board, Square destination) {
        if (!king.getLegalMoves(board).contains(destination)) {
            return false;
        }
        String target = destination.getPosition().toAlgebraic();

        if (CASTLING_MOVES.containsKey(target)) {
            performCastling(board, CASTLING_MOVES.get(target));
        } else {
            performStandardMove(destination);
        }
        king.setHasMoved(true);
        return true;
    }

    private void performCastling(Board board, CastlingData data) {
        // Move King
        movePiece(board, data.kingFrom(), data.kingTo(), king);

        // Move Rook
        Piece rook = board.getSquare(data.rookFrom()).getOccupyingPiece();
        movePiece(board, data.rookFrom(), data.rookTo(), rook);
        rook.setHasMoved(true);
    }

    private void performStandardMove(Square destination) {
        Square current = king.getPosition();
        destination.put(king);
        current.setOccupyingPiece(null);
        king.setPosition(destination);
    }

    private void movePiece(Board board, String from, String to, Piece piece) {
        Square fromSquare = board.getSquare(from);
        Square toSquare = board.getSquare(to);

        toSquare.put(piece);
        fromSquare.setOccupyingPiece(null);
        piece.setPosition(toSquare);
    }

    private record CastlingData(
            String kingFrom,
            String kingTo,
            String rookFrom,
            String rookTo
    ) {}
}