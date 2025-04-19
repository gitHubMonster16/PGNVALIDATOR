package PaintComponents;

import models.Board;
import models.Piece;
import models.Square;
import models.enums.Color_Piece;

import javax.swing.*;
import java.awt.*;

public class BoardPaintComponent extends JPanel {
    private Board board;
    public BoardPaintComponent(Board board){
        this.board=board;
    }
    public void paintComponent(Graphics g) {
        // super.paintComponent(g);

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Square sq = this.board.getSquareArray()[y][x];
                sq.paintComponent(g);
            }
        }
        Piece currPiece=this.board.getCurrPiece();
        boolean whiteTurn=this.board.getWhiteTurn();
        if (currPiece!= null) {
            if ((currPiece.getColor() == Color_Piece.WHITE && whiteTurn)
                    || (currPiece.getColor() == Color_Piece.BLACK && !whiteTurn)) {
                final Image i = currPiece.getImage();
                g.drawImage(i, this.board.getCurrX(), board.getCurrY(), null);
            }
        }
    }
}
