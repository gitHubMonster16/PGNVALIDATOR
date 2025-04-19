package service.checkmateDetector;

import models.Board;
import models.King;
import models.Piece;
import models.Square;
import java.util.*;

public class CheckDetector {
    private final Board board;
    private final Map<Square, List<Piece>> whiteAttacks;
    private final Map<Square, List<Piece>> blackAttacks;
    private final Square[][] boardSquares;

    public CheckDetector(Board board) {
        this.board = board;
        this.whiteAttacks = new HashMap<>(64);
        this.blackAttacks = new HashMap<>(64);
        this.boardSquares = board.getSquareArray();
        initializeAttackMaps();
    }

    private void initializeAttackMaps() {
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Square square = boardSquares[y][x];
                whiteAttacks.put(square, new ArrayList<>(3));
                blackAttacks.put(square, new ArrayList<>(3));
            }
        }
    }

    public void updateAttackMaps(List<Piece> whitePieces, List<Piece> blackPieces) {
        clearAttackMaps();
        updatePieceAttacks(whitePieces, whiteAttacks);
        updatePieceAttacks(blackPieces, blackAttacks);
    }

    private void clearAttackMaps() {
        whiteAttacks.values().forEach(List::clear);
        blackAttacks.values().forEach(List::clear);
    }

    private void updatePieceAttacks(List<Piece> pieces, Map<Square, List<Piece>> attackMap) {
        pieces.removeIf(piece -> piece.getPosition() == null);

        pieces.stream()
                .filter(piece -> !(piece instanceof King))
                .forEach(piece -> {
                    List<Square> legalMoves = piece.getLegalMoves(board);
                    legalMoves.forEach(move -> attackMap.get(move).add(piece));
                });
    }

    public boolean isKingInCheck(King king, boolean isWhite) {
        Square kingSquare = king.getPosition();
        return isWhite ? !blackAttacks.get(kingSquare).isEmpty()
                : !whiteAttacks.get(kingSquare).isEmpty();
    }
    public Map<Square, List<Piece>> getWhiteAttacks() {
        return Collections.unmodifiableMap(whiteAttacks);
    }
    public Map<Square, List<Piece>> getBlackAttacks() {
        return Collections.unmodifiableMap(blackAttacks);
    }
}
