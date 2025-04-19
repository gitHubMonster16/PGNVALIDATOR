package models;
import models.enums.Color_Piece;
import service.movement.Interfaces_MoveAndPlay.MovementLogic;
import service.movement.StandardQueenMovement;
import java.util.List;

public class Queen extends Piece {

    public Queen(Color_Piece color, Square initSq, String img_file) {
        super(color, initSq, img_file);
    }

    @Override
    public List<Square> getLegalMoves(Board b) {
        return  new StandardQueenMovement(this).getLegalMoves(b);
    }
    
}
