package Testing.Movements;

import models.*;
import models.enums.Color_Piece;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static models.enums.Color_Piece.*;
import static org.junit.jupiter.api.Assertions.*;

public class TestingKnightMovement {
    private Board board;

    @BeforeEach
    void initializeBoard() {
        board = new Board(null);
        clearBoard();
    }

    private void clearBoard() {
        for (Square[] row : board.getSquareArray()) {
            for (Square square : row) {
                square.setOccupyingPiece(null);
            }
        }
        board.getWpieces().clear();
        board.getBpieces().clear();
    }

    @Test
    void knightHasFullMobilityFromCenter() {
        Square e5 = board.getSquare("e5");
        Knight knight = new Knight(WHITE, e5, "/resources/wknight.png");
        e5.setOccupyingPiece(knight);

        List<Square> moves = knight.getLegalMoves(board);

        String[] expectedPositions = {
                "d7", "f7",  // Forward L-shapes
                "g6", "g4",  // Side L-shapes
                "f3", "d3",  // Backward L-shapes
                "c6", "c4"   // Wide L-shapes
        };

        assertEquals(8, moves.size(), "Centered knight should have 8 moves");
        for (String pos : expectedPositions) {
            assertTrue(moves.contains(board.getSquare(pos)), "Should reach " + pos);
        }
    }
    @Test
    void knightCannotJumpToFriendlySquares() {
        Square f6 = board.getSquare("f6");
        Knight knight = new Knight(WHITE, f6, "/resources/wknight.png");
        f6.setOccupyingPiece(knight);

        // Block all possible moves with friendly pieces
        String[] blockedSquares = {"e8", "g8", "h7", "h5", "g4", "e4", "d5", "d7"};
        for (String pos : blockedSquares) {
            board.getSquare(pos).setOccupyingPiece(new Knight(WHITE, board.getSquare(pos), "/resources/wknight.png"));
        }

        List<Square> moves = knight.getLegalMoves(board);
        assertTrue(moves.isEmpty(), "Knight should have no legal moves when surrounded by friends");
    }

    @Test
    void knightCanCaptureOpponentPieces() {
        Square c3 = board.getSquare("c3");
        Knight knight = new Knight(BLACK, c3, "/resources/bknight.png");
        c3.setOccupyingPiece(knight);

        // Place opponent pieces at all L-shaped positions
        String[] targetSquares = {"a4", "b5", "d5", "e4", "e2", "d1", "b1", "a2"};
        for (String pos : targetSquares) {
            board.getSquare(pos).setOccupyingPiece(new Knight(WHITE, board.getSquare(pos), "/resources/wknight.png"));
        }

        List<Square> moves = knight.getLegalMoves(board);
        assertEquals(8, moves.size(), "Knight should be able to capture all adjacent opponents");
    }

    @Test
    void knightHasUniqueMovementPattern() {
        Square d4 = board.getSquare("d4");
        Knight knight = new Knight(WHITE, d4, "/resources/wknight.png");
        d4.setOccupyingPiece(knight);

        // Place pieces that would block other pieces but not knight
        board.getSquare("d5").setOccupyingPiece(new Knight(WHITE, board.getSquare("d5"), "/resources/wknight.png"));
        board.getSquare("e4").setOccupyingPiece(new Knight(WHITE, board.getSquare("e4"), "/resources/wknight.png"));

        List<Square> moves = knight.getLegalMoves(board);
        assertEquals(8, moves.size(), "Knight should jump over blocking pieces");
    }

    @Test
    void knightInCornerHasMinimalMobility() {
        Square h8 = board.getSquare("h8");
        Knight knight = new Knight(WHITE, h8, "/resources/wknight.png");
        h8.setOccupyingPiece(knight);

        List<Square> moves = knight.getLegalMoves(board);

        assertAll(
                () -> assertEquals(2, moves.size()),
                () -> assertTrue(moves.contains(board.getSquare("g6"))),
                () -> assertTrue(moves.contains(board.getSquare("f7")))
        );
    }

    @Test
    void knightCanEscapeFromEdge() {
        Square a7 = board.getSquare("a7");
        Knight knight = new Knight(BLACK, a7, "/resources/bknight.png");
        a7.setOccupyingPiece(knight);

        List<Square> moves = knight.getLegalMoves(board);

        String[] expectedEscapes = {"b5", "c6", "c8"};
        for (String pos : expectedEscapes) {
            assertTrue(moves.contains(board.getSquare(pos)), "Knight should escape to " + pos);
        }
    }

    @Test
    void knightCannotBeBlockedLinearly() {
        Square f3 = board.getSquare("f3");
        Knight knight = new Knight(BLACK, f3, "/resources/bknight.png");
        f3.setOccupyingPiece(knight);

        // Place pieces that would block other pieces
        board.getSquare("f4").setOccupyingPiece(new Knight(WHITE, board.getSquare("f4"), "/resources/wknight.png"));
        board.getSquare("g3").setOccupyingPiece(new Knight(WHITE, board.getSquare("g3"), "/resources/wknight.png"));

        List<Square> moves = knight.getLegalMoves(board);
        assertEquals(8, moves.size(), "Knight should ignore linear blocking pieces");
    }
    @Test
    void knightCanReachAllBoardColors() {
        Square d4 = board.getSquare("d4");
        Knight knight = new Knight(WHITE, d4, "/resources/wknight.png");
        d4.setOccupyingPiece(knight);

        // Place markers on opposite color squares
        Square lightSquare = board.getSquare("e6"); // Light square
        Square darkSquare = board.getSquare("f5");  // Dark square
        lightSquare.setOccupyingPiece(new Knight(BLACK, lightSquare, "/resources/bknight.png"));
        darkSquare.setOccupyingPiece(new Knight(BLACK, darkSquare, "/resources/bknight.png"));

        List<Square> moves = knight.getLegalMoves(board);

        assertAll(
                () -> assertTrue(moves.contains(lightSquare), "Should reach light square"),
                        () -> assertTrue(moves.contains(darkSquare), "Should reach dark square"),
                        () -> assertEquals(8, moves.size(), "Should maintain full mobility")
                );
    }

    @Test
    void knightCannotBePinned() {
        Square e4 = board.getSquare("e4");
        Knight knight = new Knight(BLACK, e4, "/resources/bknight.png");
        e4.setOccupyingPiece(knight);

        // Place enemy rook that would pin other pieces
        board.getSquare("e8").setOccupyingPiece(new Rook(WHITE, board.getSquare("e8"), "/resources/wrook.png"));
        board.getSquare("e1").setOccupyingPiece(new Rook(WHITE, board.getSquare("e1"), "/resources/wrook.png"));

        List<Square> moves = knight.getLegalMoves(board);
        assertEquals(8, moves.size(), "Knight should have full mobility despite vertical attack line");
    }

    @Test
    void knightForkDetectionScenario() {
        Square f3 = board.getSquare("f3");
        Knight knight = new Knight(WHITE, f3, "/resources/wknight.png");
        f3.setOccupyingPiece(knight);

        // Place valuable enemy pieces in forkable positions
        board.getSquare("g5").setOccupyingPiece(new Rook(BLACK, board.getSquare("g5"), "/resources/brook.png"));
        board.getSquare("h4").setOccupyingPiece(new Queen(BLACK, board.getSquare("h4"), "/resources/bqueen.png"));
        board.getSquare("d4").setOccupyingPiece(new King(BLACK, board.getSquare("d4"), "/resources/bking.png"));

        List<Square> moves = knight.getLegalMoves(board);

        assertAll(
                () -> assertTrue(moves.contains(board.getSquare("g5")), "Should threaten rook"),
                        () -> assertTrue(moves.contains(board.getSquare("h4")), "Should threaten queen"),
                                () -> assertTrue(moves.contains(board.getSquare("d4")), "Should threaten king"),
                                        () -> assertEquals(8, moves.size(), "Should maintain all possible forks")
                                        );
    }
}