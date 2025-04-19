package models;

import PaintComponents.PieceDraw;
import PaintComponents.SquarePaintComponent;
import models.enums.Color_Piece;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class Square extends JComponent {
    private  Position position;
    private Board b;
    private SquarePaintComponent paintComponent;
    private final Color_Piece  color;
    private Piece occupyingPiece;
    private boolean dispPiece;
    
    private int xNum;
    private int yNum;
    public Square(Board b, Color_Piece c, int xNum, int yNum) {
        this.b = b;
        this.color = c;
        this.dispPiece = true;
        this.xNum = xNum;
        this.yNum = yNum;
        this.position = new Position(xNum, yNum);
        this.paintComponent=new SquarePaintComponent(this);
        this.setBorder(BorderFactory.createEmptyBorder());
    }
    public boolean getDispPiece(){
        return this.dispPiece;
    }
    public Piece getOccupyingPiece() {
        return occupyingPiece;
    }
    
    public boolean isOccupied() {
        return (this.occupyingPiece != null);
    }
    
    public int getXNum() {
        return this.xNum;
    }
    
    public int getYNum() {
        return this.yNum;
    }

    public Position getPosition() {
        return position;
    }

    public Board getB() {
        return b;
    }

    public Color_Piece getColor() {
        return color;
    }

    public void setDisplay(boolean v) {
        this.dispPiece = v;
    }

    public void setOccupyingPiece(Piece occupyingPiece) {
        this.occupyingPiece = occupyingPiece;
    }

    public void put(Piece p) {
        this.occupyingPiece = p;
        p.setPosition(this);
    }
    
    public Piece removePiece() {
        Piece p = this.occupyingPiece;
        this.occupyingPiece = null;
        return p;
    }
    
    public void capture(Piece p) {
        Piece k = getOccupyingPiece();
        if (k.getColor() == Color_Piece.BLACK) b.Bpieces.remove(k);
        if (k.getColor() == Color_Piece.WHITE) b.Wpieces.remove(k);
        this.occupyingPiece = p;
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (this.getColor() == Color_Piece.WHITE) {
            g.setColor(new Color(221,192,127));
        } else {
            g.setColor(new Color(101,67,33));
        }
        g.fillRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());

        if(this.getOccupyingPiece() != null && this.getDispPiece()) {
            new PieceDraw(this.getOccupyingPiece()).draw(g,this.getX(),this.getY());
        }
    }
    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + xNum;
        result = prime * result + yNum;
        return result;
    }
    
}
