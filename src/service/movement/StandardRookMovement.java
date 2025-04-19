package service.movement;

import models.Board;
import models.Piece;
import models.Square;
import service.movement.Interfaces_MoveAndPlay.MovementLogic;
import service.movement.handler.MovementHandle;
import java.util.List;

public class StandardRookMovement implements MovementLogic {
    private final Piece rook;
    public StandardRookMovement(Piece piece){
        this.rook=piece;
    }
    public List<Square> getLegalMoves(Board board){
        return MovementHandle.getHorizontalVerticalMoves(board,rook);
    }
}
