package Testing.Movements;

import models.Board;
import models.Rook;
import models.Square;
import models.enums.Color_Piece;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static models.enums.Color_Piece.*;
import static org.junit.jupiter.api.Assertions.*;

public class TestingRookMovement {
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
    void rookCanMoveHorizontallyAndVertically() {
        Square e4 = board.getSquare("e4");
        Rook rook = new Rook(WHITE, e4, "/resources/wrook.png");
        e4.setOccupyingPiece(rook);

        List<Square> moves = rook.getLegalMoves(board);

        assertAll(
                () -> assertTrue(moves.contains(board.getSquare("e1"))),
                () -> assertTrue(moves.contains(board.getSquare("e8"))),
                () -> assertTrue(moves.contains(board.getSquare("a4"))),
                () -> assertTrue(moves.contains(board.getSquare("h4")))
        );
    }

    @Test
    void rookCannotMoveDiagonally() {
        Square c3 = board.getSquare("c3");
        Rook rook = new Rook(BLACK, c3, "/resources/brook.png");
        c3.setOccupyingPiece(rook);

        List<Square> moves = rook.getLegalMoves(board);

        assertAll(
                () -> assertFalse(moves.contains(board.getSquare("d4"))),
                () -> assertFalse(moves.contains(board.getSquare("b2"))),
                () -> assertFalse(moves.contains(board.getSquare("b4"))),
                () -> assertFalse(moves.contains(board.getSquare("d2")))
        );
    }

    @Test
    void rookBlockedByFriendlyPiece() {
        Square f5 = board.getSquare("f5");
        Rook rook = new Rook(WHITE, f5, "/resources/wrook.png");
        f5.setOccupyingPiece(rook);

        // Block with friendly pieces
        board.getSquare("f7").setOccupyingPiece(new Rook(WHITE, board.getSquare("f7"), "/resources/wrook.png"));
        board.getSquare("b5").setOccupyingPiece(new Rook(WHITE, board.getSquare("b5"), "/resources/wrook.png"));

        List<Square> moves = rook.getLegalMoves(board);

        assertAll(
                () -> assertFalse(moves.contains(board.getSquare("f8"))),
                () -> assertFalse(moves.contains(board.getSquare("a5")))
        );
    }

    @Test
    void rookCanCaptureEnemyPiece() {
        Square g2 = board.getSquare("g2");
        Rook rook = new Rook(BLACK, g2, "/resources/brook.png");
        g2.setOccupyingPiece(rook);

        // Place enemy pieces
        board.getSquare("g5").setOccupyingPiece(new Rook(WHITE, board.getSquare("g5"), "/resources/wrook.png"));
        board.getSquare("c2").setOccupyingPiece(new Rook(WHITE, board.getSquare("c2"), "/resources/wrook.png"));

        List<Square> moves = rook.getLegalMoves(board);

        assertAll(
                () -> assertTrue(moves.contains(board.getSquare("g5"))),
                () -> assertTrue(moves.contains(board.getSquare("c2"))),
                () -> assertFalse(moves.contains(board.getSquare("g6"))),
                () -> assertFalse(moves.contains(board.getSquare("b2")))
        );
    }

    @Test
    void rookInCornerHasLimitedMoves() {
        Square a8 = board.getSquare("a8");
        Rook rook = new Rook(WHITE, a8, "/resources/wrook.png");
        a8.setOccupyingPiece(rook);

        List<Square> moves = rook.getLegalMoves(board);

        assertAll(
                () -> assertEquals(14, moves.size()),
                () -> assertTrue(moves.contains(board.getSquare("a1"))),
                () -> assertTrue(moves.contains(board.getSquare("h8")))
        );
    }

    @Test
    void rookCannotJumpOverPieces() {
        Square d4 = board.getSquare("d4");
        Rook rook = new Rook(BLACK, d4, "/resources/brook.png");
        d4.setOccupyingPiece(rook);

        // Block with pieces
        board.getSquare("d6").setOccupyingPiece(new Rook(WHITE, board.getSquare("d6"), "/resources/wrook.png"));
        board.getSquare("b4").setOccupyingPiece(new Rook(WHITE, board.getSquare("b4"), "/resources/wrook.png"));

        List<Square> moves = rook.getLegalMoves(board);

        assertAll(
                () -> assertTrue(moves.contains(board.getSquare("d6"))),
                () -> assertTrue(moves.contains(board.getSquare("b4"))),
                () -> assertFalse(moves.contains(board.getSquare("d7"))),
                () -> assertFalse(moves.contains(board.getSquare("a4")))
        );
    }

    @Test
    void rookCompletelyBlockedByFriendlyPieces() {
        Square e5 = board.getSquare("e5");
        Rook rook = new Rook(WHITE, e5, "/resources/wrook.png");
        e5.setOccupyingPiece(rook);

        board.getSquare("e4").setOccupyingPiece(new Rook(WHITE, board.getSquare("e4"), "/resources/wrook.png"));
        board.getSquare("e6").setOccupyingPiece(new Rook(WHITE, board.getSquare("e6"), "/resources/wrook.png"));
        board.getSquare("d5").setOccupyingPiece(new Rook(WHITE, board.getSquare("d5"), "/resources/wrook.png"));
        board.getSquare("f5").setOccupyingPiece(new Rook(WHITE, board.getSquare("f5"), "/resources/wrook.png"));

        List<Square> moves = rook.getLegalMoves(board);
        assertEquals(0, moves.size(), "Rook surrounded by friendly pieces should have no legal moves");
    }


    @Test
    void rookCapturesEnemyAndStops() {
        Square d5 = board.getSquare("d5");
        Rook rook = new Rook(WHITE, d5, "/resources/wrook.png");
        d5.setOccupyingPiece(rook);

        board.getSquare("d7").setOccupyingPiece(new Rook(BLACK, board.getSquare("d7"), "/resources/brook.png"));
        board.getSquare("d8").setOccupyingPiece(new Rook(BLACK, board.getSquare("d8"), "/resources/brook.png"));

        List<Square> moves = rook.getLegalMoves(board);

        assertTrue(moves.contains(board.getSquare("d7")));
        assertFalse(moves.contains(board.getSquare("d8")), "Rook should not move past captured piece");
    }


    @Test
    void rookCannotMoveToCurrentPosition() {
        Square f6 = board.getSquare("f6");
        Rook rook = new Rook(WHITE, f6, "/resources/wrook.png");
        f6.setOccupyingPiece(rook);

        List<Square> moves = rook.getLegalMoves(board);
        assertFalse(moves.contains(f6), "Rook should not be able to move to its current square");
    }

    @Test
    void rookHasMaximumMobilityWhenUnobstructed() {
        Square c4 = board.getSquare("c4");
        Rook rook = new Rook(BLACK, c4, "/resources/brook.png");
        c4.setOccupyingPiece(rook);

        List<Square> moves = rook.getLegalMoves(board);
        assertEquals(14, moves.size(), "Unobstructed rook should have 14 possible moves");
    }
}