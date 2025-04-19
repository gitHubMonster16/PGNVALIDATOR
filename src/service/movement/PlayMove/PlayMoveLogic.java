package service.movement.PlayMove;

import models.Board;
import models.Square;

public interface PlayMoveLogic {
    boolean playMoveTo(Board board, Square square);
}
