package Testing.Movements;

import models.Board;
import models.Pawn;
import models.Queen;
import models.Square;
import models.enums.Color_Piece;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static models.enums.Color_Piece.*;
import static org.junit.jupiter.api.Assertions.*;

public class TestingQueenMovement {
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
    void queenInCornerHasLimitedMobility() {
        Square h1 = board.getSquare("h1");
        Queen queen = new Queen(BLACK, h1, "/resources/bqueen.png");
        h1.setOccupyingPiece(queen);

        List<Square> moves = queen.getLegalMoves(board);

        assertAll(
                () -> assertEquals(21, moves.size()),
                () -> assertTrue(moves.contains(board.getSquare("h8"))),
                () -> assertTrue(moves.contains(board.getSquare("a1"))),
                () -> assertTrue(moves.contains(board.getSquare("a8")))
        );
    }

    @Test
    void queenCanMoveThroughEmptySquares() {
        Square d4 = board.getSquare("d4");
        Queen queen = new Queen(WHITE, d4, "/resources/wqueen.png");
        d4.setOccupyingPiece(queen);

        List<Square> moves = queen.getLegalMoves(board);

        // Test various intermediate squares
        assertAll(
                () -> assertTrue(moves.contains(board.getSquare("d6"))),
                () -> assertTrue(moves.contains(board.getSquare("f4"))),
                () -> assertTrue(moves.contains(board.getSquare("b6"))),
                () -> assertTrue(moves.contains(board.getSquare("f6")))
        );
    }

    @Test
    void queenCannotLeaveTheBoard() {
        Square a8 = board.getSquare("a8");
        Queen queen = new Queen(WHITE, a8, "/resources/wqueen.png");
        a8.setOccupyingPiece(queen);

        List<Square> moves = queen.getLegalMoves(board);

        // Verify no moves go beyond board boundaries
        moves.forEach(square -> {
            String pos = square.getPosition().toAlgebraic();
            assertTrue(pos.matches("[a-h][1-8]"), "Move " + pos + " should stay on board");
        });
    }
    @Test
    void queenVersusQueenScenario() {
        Square d4 = board.getSquare("d4");
        Queen whiteQueen = new Queen(WHITE, d4, "/resources/wqueen.png");
        d4.setOccupyingPiece(whiteQueen);

        Square d7 = board.getSquare("d7");
        Queen blackQueen = new Queen(BLACK, d7, "/resources/bqueen.png");
        d7.setOccupyingPiece(blackQueen);

        List<Square> moves = whiteQueen.getLegalMoves(board);

        assertAll(
                () -> assertTrue(moves.contains(board.getSquare("d7"))),
                () -> assertFalse(moves.contains(board.getSquare("d8"))),
                () -> assertEquals(26, moves.size())
        );
    }


    @Test
    void queenCannotJumpOverPieces() {
        Square f6 = board.getSquare("f6");
        Queen queen = new Queen(BLACK, f6, "/resources/bqueen.png");
        f6.setOccupyingPiece(queen);

        // Block diagonally
        board.getSquare("d4").setOccupyingPiece(new Queen(BLACK, board.getSquare("d4"), "/resources/bqueen.png"));
        // Block vertically
        board.getSquare("f3").setOccupyingPiece(new Queen(WHITE, board.getSquare("f3"), "/resources/wqueen.png"));

        List<Square> moves = queen.getLegalMoves(board);

        assertAll(
                () -> assertFalse(moves.contains(board.getSquare("c3"))),
                () -> assertTrue(moves.contains(board.getSquare("f3"))),
                () -> assertFalse(moves.contains(board.getSquare("f2")))
        );
    }
    @Test
    void queenCanMoveOneSquareInAnyDirection() {
        Square e4 = board.getSquare("e4");
        Queen queen = new Queen(WHITE, e4, "/resources/wqueen.png");
        e4.setOccupyingPiece(queen);

        List<Square> moves = queen.getLegalMoves(board);

        String[] oneSquareMoves = {"e5", "f5", "f4", "f3", "e3", "d3", "d4", "d5"};
        for (String pos : oneSquareMoves) {
            assertTrue(moves.contains(board.getSquare(pos)), "Queen should move one square to " + pos);
        }
    }

    @Test
    void queenCannotMoveToItsOwnSquare() {
        Square c5 = board.getSquare("c5");
        Queen queen = new Queen(BLACK, c5, "/resources/bqueen.png");
        c5.setOccupyingPiece(queen);

        List<Square> moves = queen.getLegalMoves(board);
        assertFalse(moves.contains(c5), "Queen should not be able to move to its current square");
    }

    @Test
    void queenCanCaptureAdjacentEnemy() {
        Square f3 = board.getSquare("f3");
        Queen queen = new Queen(WHITE, f3, "/resources/wqueen.png");
        f3.setOccupyingPiece(queen);

        // Place enemy pieces adjacent
        board.getSquare("f4").setOccupyingPiece(new Pawn(BLACK, board.getSquare("f4"), "/resources/bpawn.png"));
        board.getSquare("g3").setOccupyingPiece(new Pawn(BLACK, board.getSquare("g3"), "/resources/bpawn.png"));

        List<Square> moves = queen.getLegalMoves(board);
        assertAll(
                () -> assertTrue(moves.contains(board.getSquare("f4"))),
                () -> assertTrue(moves.contains(board.getSquare("g3")))
        );
    }

    @Test
    void queenHasAtLeastOneMoveFromAnyPosition() {
        Square h8 = board.getSquare("h8");
        Queen queen = new Queen(WHITE, h8, "/resources/wqueen.png");
        h8.setOccupyingPiece(queen);

        // Block all but one direction
        board.getSquare("h7").setOccupyingPiece(new Pawn(WHITE, board.getSquare("h7"), "/resources/wpawn.png"));
        board.getSquare("g8").setOccupyingPiece(new Pawn(WHITE, board.getSquare("g8"), "/resources/wpawn.png"));
        board.getSquare("g7").setOccupyingPiece(new Pawn(BLACK, board.getSquare("g7"), "/resources/bpawn.png"));

        List<Square> moves = queen.getLegalMoves(board);
        assertEquals(1, moves.size(), "Queen should always have at least one move");
    }
    @Test
    void queenCanMoveAlongRankAndFile() {
        Square a1 = board.getSquare("a1");
        Queen queen = new Queen(WHITE, a1, "/resources/wqueen.png");
        a1.setOccupyingPiece(queen);

        List<Square> moves = queen.getLegalMoves(board);

        // Check first rank
        assertTrue(moves.contains(board.getSquare("b1")));
        // Check a-file
        assertTrue(moves.contains(board.getSquare("a2")));
        // Check diagonal
        assertTrue(moves.contains(board.getSquare("b2")));
    }
}