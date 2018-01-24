package checkers;

/** 
 * A linked list of boards.
 */
public class BoardList {
   private Board boardList;
   private Board last;
   private int listSize;
   
   
   public BoardList() {
      listSize = 0;   
      boardList = null;
   }


   public void add(Board b) {
      if (boardList == null) { // first element.
         boardList = b;
         last = b;
      }
      else {
         last.setNext(b);     // attach to end of list.
         last = b;
      }               
      listSize++;
   } 
   
   
   public int size() {
      return listSize;
   }   
   
   
   public Board first() {
      return boardList;
   }
   
   
   public Board get(int index) throws IndexOutOfBoundsException {
      int current = 0;
      Board board = boardList;
      while (current != index) {
         board = board.getNext();
         if (board == null) throw new IndexOutOfBoundsException(); 
         current++;
      }
      return board;
   }
   
   
   public BoardIterator getIterator() {
      return new BoardIterator(this);
   }
   
   
   public Board findBestBoard(int color) {
      BoardIterator iterator = getIterator();
      Board bestBoard = iterator.next();
      while (iterator.hasNext()) {
         if (color == CheckerPosition.WHITE) 
            bestBoard = GameSearch.maxBoard(bestBoard, iterator.next());
         else 
            bestBoard = GameSearch.minBoard(bestBoard, iterator.next());
      }
      return bestBoard;   
   }

   
   public String toString() {
      BoardIterator iterator = getIterator();
      String s = "BoardList: ";
      while (iterator.hasNext()) {
         s = s + iterator.next().toString();
         if (iterator.hasNext()) s = s + " , ";
      }
      return s;
   }
}