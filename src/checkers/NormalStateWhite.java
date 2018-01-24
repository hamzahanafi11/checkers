package checkers;

import java.io.Serializable;

/** Implements the state pattern for the metods findValidMoves and 
 * findValidJumps in class Checker.
 */
public class NormalStateWhite implements CheckerState,Serializable {   
   
   // Attaches valid moves to the validMoves list. Returns true if a valid jump
   // exist.
   public boolean findValidMoves(final CheckerPosition c, final Board board, 
                              MoveList validMoves) {   
      if (! findValidJumps(c, board, validMoves)) {
         // If no valid jump exist then look for valid moves.
         if (GameSearch.validWhiteMove(c.getPosition(), c.getPosition().upLeftMove(), 
             board))
            validMoves.add(new MoveNormal(c, c.getPosition().upLeftMove()));
            
         if (GameSearch.validWhiteMove(c.getPosition(), c.getPosition().upRightMove(), 
             board))
            validMoves.add(new MoveNormal(c, c.getPosition().upRightMove()));
         return false;
     }
     else
      return true; 
      
   }   
   
   
   public boolean findValidJumps(CheckerPosition c, Board board, MoveList validJumps) {
      boolean found = false;
      if (GameSearch.validWhiteJump(c.getPosition(), c.getPosition().upLeftJump(), 
          board)) {
         validJumps.add(new MoveJump(c, c.getPosition().upLeftJump()));
         found = true;
      }
      
      if (GameSearch.validWhiteJump(c.getPosition(), c.getPosition().upRightJump(), 
          board)) {
         validJumps.add(new MoveJump(c, c.getPosition().upRightJump()));
         found = true;
      }
      return found;
   }
}

