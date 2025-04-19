package models;

import models.enums.Color_Piece;
import service.movement.StandardKingMovement;

import java.util.LinkedList;
import java.util.List;

public class King extends Piece {

    @Override
    public boolean move(Square fin) {
        Board board = fin.getB();
        String sq = fin.getPosition().toAlgebraic();
        if (sq.equals("c1")) {
            try {
                board.getSquare("a1").getOccupyingPiece().move(board.getSquare("d1"));
                board.getSquare("d1").setDisplay(true);
            }
            catch( Exception e){

            }
            }
        if (sq.equals("c8")) {
            try {
                board.getSquare("a8").getOccupyingPiece().move(board.getSquare("d8"));
                board.getSquare("d8").setDisplay(true);
            }
             catch(Exception e){}
            }
        if (sq.equals("g1")) {
            try {
                board.getSquare("h1").getOccupyingPiece().move(board.getSquare("f1"));
                board.getSquare("f1").setDisplay(true);
            }
            catch (Exception e){}
            }

        if(sq.equals("g8")) {
            try{
                board.getSquare("h8").getOccupyingPiece().move(board.getSquare("f8"));
                board.getSquare("f8").setDisplay(true);
            }
            catch(Exception e){}
        }
        return super.move(fin);
    }

    public King(Color_Piece color, Square initSq, String img_file) {
        super(color, initSq, img_file);
    }
    @Override
    public List<Square> getLegalMoves(Board b) {
        return new StandardKingMovement(this).getLegalMoves(b);
    }
}
