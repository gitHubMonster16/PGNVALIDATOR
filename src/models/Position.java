package models;

public class Position {
    int x;
    int y;
   public Position(int x,int y){
       this.x=x;
       this.y=y;
   }
    public boolean isValid() {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }

    public String toAlgebraic() {
        return "" + (char) ('a' + x) + (8 - y);  // Convert y correctly as 1-based index
    }

}
