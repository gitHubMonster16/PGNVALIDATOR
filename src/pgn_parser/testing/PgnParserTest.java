package pgn_parser.testing;

import org.junit.jupiter.api.Test;
import pgn_parser.PgnParser;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PgnParserTest {

    @Test
    void testIsMoveSyntaxRight() {
        assertTrue(PgnParser.isMoveSyntaxRight("e4"));
        assertTrue(PgnParser.isMoveSyntaxRight("Nf3"));
        assertTrue(PgnParser.isMoveSyntaxRight("O-O"));
        assertTrue(PgnParser.isMoveSyntaxRight("O-O-O"));

        assertFalse(PgnParser.isMoveSyntaxRight("e9"));
        assertFalse(PgnParser.isMoveSyntaxRight("Qh4"));  // Q not allowed
        assertFalse(PgnParser.isMoveSyntaxRight("abcd"));
        assertFalse(PgnParser.isMoveSyntaxRight("O-O-O-O"));
    }

    @Test
    void testEventLine() {
        assertTrue(PgnParser.eventLine("[Event \"World Championship\"]"));
        assertFalse(PgnParser.eventLine("Event \"World Championship\"]"));
        assertFalse(PgnParser.eventLine("[Event \"\"]"));
        assertFalse(PgnParser.eventLine("[Event \"Invalid<Characters>\"]"));
    }

    @Test
    void testSiteLine() {
        assertTrue(PgnParser.siteLine("[Site \"London\"]"));
        assertTrue(PgnParser.siteLine("[Site \"https://lichess.org\"]"));
        assertFalse(PgnParser.siteLine("[Site \"\"]"));
        assertFalse(PgnParser.siteLine("[Site \"bad<site>\"]"));
    }

    @Test
    void testDateLine() {
        assertTrue(PgnParser.dateLine("[Date \"2024.12.31\"]"));
        assertTrue(PgnParser.dateLine("[Date \"2024.02.29\"]")); // Leap year

        assertFalse(PgnParser.dateLine("[Date \"2024.02.30\"]"));
        assertFalse(PgnParser.dateLine("[Date \"2024.13.01\"]"));
        assertFalse(PgnParser.dateLine("[Date \"2024.12.32\"]"));
        assertFalse(PgnParser.dateLine("[Date \"2024-12-01\"]")); // wrong format
    }

    @Test
    void testWhiteLine() {
        assertTrue(PgnParser.whiteLine("[White \"Magnus Carlsen\"]"));
        assertFalse(PgnParser.whiteLine("[White \"\"]"));
        assertFalse(PgnParser.whiteLine("[White \"<hacker>\"]"));
        assertFalse(PgnParser.whiteLine("White \"Someone\"]"));
    }

    @Test
    void testBlackLine() {
        assertTrue(PgnParser.blackLine("[Black \"Hikaru Nakamura\"]"));
        assertFalse(PgnParser.blackLine("[Black \"\"]"));
        assertFalse(PgnParser.blackLine("[Black \"<Admin>\"]"));
        assertFalse(PgnParser.blackLine("Black \"Someone\"]"));
    }

    @Test
    void testResultLine() {
        assertTrue(PgnParser.resultLine("[Result \"1-0\"]"));
        assertTrue(PgnParser.resultLine("[Result \"0-1\"]"));
        assertTrue(PgnParser.resultLine("[Result \"1/2-1/2\"]"));
        assertFalse(PgnParser.resultLine("[Result \"1.0\"]"));
        assertFalse(PgnParser.resultLine("[Result \"\"]"));
        assertFalse(PgnParser.resultLine("Result \"1-0\"]"));
    }
    @Test
    void testParseChessMoves() {
        String moves = "1. e4 e5 2. Nf3 Nc6 3. Bb5 a6";
        List<List<String>> parsed = PgnParser.parseChessMoves(moves);
        assertEquals(3, parsed.size());
        assertEquals(List.of("e4", "e5"), parsed.get(0));
        assertEquals(List.of("Nf3", "Nc6"), parsed.get(1));
        assertEquals(List.of("Bb5", "a6"), parsed.get(2));
    }
    @Test
    void testIsMoveSyntaxRight_InvalidCastleWithExtraChars() {
        assertFalse(PgnParser.isMoveSyntaxRight("O-O-O-O")); // Too many O's
    }

    @Test
    void testParseChessMoves_HandlesIncompleteLastMove() {
        String moves = "1. e4 e5 2. Nf3 Nc6 3. Bb5";
        List<List<String>> parsed = PgnParser.parseChessMoves(moves);
        assertEquals(3, parsed.size());
    }

    @Test
    void testSiteLine_ValidHttpSite() {
        assertTrue(PgnParser.siteLine("[Site \"http://chess.com\"]"));
    }

    @Test
    void testDateLine_InvalidNonNumeric() {
        assertFalse(PgnParser.dateLine("[Date \"20XX.12.01\"]")); // Non-numeric year
    }

    @Test
    void testResultLine_InvalidDrawSpelling() {
        assertFalse(PgnParser.resultLine("[Result \"draw\"]")); // Only PGN-style "1/2-1/2" is valid
    }

}

