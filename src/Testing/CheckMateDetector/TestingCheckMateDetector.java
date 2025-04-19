package Testing.CheckMateDetector;
import models.*;
import models.enums.Color_Piece;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.checkmateDetector.CheckmateDetector;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestingCheckMateDetector {
    private Board board;
    private CheckmateDetector detector;
    private King whiteKing;
    private King blackKing;

    @BeforeEach
    void setUp() {
        board = new Board(null);
        board.initializePieces();
        whiteKing = (King) board.getSquare(4, 0).getOccupyingPiece();
        blackKing = (King) board.getSquare(4, 7).getOccupyingPiece();
        detector = new CheckmateDetector(board,
                board.getWpieces(),
                board.getBpieces(),
                whiteKing, blackKing);
    }

    @Test
    void testInitialPositionNotInCheck() {
        assertFalse(detector.whiteInCheck());
        assertFalse(detector.blackInCheck());
    }






    @Test
    void testTestMoveValid() {
        Square initialSquare = board.getSquare(1, 0).getOccupyingPiece().getPosition();
        Square targetSquare = board.getSquare(2, 2);

        assertTrue(detector.testMove(board.getSquare(1, 0).getOccupyingPiece(), targetSquare));
        assertEquals(initialSquare, board.getSquare(1, 0).getOccupyingPiece().getPosition());
    }


    @Test
    void testGetAllowableSquaresInCheck() {
        // Put white king in check with black rook
        Piece blackRook = board.getSquare(0, 7).getOccupyingPiece();
        blackRook.move(board.getSquare(4, 7));
        detector.update();

        List<Square> allowableSquares = detector.getAllowableSquares(true);
        assertFalse(allowableSquares.isEmpty());
        assertTrue(allowableSquares.contains(board.getSquare(3, 0))); // King escape
        assertTrue(allowableSquares.contains(board.getSquare(5, 0))); // King escape
    }

    @Test
    void testGetAllowableSquaresNotInCheck() {
        List<Square> allowableSquares = detector.getAllowableSquares(true);
        assertEquals(64, allowableSquares.size()); // All squares should be allowable
    }

    @Test
    void testGetSquaresBetweenHorizontal() {
        Square start = board.getSquare(0, 0);
        Square end = board.getSquare(3, 0);
        List<Square> between = detector.getSquaresBetween(start, end);

        assertEquals(2, between.size());
        assertTrue(between.contains(board.getSquare(1, 0)));
        assertTrue(between.contains(board.getSquare(2, 0)));
    }

    @Test
    void testGetSquaresBetweenVertical() {
        Square start = board.getSquare(0, 0);
        Square end = board.getSquare(0, 3);
        List<Square> between = detector.getSquaresBetween(start, end);

        assertEquals(2, between.size());
        assertTrue(between.contains(board.getSquare(0, 1)));
        assertTrue(between.contains(board.getSquare(0, 2)));
    }

    @Test
    void testGetSquaresBetweenDiagonal() {
        Square start = board.getSquare(0, 0);
        Square end = board.getSquare(3, 3);
        List<Square> between = detector.getSquaresBetween(start, end);

        assertEquals(2, between.size());
        assertTrue(between.contains(board.getSquare(1, 1)));
        assertTrue(between.contains(board.getSquare(2, 2)));
    }


    @Test
    void testAllSquaresAllowedWhenNotInCheck() {
        // Initial position - no checks
        List<Square> squares = detector.getAllowableSquares(true);
        assertEquals(64, squares.size()); // All 64 squares should be allowed

        // After first pawn move
        board.getSquare(0, 1).getOccupyingPiece().move(board.getSquare(0, 2));
        detector.update();
        squares = detector.getAllowableSquares(false); // Black's turn
        assertEquals(64, squares.size());
    }

}