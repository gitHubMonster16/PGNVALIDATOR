package service.movement.Interfaces_MoveAndPlay;

import models.Board;
import models.Square;

import java.util.List;

public interface MovementLogic {
     List<Square> getLegalMoves(Board board);
}
