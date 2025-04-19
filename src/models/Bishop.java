package models;

import models.enums.Color_Piece;
import service.movement.StandardBishopMovement;

import java.util.List;

public class Bishop extends Piece {

    public Bishop(Color_Piece color, Square initSq, String img_file) {
        super(color, initSq, img_file);
    }
    
    @Override
    public List<Square> getLegalMoves(Board b) {
        return new StandardBishopMovement(this).getLegalMoves(b);
    }
}
