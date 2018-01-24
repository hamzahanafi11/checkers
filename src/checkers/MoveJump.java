package checkers;

public class MoveJump extends Move {
 
   public MoveJump(CheckerPosition checker, Coordinate destination) {
      this.checker = checker;
      this.destination = destination;
   }
  
  
   public boolean isJump() {
      return true;
   }
   
   
   public Move copy() {
      return new MoveJump(checker.copy(), destination);
   }
   
   
   // Return a copy of this move from the newBoard.   
   public Move copy(Board newBoard) {
      return new MoveJump(newBoard.getChecker(checker.getPosition()), 
                          destination);
   }
      
  
   // Returns the coordinate of the captured checker of this move.
   public Coordinate capturedCoordinate() {
      if (checker.getPosition().row() - destination.row() == 2) { // Up.
         if (checker.getPosition().column() - destination.column() == 2) //Up,left.
            return checker.getPosition().upLeftMove();      
         else
            return checker.getPosition().upRightMove();
      }
      else { // Down.
         if (checker.getPosition().column() - destination.column() == 2)//Down,left.
            return checker.getPosition().downLeftMove();      
         else
            return checker.getPosition().downRightMove();     
      }
   }
  
   
   public String toString() {
      String s = "";
      if (checker.getColor() == CheckerPosition.BLACK) s = "Black-J:"; else s = "White-J:";
      s = s + "(" + checker.getPosition() + "-" + destination + ")";
      return s; 
   }
}