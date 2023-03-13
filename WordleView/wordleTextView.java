/**
 * Class: CSC 335
 * Project: WordleTextView.java
 * Arthur: Mike Yu
 * Description: You have 6 guesses to reveal a hidden 5-letter word. After each guess, 
 * letters that are in the hidden word are revealed. However, these letters are revealed 
 * differently depending on if they are in the same index in both the guess and the hidden word 
 * or if the letter is in both the guess and the hidden word but in a different index in each.
 */

package view;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import controller.WordleController;
import model.WordleModel;
import utilities.Guess;
import utilities.INDEX_RESULT;

/**
 *  This class is a custom exception to handle invalid guess input
 *  Throw when:
 *  	1. length of guess is not 5
 *  	2. our dictionary does not contain the guess input
 *  	3. the guess contains not only letters.
 *  @param: 
 *  	str: the error message you want to tell the user.
 */
@SuppressWarnings("serial")
class UnacceptableInputException  extends Exception  
{  
    public UnacceptableInputException (String str)  
    {  
        // calling the constructor of parent Exception  
        super(str);  
    }  
}

public class WordleTextView {
	/**
	 *  This main function provides the overall view of this game: Wordle.
	 *  1. It first create the WordleModel and WordleController 'this' run of game.
	 *  2. then ask the user for their guess of words while not game-over.
	 *  3. check if the input is valid, if not, throw exception and ask for the input again
	 *  4. make the Guess: Check for correctness.
	 *  5. print out the current status of the game. (correctness, guessed/unguessed characters lists)
	 *  6. ask if the user wants to play again (yes/no)
	 */
	public static void main(String[] args) throws FileNotFoundException {
		Boolean cont = true;
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		while (cont) {
			// 1
			WordleModel mod = new WordleModel();
			WordleController con = new WordleController(mod);
			boolean gg = true;
			while (gg) {
				System.out.print("Enter a guess: ");
				try {
					// 2
					String guess = in.nextLine();
					// Put all word in a ArrayList to check if input is valid
					Scanner dict = new Scanner(new File(con.getFileName()));
					List<String> dict_lst = new ArrayList<String>();
					while (dict.hasNext()) {
			            dict_lst.add(dict.nextLine());
			        }
			        dict.close();
			        boolean onlyChar = true;
			        for (char ch: guess.toCharArray()) {
			        	if (!Character.isLetter(ch)) {
			        		onlyChar = false;
			        		break;
			        	}
			        }
			        // 3
			        if (guess.length() != 5) {
						throw new UnacceptableInputException("Your guess should be exactly 5 letters.");
					} else if (!onlyChar) {
						throw new UnacceptableInputException("Your guess should only contain letter.");
					} else if (!(dict_lst.contains(guess))) {
						throw new UnacceptableInputException("Our Dictionary does not contain your guess.");
					}
			        // 4
					con.makeGuess(guess);
					Guess[] prog = mod.getProgress();
					// 5
					for (int i =0; i < prog.length; i++) {
						if (prog[i] != null) {
							int c = 0;
							for (char ch: prog[i].getGuess().toCharArray()) {
								if (prog[i].getIndices()[c].getDescription().compareTo(INDEX_RESULT.CORRECT_WRONG_INDEX.getDescription()) == 0) {
									System.out.print(Character.toLowerCase(ch) + " ");
								} else if (prog[i].getIndices()[c].getDescription().compareTo(INDEX_RESULT.CORRECT.getDescription()) == 0) {
									System.out.print(Character.toUpperCase(ch) + " ");
								} else {
									System.out.print("_" + " ");
								}
								c += 1;
							}
						} else {
							System.out.print("_ _ _ _ _");
						}
						System.out.println("");
					}
					System.out.println("");
					
					// START PRINTING CHAR LIST
					int ascii = 65;
					ArrayList<Character> correct = new ArrayList<>();
					ArrayList<Character> incorrect = new ArrayList<>();
					ArrayList<Character> mismatch = new ArrayList<>();
					ArrayList<Character> unguessed = new ArrayList<>();
	
					for (int c =0; c < 26; c++) {
						if (mod.getGuessedCharacters()[c] == null) {
							unguessed.add((char) (ascii+c));
						} else if (mod.getGuessedCharacters()[c].getDescription().compareTo(INDEX_RESULT.CORRECT_WRONG_INDEX.getDescription()) == 0){
							mismatch.add((char) (ascii+c));
						} else if (mod.getGuessedCharacters()[c].getDescription().compareTo(INDEX_RESULT.CORRECT.getDescription()) == 0){
							correct.add((char) (ascii+c));
						} else if (mod.getGuessedCharacters()[c].getDescription().compareTo(INDEX_RESULT.INCORRECT.getDescription()) == 0){
							incorrect.add((char) (ascii+c));
						} 
					}
					System.out.println("Unguess: " + unguessed.toString());
					System.out.println("Correct:" + correct.toString());
					System.out.println("Incorrect: " + incorrect.toString());
					System.out.println("Right character but in wrong spot: "+ mismatch.toString());
					if (con.isGameOver() == true) {
						gg = false;
					}
				}
				catch(UnacceptableInputException e){
					System.out.println("Exception caught: " + e);
					System.out.println("Please enter a guess again: ");
				}
			}
			// show answer and ask if the user want to play again
			System.out.println("Good game! The word was " + con.getAnswer() + ".");
			System.out.println("Would you like to play again? yes/no");
			String c = in.nextLine();
			if (c.equals("no")) {
				cont = false;
			}
		}

    }
    
}
