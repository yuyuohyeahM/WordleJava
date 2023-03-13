/**
 * Class: CSC 335
 * Project: Wordle.java
 * Arthur: Mike Yu
 * Description: You have 6 guesses to reveal a hidden 5-letter word. After each guess, 
 * letters that are in the hidden word are revealed. However, these letters are revealed 
 * differently depending on if they are in the same index in both the guess and the hidden word 
 * or if the letter is in both the guess and the hidden word but in a different index in each.
 */

package view;
import javafx.application.Application;

public class Wordle{
    public static void main(String[] args) throws Exception{
    	if (args.length == 0 || args[0] == "-gui") {
    		Application.launch(WordleGUIView.class, args);
    	} else if (args[0] == "-text") {
    		WordleTextView.main(args);
    	}
    }
    
}
