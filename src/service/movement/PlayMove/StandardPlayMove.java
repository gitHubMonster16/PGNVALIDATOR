
package service.movement.PlayMove;
import models.Board;
import models.Piece;
import models.Square;
import service.movement.PlayMove.PlayMoveLogic;

public class StandardPlayMove implements PlayMoveLogic {
    private final Piece piece;

    public StandardPlayMove(Piece piece) {
        this.piece = piece;
    }

    @Override
    public boolean playMoveTo(Board board, Square destination) {
        Piece occup = destination.getOccupyingPiece();
        if (occup != null) {
            if (occup.getColor() == piece.getColor()) return false;
            else board.capturePiece(destination,piece);
        }
        piece.getPosition().removePiece();
        piece.setPosition(destination);
        piece.getPosition().put(piece);
        return true;
    }

}