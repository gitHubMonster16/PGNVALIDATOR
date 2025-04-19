package Testing.Movements;

import models.Bishop;
import models.Board;
import models.Square;
import models.enums.Color_Piece;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static models.enums.Color_Piece.*;
import static org.junit.jupiter.api.Assertions.*;

public class TestingBishopMovement {
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
    void bishopCanMoveFourDiagonalsFromCenter() {
        Square d4 = board.getSquare("d4");
        Bishop bishop = new Bishop(WHITE, d4, "/resources/wbishop.png");
        d4.setOccupyingPiece(bishop);

        List<Square> moves = bishop.getLegalMoves(board);

        assertAll(
                () -> assertTrue(moves.contains(board.getSquare("a1"))), // NW diagonal
                        () -> assertTrue(moves.contains(board.getSquare("g7"))), // NE diagonal
                                () -> assertTrue(moves.contains(board.getSquare("a7"))), // SW diagonal
                                        () -> assertTrue(moves.contains(board.getSquare("g1")))  // SE diagonal
                                        );
    }

    @Test
    void bishopBlockedByFriendlyPiece() {
        Square f5 = board.getSquare("f5");
        Bishop bishop = new Bishop(BLACK, f5, "/resources/bbishop.png");
        f5.setOccupyingPiece(bishop);

        // Block one diagonal with friendly piece
        board.getSquare("g6").setOccupyingPiece(new Bishop(BLACK, board.getSquare("g6"), "/resources/bbishop.png"));

        List<Square> moves = bishop.getLegalMoves(board);

        assertAll(
                () -> assertFalse(moves.contains(board.getSquare("h7")), "Blocked by friendly piece"),
                () -> assertTrue(moves.contains(board.getSquare("e4")), "Open diagonal should be available")
        );
    }

    @Test
    void bishopCanCaptureEnemyPiece() {
        Square c3 = board.getSquare("c3");
        Bishop bishop = new Bishop(WHITE, c3, "/resources/wbishop.png");
        c3.setOccupyingPiece(bishop);

        // Place enemy piece two squares away
        board.getSquare("e5").setOccupyingPiece(new Bishop(BLACK, board.getSquare("e5"), "/resources/bbishop.png"));

        List<Square> moves = bishop.getLegalMoves(board);

        assertAll(
                () -> assertTrue(moves.contains(board.getSquare("e5")), "Should be able to capture"),
                () -> assertFalse(moves.contains(board.getSquare("f6")), "Shouldn't move beyond enemy")
        );
    }

    @Test
    void bishopOnEdgeHasLimitedMoves() {
        Square a3 = board.getSquare("a3");
        Bishop bishop = new Bishop(WHITE, a3, "/resources/wbishop.png");
        a3.setOccupyingPiece(bishop);

        List<Square> moves = bishop.getLegalMoves(board);

        assertAll(
                () -> assertTrue(moves.contains(board.getSquare("b4"))), // Up-right
                        () -> assertTrue(moves.contains(board.getSquare("b2")))  // Down-right
                        );
    }

    @Test
    void bishopInCornerHasLimitedMoves() {
        Square h8 = board.getSquare("h8");
        Bishop bishop = new Bishop(WHITE, h8, "/resources/wbishop.png");
        h8.setOccupyingPiece(bishop);

        List<Square> moves = bishop.getLegalMoves(board);

        assertAll(
                () -> assertTrue(moves.contains(board.getSquare("a1"))), // Long diagonal
                        () -> assertEquals(7, moves.size(), "Corner bishop should have 7 moves")
                );
    }

    @Test
    void bishopCannotMoveWhenSurrounded() {
        Square e4 = board.getSquare("e4");
        Bishop bishop = new Bishop(WHITE, e4, "/resources/wbishop.png");
        e4.setOccupyingPiece(bishop);

        // Completely surround with friendly pieces
        board.getSquare("d5").setOccupyingPiece(new Bishop(WHITE, board.getSquare("d5"), "/resources/wbishop.png"));
        board.getSquare("f5").setOccupyingPiece(new Bishop(WHITE, board.getSquare("f5"), "/resources/wbishop.png"));
        board.getSquare("d3").setOccupyingPiece(new Bishop(WHITE, board.getSquare("d3"), "/resources/wbishop.png"));
        board.getSquare("f3").setOccupyingPiece(new Bishop(WHITE, board.getSquare("f3"), "/resources/wbishop.png"));

        List<Square> moves = bishop.getLegalMoves(board);

        assertTrue(moves.isEmpty(), "Surrounded bishop should have no moves");
    }

    @Test
    void bishopCannotMoveVerticallyOrHorizontally() {
        Square e4 = board.getSquare("e4");
        Bishop bishop = new Bishop(WHITE, e4, "/resources/wbishop.png");
        e4.setOccupyingPiece(bishop);

        List<Square> moves = bishop.getLegalMoves(board);

        assertAll(
                () -> assertFalse(moves.contains(board.getSquare("e5"))), // Vertical
                () -> assertFalse(moves.contains(board.getSquare("f4")))  // Horizontal
        );
    }
}