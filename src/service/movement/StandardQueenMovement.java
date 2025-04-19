package service.movement;

import models.Board;
import models.Piece;
import models.Square;
import service.movement.Interfaces_MoveAndPlay.MovementLogic;
import service.movement.handler.MovementHandle;

import java.util.List;

public class StandardQueenMovement implements MovementLogic {
    public final Piece queen;
    public StandardQueenMovement(Piece piece){
        this.queen=piece;
    }

    @Override
    public List<Square> getLegalMoves(Board board) {
        return MovementHandle.getCombinedMoves(board,queen);
    }
}
