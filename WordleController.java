/**
 * Class: CSC 335
 * Project: WordleController.java
 * Arthur: Mike Yu
 * Description: You have 6 guesses to reveal a hidden 5-letter word. After each guess, 
 * letters that are in the hidden word are revealed. However, these letters are revealed 
 * differently depending on if they are in the same index in both the guess and the hidden word 
 * or if the letter is in both the guess and the hidden word but in a different index in each.
 */

package controller;

import model.WordleModel;
import utilities.Guess;
import utilities.INDEX_RESULT;

public class WordleController {
	
	/**
	 * Maintains the progress the user has made so far. This array should have
	 * as many indices as there are turns/guesses for the user. Indices for turns
	 * that the user has not had yet (that are in the future) should be indicated
	 * by a 'null' value.
	 */
	private WordleModel model;
	
	public WordleController (WordleModel model) {
		this.model = model;
	} 
	
	/** Returns true if the game is over, false if not. */
	public boolean isGameOver() {
		if (model.getGuesses() == 0) {
			return false;
		}
		Guess n = model.getProgress()[model.getGuesses()-1];
		if (n.getIsCorrect()){
			return true;
		}
		if (model.getGuesses() == 6) {
			return true;
		}
		return false;
	}
	
	/**
	 *  Return this game's answer.
	 *  @return: 
	 *  	answer of a game
	 */
	public String getAnswer() {
		return this.model.getAnswer();
	}
	
	/**
	 * Create a Guess object and put it into progress
	 * add 1 to guesses.
	 * @param:
	 * 		guess: the guess input by the user.
	 */
	public void makeGuess(String guess) {
		this.model.makeGuess(guess);
	}
	
	/**
	 *  Return the file name.
	 *  @return: 
	 *  	name of the dictionary file.
	 */
	public String getFileName() {
		return this.model.getFileName();
	}
	
	/**
	 *  Return the progress.
	 *  @return: 
	 *  	the progress of a game.
	 */
	public Guess[] getProg() {
		return this.model.getProgress();
	}
	
	/**
	 *  Return this game's guessed character list.
	 *  @return: 
	 *  	guessed character list of a game
	 */
	public INDEX_RESULT[] getGuessed() {
		return this.model.getGuessedCharacters();
	}
}
