/**
 * Class: CSC 335
 * Project: WordleModel.java
 * Arthur: Mike Yu
 * Description: You have 6 guesses to reveal a hidden 5-letter word. After each guess, 
 * letters that are in the hidden word are revealed. However, these letters are revealed 
 * differently depending on if they are in the same index in both the guess and the hidden word 
 * or if the letter is in both the guess and the hidden word but in a different index in each.
 */

package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Random;
import java.util.Scanner;

import utilities.Guess;
import utilities.INDEX_RESULT;

@SuppressWarnings("deprecation")
public class WordleModel extends Observable {
	
	private static final String FILENAME = "dictionary.txt";
	private String answer;
	private Guess[] progress;
	private int guesses;
	/** 
	 * Maintains an array of INDEX_RESULTs for the guessed characters. There
	 * should be 26 indices in this array, one for each character in the english
	 * alphabet. Before a character has been guessed, its position in the array
	 * should hold the value 'null'.
	 */
	private INDEX_RESULT[] guessedCharacters;
	
	/**
	 *  This function generate a random word from the dictionary file
	 *  and make it the answer of a game.
	 */
	private String genWord() throws FileNotFoundException{
		Scanner dict = new Scanner(new File(FILENAME));
		List<String> dict_lst = new ArrayList<String>();
		while (dict.hasNext()) {
            dict_lst.add(dict.nextLine());
        }
        dict.close();
        int rndN = new Random().nextInt(dict_lst.size());
        return dict_lst.get(rndN);
	}
	
	public WordleModel() throws FileNotFoundException { 
		this.answer = genWord();
        this.guessedCharacters = new INDEX_RESULT[26];
        this.progress = new Guess[6];
		this.guesses = 0;
	}

	/**
	 * This function create a Guess for the user's guess, it makes a 
	 * INDEX_RESULT array "guess_cor", which represent the correctness of the guess, 
	 * if the guess is correct, all of the five index in the array should be "Correct"
	 * 
	 * Notify the Observer after changed.
	 */
	public void makeGuess(String guess) {
		INDEX_RESULT[] guess_cor = new INDEX_RESULT[5];
		// compare guess_cor to the answer to see the correctness of the guess.
		int pos = 0;
		for (char c : guess.toCharArray()) {
			int ans_pos = 0;
			for (char ans : this.answer.toCharArray()) {
				if (Character.toUpperCase(this.answer.toCharArray()[pos]) == c) {
					guess_cor[pos] = INDEX_RESULT.CORRECT;
					break;
				} else if (c == Character.toUpperCase(ans) && pos == ans_pos) {
					guess_cor[pos] = INDEX_RESULT.CORRECT;
					break;
				} else if (c == Character.toUpperCase(ans) && pos != ans_pos) {
					guess_cor[pos] = INDEX_RESULT.CORRECT_WRONG_INDEX;
					break;
				}
				ans_pos += 1;
			}
			pos += 1;
		}
		
		for (int i = 0; i < 5; i++) {
			if (guess_cor[i] == null) {
				guess_cor[i] = INDEX_RESULT.INCORRECT;
			}
		}
		boolean tf = true;
		// loop through the guess_cor, if any index is INCORRECT or CORRECT_WRONG_INDEX, tf = false, break
		for (int i = 0; i < 5; i++) {
			if (guess_cor[i].getDescription().compareTo(INDEX_RESULT.INCORRECT.getDescription()) == 0) {
				tf = false;
				break;
			} else if (guess_cor[i].getDescription().compareTo(INDEX_RESULT.CORRECT_WRONG_INDEX.getDescription()) == 0) {
				tf = false;
				break;
			}
		}
		Guess ret = new Guess(guess, guess_cor, tf);
		int ind = 0;
		for (char c: guess.toCharArray()) {
			this.guessedCharacters[Character.toUpperCase(c) - 'A'] =  ret.getIndices()[ind];
			ind += 1;
		}
		this.progress[this.guesses] = ret;
		this.guesses += 1;
		setChanged();
		notifyObservers(this.progress);
	}

	/**
	 *  Return this game's answer.
	 *  @return: 
	 *  	answer of a game
	 */
	public String getAnswer() {
		return this.answer;
	}

	/**
	 *  Return this game's guessed character list.
	 *  @return: 
	 *  	guessed character list of a game
	 */
	public INDEX_RESULT[] getGuessedCharacters() {
		return this.guessedCharacters;
	}
	
	/**
	 *  Return the progress.
	 *  @return: 
	 *  	the progress of a game.
	 */
	public Guess[] getProgress() {
		return this.progress;
	}
	
	/**
	 *  Return the file name.
	 *  @return: 
	 *  	name of the dictionary file.
	 */
	public String getFileName() {
		return WordleModel.FILENAME;
	}
	
	/**
	 * Return the attempt so far.
	 * @return:
	 * 		the cur attempt.
	 */
	public int getGuesses() {
		return this.guesses;
	}
}
