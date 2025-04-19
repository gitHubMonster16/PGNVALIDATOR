package Testing.Movements;

import models.Board;
import models.King;
import models.Pawn;
import models.Square;
import models.enums.Color_Piece;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static models.enums.Color_Piece.*;
import static org.junit.jupiter.api.Assertions.*;

public class TestingPawnMovement {
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
    void whitePawnCanAdvanceTwoSquaresFromStartingRank() {
        Square b2 = board.getSquare("b2");
        Pawn pawn = new Pawn(WHITE, b2, "/resources/wpawn.png");
        b2.setOccupyingPiece(pawn);

        List<Square> moves = pawn.getLegalMoves(board);

        assertAll(
                () -> assertTrue(moves.contains(board.getSquare("b3"))),
                () -> assertTrue(moves.contains(board.getSquare("b4"))),
                () -> assertEquals(2, moves.size())
        );
    }



    @Test
    void pawnCannotJumpOverPieces() {
        Square c2 = board.getSquare("c2");
        Pawn pawn = new Pawn(WHITE, c2, "/resources/wpawn.png");
        c2.setOccupyingPiece(pawn);

        // Block pawn's path
        board.getSquare("c3").setOccupyingPiece(new Pawn(BLACK, board.getSquare("c3"), "/resources/bpawn.png"));

        List<Square> moves = pawn.getLegalMoves(board);
        assertTrue(moves.isEmpty(), "Pawn should have no moves when blocked");
    }

    @Test
    void pawnPromotionRankNotIncludedInMoves() {
        Square h7 = board.getSquare("h7");
        Pawn pawn = new Pawn(WHITE, h7, "/resources/wpawn.png");
        h7.setOccupyingPiece(pawn);

        List<Square> moves = pawn.getLegalMoves(board);
        assertEquals(1, moves.size(), "Pawn on 7th rank should only have one move forward");
        assertTrue(moves.contains(board.getSquare("h8")));
    }

    @Test
    void enPassantNotAllowedByDefault() {
        Square d5 = board.getSquare("d5");
        Pawn pawn = new Pawn(WHITE, d5, "/resources/wpawn.png");
        d5.setOccupyingPiece(pawn);

        // Place opponent pawn that just moved two squares
        board.getSquare("e5").setOccupyingPiece(new Pawn(BLACK, board.getSquare("e5"), "/resources/bpawn.png"));

        List<Square> moves = pawn.getLegalMoves(board);
        assertFalse(moves.contains(board.getSquare("e6")), "En passant should not be available by default");
    }

    @Test
    void pawnCannotCaptureStraight() {
        Square f4 = board.getSquare("f4");
        Pawn pawn = new Pawn(WHITE, f4, "/resources/wpawn.png");
        f4.setOccupyingPiece(pawn);

        // Place enemy pawn directly ahead
        board.getSquare("f5").setOccupyingPiece(new Pawn(BLACK, board.getSquare("f5"), "/resources/bpawn.png"));

        List<Square> moves = pawn.getLegalMoves(board);
        assertFalse(moves.contains(board.getSquare("f5")), "Pawn should not capture straight ahead");
    }

    @Test
    void blackPawnCannotMoveBackwards() {
        Square a6 = board.getSquare("a6");
        Pawn pawn = new Pawn(BLACK, a6, "/resources/bpawn.png");
        a6.setOccupyingPiece(pawn);

        List<Square> moves = pawn.getLegalMoves(board);
        assertFalse(moves.contains(board.getSquare("a7")), "Black pawn should not move backwards");
    }



    @Test
    void pawnCannotMoveDiagonallyWithoutCapture() {
        Square g3 = board.getSquare("g3");
        Pawn pawn = new Pawn(WHITE, g3, "/resources/wpawn.png");
        g3.setOccupyingPiece(pawn);

        List<Square> moves = pawn.getLegalMoves(board);

        assertAll(
                () -> assertFalse(moves.contains(board.getSquare("h4"))),
                () -> assertFalse(moves.contains(board.getSquare("f4"))),
                () -> assertTrue(moves.contains(board.getSquare("g4")))
        );
    }

    @Test
    void pawnBlockedBySameColor() {
        Square e3 = board.getSquare("e3");
        Pawn pawn = new Pawn(BLACK, e3, "/resources/bpawn.png");
        e3.setOccupyingPiece(pawn);

        // Block with same color
        board.getSquare("e2").setOccupyingPiece(new Pawn(BLACK, board.getSquare("e2"), "/resources/bpawn.png"));

        List<Square> moves = pawn.getLegalMoves(board);
        assertTrue(moves.isEmpty(), "Pawn should have no moves when blocked by same color");
    }
    @Test
    void whitePawnCannotMoveTwoSquaresAfterFirstMove() {
        Square c2 = board.getSquare("c2");
        Pawn pawn = new Pawn(WHITE, c2, "/resources/wpawn.png");
        c2.setOccupyingPiece(pawn);

        // Simulate first move
        pawn.move(board.getSquare("c3"));

        List<Square> moves = pawn.getLegalMoves(board);
        assertAll(
                () -> assertTrue(moves.contains(board.getSquare("c4"))),
                () -> assertFalse(moves.contains(board.getSquare("c5"))),
                () -> assertEquals(1, moves.size())
        );
    }
}