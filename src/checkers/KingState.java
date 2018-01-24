package checkers;

import java.io.Serializable;

/** Implements the state pattern for the metods findValidMoves and 
 * findValidJumps in class Checker.
 */
public class KingState implements CheckerState,Serializable {    
   
   public boolean findValidMoves(final CheckerPosition c, final Board board, 
                              MoveList validMoves) {
      if (! findValidJumps(c, board, validMoves)) {         
      
         if (GameSearch.validKingMove(c.getPosition(), c.getPosition().downLeftMove(), 
             board)) 
            validMoves.add(new MoveNormal(c, c.getPosition().downLeftMove()));
           
         if (GameSearch.validKingMove(c.getPosition(), c.getPosition().downRightMove(), 
             board))
            validMoves.add(new MoveNormal(c, c.getPosition().downRightMove()));
        
         if (GameSearch.validKingMove(c.getPosition(), c.getPosition().upLeftMove(), 
             board))
            validMoves.add(new MoveNormal(c, c.getPosition().upLeftMove()));
            
         if (GameSearch.validKingMove(c.getPosition(), c.getPosition().upRightMove(), 
             board))
            validMoves.add(new MoveNormal(c, c.getPosition().upRightMove()));
         return false;
      }
      else
         return true;
   }
   
   
   public boolean findValidJumps(final CheckerPosition c, final Board board, 
                              MoveList validJumps) {
      boolean found = false;                              
      if (GameSearch.validKingJump(c.getPosition(), c.getPosition().downLeftJump(), 
          board)) {
         validJumps.add(new MoveJump(c, c.getPosition().downLeftJump()));
         found = true;
      }
      
      if (GameSearch.validKingJump(c.getPosition(), c.getPosition().downRightJump(), 
          board)) {
         validJumps.add(new MoveJump(c, c.getPosition().downRightJump()));
         found = true;
      }
    
      if (GameSearch.validKingJump(c.getPosition(), c.getPosition().upLeftJump(), 
          board)) {
         validJumps.add(new MoveJump(c, c.getPosition().upLeftJump()));
         found = true;
      }
      
      if (GameSearch.validKingJump(c.getPosition(), c.getPosition().upRightJump(), 
          board)) {
         validJumps.add(new MoveJump(c, c.getPosition().upRightJump()));
         found = true;
      }
      return found;
   }
}