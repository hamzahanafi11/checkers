package checkers;

import javax.swing.ImageIcon; 
import java.net.URL;

public class BlackChecker extends CheckerPosition {

   public BlackChecker(Coordinate c) {
      checkerState = new NormalStateBlack();
      position = c;
      value = BLACK_VALUE_NORMAL;
      stringRep = "X";
   }
     
   
   public int getColor() {
      return BLACK;
   }
   
   
  
   
   
   public void makeKing() {
      checkerState = new KingState();
      value = BLACK_VALUE_KING;
      stringRep = "B";
   }
   
   
   public boolean isKing() {
      return (value == BLACK_VALUE_KING);
   }
   
     
   public boolean kingRow() {
      return ( (position.get() >= 29) && (position.get() <= 32) );
   }
   
   
   public CheckerPosition copy() {
      CheckerPosition newChecker = new BlackChecker(position);
      if (value == BLACK_VALUE_KING)
         newChecker.makeKing();
      return newChecker;
   }          

    @Override
    public ImageIcon getIcon() {
        return  null;
    }
}