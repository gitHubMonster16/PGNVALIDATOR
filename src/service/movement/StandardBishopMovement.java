package service.movement;

import models.Board;
import models.Piece;
import models.Square;
import service.movement.Interfaces_MoveAndPlay.MovementLogic;
import service.movement.handler.MovementHandle;

import java.util.List;

public class StandardBishopMovement implements MovementLogic {
    private final Piece bishop;
    public StandardBishopMovement(Piece piece){
        this.bishop=piece;
    }
    @Override
    public List<Square> getLegalMoves(Board board) {
        return MovementHandle.diagonalMoves(board,bishop);
    }
}
