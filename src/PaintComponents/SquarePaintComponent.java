package PaintComponents;
import models.Square;
import models.enums.Color_Piece;

import javax.swing.*;
import java.awt.*;

public class SquarePaintComponent extends JComponent {
    private Square square;
    public Square getSquare() {
        return square;
    }
    public SquarePaintComponent(Square square){
      this.square=square;
    }
//    public void paintComponent(Graphics g) {
////        super.paintComponent(g);
//        if (this.square.getColor() == Color_Piece.WHITE) {
//            g.setColor(new Color(221,192,127));
//        } else {
//            g.setColor(new Color(101,67,33));
//        }
//        g.fillRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
//        if (square.getOccupyingPiece() != null && this.square.getDispPiece()) {
//            PieceDraw pd = new PieceDraw(square.getOccupyingPiece());
//            pd.draw(g, getWidth(), getHeight());
//        }
//  }
public void paintComponent(Graphics g) {
    super.paintComponent(g);

    if (square.getColor() == Color_Piece.WHITE) {
        g.setColor(new Color(221,192,127));
    } else {
        g.setColor(new Color(101,67,33));
    }
    g.fillRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());

    if(square.getOccupyingPiece() != null && square.getDispPiece()) {
        new PieceDraw(square.getOccupyingPiece()).draw(g,this.getX(),this.getY());
    }
}
}
