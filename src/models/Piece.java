package models;

import PaintComponents.PieceDraw;
import models.enums.Color_Piece;

import service.movement.PlayMove.PlayMoveLogic;
import service.movement.PlayMove.StandardPlayMove;
import service.movement.handler.MovementHandle;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;

public abstract class Piece {
    public boolean isHasMoved;
    private final Color_Piece color;
    private Square currentSquare;
    private BufferedImage img;
    private MovementHandle movementhandle;
    private PlayMoveLogic STANDARDPLAYMOVE;

    public Piece(Color_Piece color, Square initSq, String img_file) {
        this.color = color;
        this.currentSquare = initSq;
        this.isHasMoved=false;
        
        try {
            if (this.img == null) {
              this.img = ImageIO.read(getClass().getResource(img_file));
            }
          } catch (IOException e) {
            System.out.println("File not found: " + e.getMessage());
          }
    }

    public boolean move(Square fin) {
        Piece occup = fin.getOccupyingPiece();
        isHasMoved=true;
        if (occup != null) {
            if (occup.getColor() == this.color) return false;
            else fin.capture(this);
        }
        currentSquare.removePiece();
        this.currentSquare = fin;
        currentSquare.put(this);
        return true;
    }
    public Square getPosition() {
        return currentSquare;
    }

    public Square getCurrentSquare() {
        return currentSquare;
    }

    public void setPosition(Square sq) {
        this.currentSquare = sq;
    }

    public void setHasMoved(boolean hasMoved) {
        isHasMoved = hasMoved;
    }

    public Color_Piece getColor() {
        return color;
    }
    
    public Image getImage() {
        return img;
    }

    public abstract List<Square> getLegalMoves(Board b);

    public static void main(String[] args) throws IOException {
        URL resource = Piece.class.getResource("/resources/wp.png");
        if (resource == null) {
            throw new IllegalStateException("Missing image: wp.png");
        }
        Image whiteImg = ImageIO.read(resource);
    }
}