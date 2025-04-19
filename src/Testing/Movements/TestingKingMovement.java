package Testing.Movements;

import models.Board;
import models.King;
import models.Rook;
import models.Square;
import models.enums.Color_Piece;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static models.enums.Color_Piece.*;
import static org.junit.jupiter.api.Assertions.*;

public class TestingKingMovement {
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
    void kingHasEightPossibleMovesWhenCentered() {
        Square e4 = board.getSquare("e4");
        King king = new King(WHITE, e4, "/resources/wking.png");
        e4.setOccupyingPiece(king);

        List<Square> moves = king.getLegalMoves(board);

        String[] expectedPositions = {
                "d3", "d4", "d5",
                "e3",       "e5",
                "f3", "f4", "f5"
        };

        assertEquals(8, moves.size(), "Centered king should have 8 moves");
        for (String pos : expectedPositions) {
            assertTrue(moves.contains(board.getSquare(pos)), "Should be able to move to " + pos);
        }
    }

    @Test
    void kingHasThreeMovesInCorner() {
        Square h8 = board.getSquare("h8");
        King king = new King(BLACK, h8, "/resources/bking.png");
        h8.setOccupyingPiece(king);

        List<Square> moves = king.getLegalMoves(board);

        assertAll(
                () -> assertEquals(3, moves.size(), "Corner king should have 3 moves"),
                () -> assertTrue(moves.contains(board.getSquare("g7"))),
                () -> assertTrue(moves.contains(board.getSquare("g8"))),
                () -> assertTrue(moves.contains(board.getSquare("h7")))
        );
    }

    @Test
    void kingCannotMoveToFriendlyOccupiedSquares() {
        Square c6 = board.getSquare("c6");
        King king = new King(WHITE, c6, "/resources/wking.png");
        c6.setOccupyingPiece(king);

        // Surround king with friendly pieces
        String[] friendlySquares = {"b5", "b6", "b7", "c5", "c7", "d5", "d6", "d7"};
        for (String pos : friendlySquares) {
            board.getSquare(pos).setOccupyingPiece(new King(WHITE, board.getSquare(pos), "/resources/wking.png"));
        }

        List<Square> moves = king.getLegalMoves(board);
        assertTrue(moves.isEmpty(), "King should have no moves when surrounded by friends");
    }

    @Test
    void kingCanCaptureOpponentPieces() {
        Square f3 = board.getSquare("f3");
        King king = new King(BLACK, f3, "/resources/bking.png");
        f3.setOccupyingPiece(king);

        // Place opponent pieces around king
        String[] opponentPositions = {"e2", "f2", "g2", "e3", "g3", "e4", "f4", "g4"};
        for (String pos : opponentPositions) {
            board.getSquare(pos).setOccupyingPiece(new King(WHITE, board.getSquare(pos), "/resources/wking.png"));
        }

        List<Square> moves = king.getLegalMoves(board);
        assertEquals(8, moves.size(), "King should be able to capture all adjacent opponents");
    }

    @Test
    void kingOnEdgeHasFivePossibleMoves() {
        Square a4 = board.getSquare("a4");
        King king = new King(WHITE, a4, "/resources/wking.png");
        a4.setOccupyingPiece(king);

        List<Square> moves = king.getLegalMoves(board);

        assertAll(
                () -> assertEquals(5, moves.size()),
                () -> assertTrue(moves.contains(board.getSquare("a3"))),
                () -> assertTrue(moves.contains(board.getSquare("a5"))),
                () -> assertTrue(moves.contains(board.getSquare("b3"))),
                () -> assertTrue(moves.contains(board.getSquare("b4"))),
                () -> assertTrue(moves.contains(board.getSquare("b5")))
        );
    }

    @Test
    void blackKingCanCastleBothSidesWhenUnmoved() {
        Square e8 = board.getSquare("e8");
        King king = new King(BLACK, e8, "/resources/bking.png");
        Rook kingsideRook = new Rook(BLACK, board.getSquare("h8"), "/resources/brook.png");
        Rook queensideRook = new Rook(BLACK, board.getSquare("a8"), "/resources/brook.png");

        e8.setOccupyingPiece(king);
        board.getSquare("h8").setOccupyingPiece(kingsideRook);
        board.getSquare("a8").setOccupyingPiece(queensideRook);

        List<Square> moves = king.getLegalMoves(board);

        assertAll(
                () -> assertTrue(moves.contains(board.getSquare("g8")), "Should allow kingside castling"),
                () -> assertTrue(moves.contains(board.getSquare("c8")), "Should allow queenside castling")
        );
    }

    @Test
    void castlingBlockedByIntermediatePiece() {
        Square e1 = board.getSquare("e1");
        King king = new King(WHITE, e1, "/resources/wking.png");
        Rook rook = new Rook(WHITE, board.getSquare("a1"), "/resources/wrook.png");
        // Blocking piece
        King blocker = new King(WHITE, board.getSquare("b1"), "/resources/wking.png");

        e1.setOccupyingPiece(king);
        board.getSquare("a1").setOccupyingPiece(rook);
        board.getSquare("b1").setOccupyingPiece(blocker);

        List<Square> moves = king.getLegalMoves(board);
        assertFalse(moves.contains(board.getSquare("c1")), "Queenside castling should be blocked");
    }

    @Test
    void cannotCastleIfKingHasMoved() {
        Square e1 = board.getSquare("e1");
        King king = new King(WHITE, e1, "/resources/wking.png");
        king.setHasMoved(true);
        Rook rook = new Rook(WHITE, board.getSquare("h1"), "/resources/wrook.png");

        e1.setOccupyingPiece(king);
        board.getSquare("h1").setOccupyingPiece(rook);

        List<Square> moves = king.getLegalMoves(board);
        assertFalse(moves.contains(board.getSquare("g1")), "Should not allow castling after king has moved");
    }

}