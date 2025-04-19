package pgn_parser;

import models.Board;

import java.util.HashSet;
import java.util.Set;

public class PgnParser {
    private Board board;
    private Set<Character> files = new HashSet<>(Set.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'));
    private Set<Character> rank=new HashSet<>(Set.of('1','2','3','4','5','6','7','8'));
    private Set<Character> pieces=new HashSet<>(Set.of('N','B'));

    public boolean isMoveSyntaxRight(String move){
        if(move.length()==2){
            return files.contains(move.charAt(0))&&rank.contains(move.charAt(1));
        }
        if(move.length()==3){
            return (pieces.contains(move.charAt(0))&&files.contains(move.charAt(1))&&rank.contains(move.charAt(2)))||move.equals("O-O");
        }
        if(move.length()==5){
            return move.equals("O-O-O");
        }
         return false;
    }

  public boolean ParsePGN(String game){
      return false;
  }
}
