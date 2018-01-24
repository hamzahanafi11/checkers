package checkers;
 
import java.io.Serializable;
import javax.swing.ImageIcon; 

public abstract class CheckerPosition extends Position implements Serializable{
   
   public static final int BLACK = 1;
   public static final int WHITE = 2;
   public static final int WHITE_VALUE_NORMAL = 2;
   public static final int BLACK_VALUE_NORMAL = -2;
   public static final int WHITE_VALUE_KING = 3;
   public static final int BLACK_VALUE_KING = -3;
   
   protected CheckerState checkerState;
   protected Coordinate position;
   protected int value;
   protected String stringRep;
   
   public abstract boolean isKing();
   public abstract int getColor();
   public abstract void makeKing();
   public abstract CheckerPosition copy();
   public abstract boolean kingRow();
   public abstract ImageIcon getIcon();
   
   
   public Coordinate getPosition() {
      return position;
   } 
   
   
   public void setPosition(Coordinate c) {
      position = c;
   }
   
   
   public int getValue() {
      return value;
   }
   
   
   public boolean findValidMoves(MoveList moveList, final Board board) {
      return checkerState.findValidMoves(this, board, moveList);  
   }
   
   
   public boolean findValidJumps(MoveList moveList, final Board board) {
      return checkerState.findValidJumps(this, board, moveList);  
   }
   
   
   public String toString() {
      return stringRep;  
   }
    
}