package checkers;

import java.io.Serializable;

/** 
 * A linked list of moves.
 */
public class MoveList implements Serializable{
   private Move moveList;
   private Move last;
   private int listSize;
   
   
   public MoveList() {
      listSize = 0;   
      moveList = null;
   }


   public void add(Move c) {
      if (moveList == null) { // first element.
         moveList = c;
         last = c;
      }
      else {
         last.setNext(c);
         last = c;
      }               
      listSize++;
   } 
   
   
   public void remove(Move move) {
      if (move == moveList) {  // first element.
         moveList = moveList.getNext();
         listSize--;
      }
      else {
         MoveIterator iterator = getIterator();   
         Move previous = iterator.next();    // previous = first element.
         Move current = null;
         while (iterator.hasNext()) {
            current = iterator.next();
            if (move == current) {
               previous.setNext(current.getNext());
               listSize--;
            }
         }
      }
   }
      
   
   public int size() {
      return listSize;
   }   
   
   
   public Move first() {
      return moveList;
   }
   
   
   public void reset() {
      listSize = 0;
      moveList = null;
   }
   
   
   public Move get(int index) throws IndexOutOfBoundsException {
      int current = 0;
      Move move = moveList;
      while (current != index) {
         move = move.getNext();
         if (move == null) throw new IndexOutOfBoundsException(); 
         current++;
      }
      return move;
   }
   
   
   public MoveIterator getIterator() {
      return new MoveIterator(this);
   }
   
   
   public MoveList copy() {
      MoveIterator iterator = getIterator();
      MoveList newList = new MoveList();
      while (iterator.hasNext())
         newList.add(iterator.next().copy());
      return newList;
   }
   
   
   public String toString() {
      MoveIterator iterator = getIterator();
      String s = "Movelist: ";
      while (iterator.hasNext()) {
         s = s + iterator.next().toString();
         if (iterator.hasNext()) s = s + " , ";
      }
      return s;
   }
}