package checkers;

public class MoveNormal extends Move {
   
   public MoveNormal(CheckerPosition checker, Coordinate destination) {
      this.checker = checker;
      this.destination = destination;
   }
   
   
   public boolean isJump() {
      return false;
   }
   
   
   public Move copy() {
      return new MoveNormal(checker.copy(), destination);
   }

   
   // Return a copy of this move from the newBoard.   
   public Move copy(Board newBoard) {
      return new MoveNormal(newBoard.getChecker(checker.getPosition()), 
                            destination);
   }
   
   
   public String toString() {
      String s = "Move: ";
      if (checker.getColor() == CheckerPosition.BLACK) s = "Black:"; else s = "White:";
      s = s + "(" + checker.getPosition() + "-" + destination + ")";
      return s; 
   }
}