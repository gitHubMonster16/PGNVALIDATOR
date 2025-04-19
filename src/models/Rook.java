package models;

import models.Board;
import models.enums.Color_Piece;
import service.movement.Interfaces_MoveAndPlay.MovementLogic;
import service.movement.StandardRookMovement;

import java.util.LinkedList;
import java.util.List;

public class Rook extends Piece {

    public Rook(Color_Piece color, Square initSq, String img_file) {
        super(color, initSq, img_file);
    }

    @Override
    public List<Square> getLegalMoves(Board b) {
        return  new StandardRookMovement(this).getLegalMoves(b);
    }

}
