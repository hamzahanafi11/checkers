package checkers;

import java.io.StreamTokenizer;
import java.io.InputStreamReader;
import java.io.IOException;

public class FormattedInput {   
   
   private StreamTokenizer keyboard = new StreamTokenizer(
                                     new InputStreamReader(System.in)); 
   
   
   public int readCoordinate(String output) {
      boolean number = false;
      while (! number) {
         System.out.print(output);
         try {
            if ( (keyboard.nextToken() == keyboard.TT_NUMBER) &
                  (keyboard.nval >= 1) & (keyboard.nval <= 32) ) 
               number = true;
            else
               System.out.println("Wrong input.");
         }
         catch (IOException e) {
            System.out.println("Wrong input.");
         }
      }
      return (int) keyboard.nval;
   }         
   
   
   public int readColor() {
      boolean correct = false;
      int input = 0;
      while (! correct) {
          input = readCoordinate("Please choose color - black (1) " +
                                      "or White (2):");
         if ((input == CheckerPosition.BLACK) || (input == CheckerPosition.WHITE))
            correct = true;
         else
            System.out.println("Please type 1 for black and 2 for white.");
      }
      return input;
   }
}