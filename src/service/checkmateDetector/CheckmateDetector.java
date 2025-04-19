//package models;
//
//import models.Bishop;
//import models.King;
//import models.enums.Color_Piece;
//
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ConcurrentLinkedDeque;
//
//
///**
// * Component of the Chess game that detects check mates in the game.
// *
// * @author Jussi Lundstedt
// *
// */
//public class CheckmateDetector {
//    private Board b;
//    private LinkedList<Piece> wPieces;
//    private LinkedList<Piece> bPieces;
//    private LinkedList<Square> movableSquares;
//    private final LinkedList<Square> squares;
//    private King bk;
//    private King wk;
//    private HashMap<Square,List<Piece>> wMoves;
//    private HashMap<Square,List<Piece>> bMoves;
//
//    /**
//     * Constructs a new instance of service.checkmateDetector.CheckmateDetector on a given board. By
//     * convention should be called when the board is in its initial state.
//     *
//     * @param b The board which the detector monitors
//     * @param wPieces White pieces on the board.
//     * @param bPieces Black pieces on the board.
//     * @param wk models.Piece object representing the white king
//     * @param bk models.Piece object representing the black king
//     */
//    public CheckmateDetector(Board b, LinkedList<Piece> wPieces,
//                             LinkedList<Piece> bPieces, King wk, King bk) {
//        this.b = b;
//        this.wPieces = wPieces;
//        this.bPieces = bPieces;
//        this.bk = bk;
//        this.wk = wk;
//
//        // Initialize other fields
//        squares = new LinkedList<Square>();
//        movableSquares = new LinkedList<Square>();
//        wMoves = new HashMap<Square,List<Piece>>();
//        bMoves = new HashMap<Square,List<Piece>>();
//
//        Square[][] brd = b.getSquareArray();
//
//        // add all squares to squares list and as hashmap keys
//        for (int x = 0; x < 8; x++) {
//            for (int y = 0; y < 8; y++) {
//                squares.add(brd[y][x]);
//                wMoves.put(brd[y][x], new LinkedList<Piece>());
//                bMoves.put(brd[y][x], new LinkedList<Piece>());
//            }
//        }
//
//        // update situation
//        update();
//    }
//
//    /**
//     * Updates the object with the current situation of the game.
//     */
//    public void update() {
//        // Iterators through pieces
//        Iterator<Piece> wIter = wPieces.iterator();
//        Iterator<Piece> bIter = bPieces.iterator();
//
//        // empty moves and movable squares at each update
//        for (List<Piece> pieces : wMoves.values()) {
//            pieces.removeAll(pieces);
//        }
//
//        for (List<Piece> pieces : bMoves.values()) {
//            pieces.removeAll(pieces);
//        }
//
//        movableSquares.removeAll(movableSquares);
//
//        // Add each move white and black can make to map
//        while (wIter.hasNext()) {
//            Piece p = wIter.next();
//
//            if (!p.getClass().equals(King.class)) {
//                if (p.getPosition() == null) {
//                    wIter.remove();
//                    continue;
//                }
//
//                List<Square> mvs = p.getLegalMoves(b);
//                Iterator<Square> iter = mvs.iterator();
//                while (iter.hasNext()) {
//                    List<Piece> pieces = wMoves.get(iter.next());
//                    pieces.add(p);
//                }
//            }
//        }
//
//        while (bIter.hasNext()) {
//            Piece p = bIter.next();
//            if (!p.getClass().equals(King.class)) {
//                if (p.getPosition() == null) {
//                    wIter.remove();
//                    continue;
//                }
//
//                List<Square> mvs = p.getLegalMoves(b);
//                Iterator<Square> iter = mvs.iterator();
//                while (iter.hasNext()) {
//                    List<Piece> pieces = bMoves.get(iter.next());
//                    pieces.add(p);
//                }
//            }
//        }
//    }
//
//    /**
//     * Checks if the black king is threatened
//     * @return boolean representing whether the black king is in check.
//     */
//    public boolean blackInCheck() {
//        update();
//        Square sq = bk.getPosition();
//        if (wMoves.get(sq).isEmpty()) {
//            movableSquares.addAll(squares);
//            return false;
//        } else return true;
//    }
//
//    /**
//     * Checks if the white king is threatened
//     * @return boolean representing whether the white king is in check.
//     */
//    public boolean whiteInCheck() {
//        update();
//        Square sq = wk.getPosition();
//        if (bMoves.get(sq).isEmpty()) {
//            movableSquares.addAll(squares);
//            return false;
//        } else return true;
//    }
//
//    /**
//     * Checks whether black is in checkmate.
//     * @return boolean representing if black player is checkmated.
//     */
//    public boolean blackCheckMated() {
//        boolean checkmate = true;
//        // Check if black is in check
//        if (!this.blackInCheck()) return false;
//
//        // If yes, check if king can evade
//        if (canEvade(wMoves, bk)) checkmate = false;
//
//        // If no, check if threat can be captured
//        List<Piece> threats = wMoves.get(bk.getPosition());
//        if (canCapture(bMoves, threats, bk)) checkmate = false;
//
//        // If no, check if threat can be blocked
//        if (canBlock(threats, bMoves, bk)) checkmate = false;
//
//        // If no possible ways of removing check, checkmate occurred
//        return checkmate;
//    }
//
//    /**
//     * Checks whether white is in checkmate.
//     * @return boolean representing if white player is checkmated.
//     */
//    public boolean whiteCheckMated() {
//        boolean checkmate = true;
//        // Check if white is in check
//        if (!this.whiteInCheck()) return false;
//
//        // If yes, check if king can evade
//        if (canEvade(bMoves, wk)) checkmate = false;
//
//        // If no, check if threat can be captured
//        List<Piece> threats = bMoves.get(wk.getPosition());
//        if (canCapture(wMoves, threats, wk)) checkmate = false;
//
//        // If no, check if threat can be blocked
//        if (canBlock(threats, wMoves, wk)) checkmate = false;
//
//        // If no possible ways of removing check, checkmate occurred
//        return checkmate;
//    }
//
//    /*
//     * Helper method to determine if the king can evade the check.
//     * Gives a false positive if the king can capture the checking piece.
//     */
//    private boolean canEvade(Map<Square,List<Piece>> tMoves, King tKing) {
//        boolean evade = false;
//        List<Square> kingsMoves = tKing.getLegalMoves(b);
//        Iterator<Square> iterator = kingsMoves.iterator();
//
//        // If king is not threatened at some square, it can evade
//        while (iterator.hasNext()) {
//            Square sq = iterator.next();
//            if (!testMove(tKing, sq)) continue;
//            if (tMoves.get(sq).isEmpty()) {
//                movableSquares.add(sq);
//                evade = true;
//            }
//        }
//
//        return evade;
//    }
//
//    /*
//     * Helper method to determine if the threatening piece can be captured.
//     */
//    private boolean canCapture(Map<Square,List<Piece>> poss,
//                               List<Piece> threats, King k) {
//
//        boolean capture = false;
//        if (threats.size() == 1) {
//            Square sq = threats.get(0).getPosition();
//
//            if (k.getLegalMoves(b).contains(sq)) {
//                movableSquares.add(sq);
//                if (testMove(k, sq)) {
//                    capture = true;
//                }
//            }
//
//            List<Piece> caps = poss.get(sq);
//            ConcurrentLinkedDeque<Piece> capturers = new ConcurrentLinkedDeque<Piece>();
//            capturers.addAll(caps);
//
//            if (!capturers.isEmpty()) {
//                movableSquares.add(sq);
//                for (Piece p : capturers) {
//                    if (testMove(p, sq)) {
//                        capture = true;
//                    }
//                }
//            }
//        }
//
//        return capture;
//    }
//
//    /*
//     * Helper method to determine if check can be blocked by a piece.
//     */
//    private boolean canBlock(List<Piece> threats,
//                             Map <Square,List<Piece>> blockMoves, King k) {
//        boolean blockable = false;
//
//        if (threats.size() == 1) {
//            Square ts = threats.get(0).getPosition();
//            Square ks = k.getPosition();
//            Square[][] brdArray = b.getSquareArray();
//
//            if (ks.getXNum() == ts.getXNum()) {
//                int max = Math.max(ks.getYNum(), ts.getYNum());
//                int min = Math.min(ks.getYNum(), ts.getYNum());
//
//                for (int i = min + 1; i < max; i++) {
//                    List<Piece> blks =
//                            blockMoves.get(brdArray[i][ks.getXNum()]);
//                    ConcurrentLinkedDeque<Piece> blockers =
//                            new ConcurrentLinkedDeque<Piece>();
//                    blockers.addAll(blks);
//
//                    if (!blockers.isEmpty()) {
//                        movableSquares.add(brdArray[i][ks.getXNum()]);
//
//                        for (Piece p : blockers) {
//                            if (testMove(p,brdArray[i][ks.getXNum()])) {
//                                blockable = true;
//                            }
//                        }
//
//                    }
//                }
//            }
//
//            if (ks.getYNum() == ts.getYNum()) {
//                int max = Math.max(ks.getXNum(), ts.getXNum());
//                int min = Math.min(ks.getXNum(), ts.getXNum());
//
//                for (int i = min + 1; i < max; i++) {
//                    List<Piece> blks =
//                            blockMoves.get(brdArray[ks.getYNum()][i]);
//                    ConcurrentLinkedDeque<Piece> blockers =
//                            new ConcurrentLinkedDeque<Piece>();
//                    blockers.addAll(blks);
//
//                    if (!blockers.isEmpty()) {
//
//                        movableSquares.add(brdArray[ks.getYNum()][i]);
//
//                        for (Piece p : blockers) {
//                            if (testMove(p, brdArray[ks.getYNum()][i])) {
//                                blockable = true;
//                            }
//                        }
//
//                    }
//                }
//            }
//
//            Class<? extends Piece> tC = threats.get(0).getClass();
//
//            if (tC.equals(Queen.class) || tC.equals(Bishop.class)) {
//                int kX = ks.getXNum();
//                int kY = ks.getYNum();
//                int tX = ts.getXNum();
//                int tY = ts.getYNum();
//
//                if (kX > tX && kY > tY) {
//                    for (int i = tX + 1; i < kX; i++) {
//                        tY++;
//                        List<Piece> blks =
//                                blockMoves.get(brdArray[tY][i]);
//                        ConcurrentLinkedDeque<Piece> blockers =
//                                new ConcurrentLinkedDeque<Piece>();
//                        blockers.addAll(blks);
//
//                        if (!blockers.isEmpty()) {
//                            movableSquares.add(brdArray[tY][i]);
//
//                            for (Piece p : blockers) {
//                                if (testMove(p, brdArray[tY][i])) {
//                                    blockable = true;
//                                }
//                            }
//                        }
//                    }
//                }
//
//                if (kX > tX && tY > kY) {
//                    for (int i = tX + 1; i < kX; i++) {
//                        tY--;
//                        List<Piece> blks =
//                                blockMoves.get(brdArray[tY][i]);
//                        ConcurrentLinkedDeque<Piece> blockers =
//                                new ConcurrentLinkedDeque<Piece>();
//                        blockers.addAll(blks);
//
//                        if (!blockers.isEmpty()) {
//                            movableSquares.add(brdArray[tY][i]);
//
//                            for (Piece p : blockers) {
//                                if (testMove(p, brdArray[tY][i])) {
//                                    blockable = true;
//                                }
//                            }
//                        }
//                    }
//                }
//
//                if (tX > kX && kY > tY) {
//                    for (int i = tX - 1; i > kX; i--) {
//                        tY++;
//                        List<Piece> blks =
//                                blockMoves.get(brdArray[tY][i]);
//                        ConcurrentLinkedDeque<Piece> blockers =
//                                new ConcurrentLinkedDeque<Piece>();
//                        blockers.addAll(blks);
//
//                        if (!blockers.isEmpty()) {
//                            movableSquares.add(brdArray[tY][i]);
//
//                            for (Piece p : blockers) {
//                                if (testMove(p, brdArray[tY][i])) {
//                                    blockable = true;
//                                }
//                            }
//                        }
//                    }
//                }
//
//                if (tX > kX && tY > kY) {
//                    for (int i = tX - 1; i > kX; i--) {
//                        tY--;
//                        List<Piece> blks =
//                                blockMoves.get(brdArray[tY][i]);
//                        ConcurrentLinkedDeque<Piece> blockers =
//                                new ConcurrentLinkedDeque<Piece>();
//                        blockers.addAll(blks);
//
//                        if (!blockers.isEmpty()) {
//                            movableSquares.add(brdArray[tY][i]);
//
//                            for (Piece p : blockers) {
//                                if (testMove(p, brdArray[tY][i])) {
//                                    blockable = true;
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        return blockable;
//    }
//
//    /**
//     * Method to get a list of allowable squares that the player can move.
//     * Defaults to all squares, but limits available squares if player is in
//     * check.
//     * @param b boolean representing whether it's white player's turn (if yes,
//     * true)
//     * @return List of squares that the player can move into.
//     */
//    public List<Square> getAllowableSquares(boolean b) {
//        movableSquares.removeAll(movableSquares);
//        if (whiteInCheck()) {
//            whiteCheckMated();
//        } else if (blackInCheck()) {
//            blackCheckMated();
//        }
//        return movableSquares;
//    }
//
//    /**
//     * Tests a move a player is about to make to prevent making an illegal move
//     * that puts the player in check.
//     * @param p models.Piece moved
//     * @param sq models.Square to which p is about to move
//     * @return false if move would cause a check
//     */
//    public boolean testMove(Piece p, Square sq) {
//        Piece c = sq.getOccupyingPiece();
//
//        boolean movetest = true;
//        Square init = p.getPosition();
//
//        p.move(sq);
//        update();
//
//        if (p.getColor() == Color_Piece.BLACK && blackInCheck()) movetest = false;
//        else if (p.getColor() ==Color_Piece.WHITE && whiteInCheck()) movetest = false;
//
//        p.move(init);
//        if (c != null) sq.put(c);
//        update();
//        movableSquares.addAll(squares);
//        return movetest;
//    }
//
//}
package service.checkmateDetector;

import models.Board;
import models.King;
import models.Piece;
import models.Square;
import models.enums.Color_Piece;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

public class CheckmateDetector {
    private final Board board;
    private final List<Piece> whitePieces;
    private final List<Piece> blackPieces;
    private final Set<Square> movableSquares;
    private final King whiteKing;
    private final King blackKing;
    private final Map<Square, List<Piece>> whiteAttacks;
    private final Map<Square, List<Piece>> blackAttacks;
    private final Square[][] boardSquares;

    public CheckmateDetector(Board board, List<Piece> whitePieces,
                             List<Piece> blackPieces, King whiteKing, King blackKing) {
        this.board = board;
        this.whitePieces = new ArrayList<>(whitePieces);
        this.blackPieces = new ArrayList<>(blackPieces);
        this.whiteKing = whiteKing;
        this.blackKing = blackKing;
        this.movableSquares = new HashSet<>(64);
        this.whiteAttacks = new HashMap<>(64);
        this.blackAttacks = new HashMap<>(64);
        this.boardSquares = board.getSquareArray();

        initializeAttackMaps();
        update();
    }

    public Map<Square, List<Piece>> getBlackAttacks() {
        return blackAttacks;
    }

    public Map<Square, List<Piece>> getWhiteAttacks() {
        return whiteAttacks;
    }

    private void initializeAttackMaps() {
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Square square = boardSquares[y][x];
                whiteAttacks.put(square, new ArrayList<>(3)); // Average attacks per square
                blackAttacks.put(square, new ArrayList<>(3));
            }
        }
    }

    public void update() {
        clearAttackMaps();
        updatePieceAttacks(whitePieces, whiteAttacks);
        updatePieceAttacks(blackPieces, blackAttacks);
    }

    private void clearAttackMaps() {
        for (List<Piece> attacks : whiteAttacks.values()) {
            attacks.clear();
        }
        for (List<Piece> attacks : blackAttacks.values()) {
            attacks.clear();
        }
        movableSquares.clear();
    }

    private void updatePieceAttacks(List<Piece> pieces, Map<Square, List<Piece>> attackMap) {
        Iterator<Piece> iterator = pieces.iterator();
        while (iterator.hasNext()) {
            Piece piece = iterator.next();
            if (piece.getPosition() == null) {
                iterator.remove();
                continue;
            }

            if (!(piece instanceof King)) {
                for (Square move : piece.getLegalMoves(board)) {
                    attackMap.get(move).add(piece);
                }
            }
        }
    }

    public boolean isKingInCheck(King king, Map<Square, List<Piece>> opponentAttacks) {
        Square kingSquare = king.getPosition();
        boolean inCheck = !opponentAttacks.get(kingSquare).isEmpty();
        if (!inCheck) {
            movableSquares.addAll(Arrays.asList(boardSquares).stream()
                    .flatMap(Arrays::stream)
                    .collect(Collectors.toList()));
        }
        return inCheck;
    }

    public boolean isCheckmated(King king, Map<Square, List<Piece>> opponentAttacks,
                                Map<Square, List<Piece>> allyAttacks) {
        if (!isKingInCheck(king, opponentAttacks)) return false;

        return !canKingEvade(king, opponentAttacks) &&
                !canCaptureThreat(king, opponentAttacks.get(king.getPosition()), allyAttacks) &&
                !canBlockThreat(king, opponentAttacks.get(king.getPosition()), allyAttacks);
    }

    public boolean canKingEvade(King king, Map<Square, List<Piece>> opponentAttacks) {
        return king.getLegalMoves(board).stream()
                .anyMatch(sq -> opponentAttacks.get(sq).isEmpty() && testMove(king, sq));
    }

    public boolean canCaptureThreat(King king, List<Piece> threats,
                                     Map<Square, List<Piece>> allyAttacks) {
        if (threats.size() != 1) return false;

        Square threatSquare = threats.get(0).getPosition();
        boolean kingCanCapture = king.getLegalMoves(board).contains(threatSquare) &&
                testMove(king, threatSquare);
        boolean allyCanCapture = allyAttacks.get(threatSquare).stream()
                .anyMatch(p -> testMove(p, threatSquare));

        if (kingCanCapture || allyCanCapture) {
            movableSquares.add(threatSquare);
            return true;
        }
        return false;
    }

    public boolean canBlockThreat(King king, List<Piece> threats,
                                   Map<Square, List<Piece>> allyAttacks) {
        if (threats.size() != 1) return false;

        Piece threat = threats.get(0);
        Square kingSquare = king.getPosition();
        Square threatSquare = threat.getPosition();

        // Get squares between king and threat
        List<Square> blockingSquares = getSquaresBetween(kingSquare, threatSquare);

        return blockingSquares.stream()
                .anyMatch(sq -> allyAttacks.get(sq).stream()
                        .anyMatch(p -> testMove(p, sq)));
    }

    public List<Square> getSquaresBetween(Square start, Square end) {
        List<Square> squares = new ArrayList<>();
        int x1 = start.getXNum(), y1 = start.getYNum();
        int x2 = end.getXNum(), y2 = end.getYNum();

        if (x1 == x2) { // Vertical line
            int minY = Math.min(y1, y2);
            int maxY = Math.max(y1, y2);
            for (int y = minY + 1; y < maxY; y++) {
                squares.add(boardSquares[y][x1]);
            }
        } else if (y1 == y2) { // Horizontal line
            int minX = Math.min(x1, x2);
            int maxX = Math.max(x1, x2);
            for (int x = minX + 1; x < maxX; x++) {
                squares.add(boardSquares[y1][x]);
            }
        } else if (Math.abs(x1 - x2) == Math.abs(y1 - y2)) { // Diagonal
            int xStep = x1 < x2 ? 1 : -1;
            int yStep = y1 < y2 ? 1 : -1;
            for (int x = x1 + xStep, y = y1 + yStep;
                 x != x2 && y != y2;
                 x += xStep, y += yStep) {
                squares.add(boardSquares[y][x]);
            }
        }
        return squares;
    }

    public boolean testMove(Piece piece, Square target) {
        Square originalSquare = piece.getPosition();
        Piece capturedPiece = target.getOccupyingPiece();

        // Simulate move
        piece.move(target);
        update();

        boolean isValid;
        if (piece.getColor() == Color_Piece.WHITE) {
            isValid = !isKingInCheck(whiteKing, blackAttacks);
        } else {
            isValid = !isKingInCheck(blackKing, whiteAttacks);
        }

        // Revert move
        piece.move(originalSquare);
        if (capturedPiece != null) {
            target.put(capturedPiece);
        }
        update();

        if (isValid) {
            movableSquares.add(target);
        }
        return isValid;
    }

    // Simplified public interface methods
    public boolean whiteInCheck() {
        return isKingInCheck(whiteKing, blackAttacks);
    }

    public boolean blackInCheck() {
        return isKingInCheck(blackKing, whiteAttacks);
    }

    public boolean whiteCheckMated() {
        return isCheckmated(whiteKing, blackAttacks, whiteAttacks);
    }

    public boolean blackCheckMated() {
        return isCheckmated(blackKing, whiteAttacks, blackAttacks);
    }
    public List<Square> getAllowableSquares(boolean isWhiteTurn) {
        movableSquares.clear();
        if (isWhiteTurn) {
            if (whiteInCheck()) {
                whiteCheckMated(); // This populates movableSquares with valid evasion moves
            } else {
                addAllBoardSquares();
            }
        } else {
            if (blackInCheck()) {
                blackCheckMated(); // This populates movableSquares with valid evasion moves
            } else {
                addAllBoardSquares();
            }
        }

        return new ArrayList<>(movableSquares);
    }

    private void addAllBoardSquares() {
        for (Square[] row : boardSquares) {
            movableSquares.addAll(Arrays.asList(row));
        }
    }
}
