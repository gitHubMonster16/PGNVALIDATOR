package models;

import models.Board;
import models.enums.Color_Piece;
import service.movement.StandardKnightMovement;

import java.util.LinkedList;
import java.util.List;

public class Knight extends Piece {

    public Knight(Color_Piece color, Square initSq, String img_file) {
        super(color, initSq, img_file);
    }

    @Override
    public List<Square> getLegalMoves(Board b) {
       return new StandardKnightMovement(this).getLegalMoves(b);
    }

}
