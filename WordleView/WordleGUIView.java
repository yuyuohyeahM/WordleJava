/**
 * 
 * @author Mike Yu
 * This file present a GUI view for the game Wordle.
 * 
 * @Animation When typing a character: the current box pop.
 * @Animation When ENTER is pressed, whole row is pop.
 */

package view;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Observer;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;


import controller.WordleController;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.WordleModel;
import utilities.Guess;
import utilities.INDEX_RESULT;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;



@SuppressWarnings("deprecation")
public class WordleGUIView extends Application implements Observer {
	/* Constants for the scene */
	private static final int SCENE_SIZE = 900;

	/* Constants for grid of letters */
	private static final int GRID_GAP = 10;

	/* Constants for letters in grid */
	private static final int LETTER_FONT_SIZE = 75;
	private static final int LETTER_SQUARE_SIZE = 90;
	private static final int GUESS_F = 30;
	private static final int GUESS_S = 40;
	
	WordleModel mod;
	WordleController con;
	Guess[] progress;
	INDEX_RESULT[] guessedChar;
	int labR = 0;
	int labC = 0;
	String inp = "";
	
	BorderPane window;
	Label[][] lb;
	GridPane gpU;
	GridPane gpD;
	Stage stage;
	Scene scene;
	Canvas canvas;
	VBox vbox;
	List<String> dict_lst;

	/** Create/set up any necessary object needed for this GUI view. Handle the keyevent. */
	@Override
	public void start(Stage stageO) throws Exception{
		stage = stageO;
		mod = new WordleModel();
		mod.addObserver(this);
		con = new WordleController(mod);
		progress = con.getProg();
		guessedChar = con.getGuessed();
		
		// get the dictionary file
		dictSetUp();
		// UPPER GridPane for guess boxes
        setUpperGP();
		// Lower GridPane for guessed boxes
		setLowerGP();
		window = new BorderPane();
		vbox = new VBox();
		// add upper/lower gridpane to vbox
		vbox.getChildren().add(gpU);
		vbox.getChildren().add(gpD);
		vbox.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
		scene = new Scene(vbox, SCENE_SIZE, SCENE_SIZE);
		// event handler for upper gridpane.
		scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			
			@SuppressWarnings("static-access")
			@Override
			public void handle(KeyEvent ke) {
				
				switch(ke.getCode()) {
					// if DELETE or BACK_SPACE is entered:
					// set the Label's text to " ", remove last char in inp.
					case DELETE:
					case BACK_SPACE:
						if (labC > 0) {
							lb[labR][labC-1].setText(" ");
							inp = inp.substring(0, inp.length()-1);
							labC -= 1;
						}
						break;
						// if ENTER is entered:
						// call makeGuess, which will then notifyObservers, call update.
					case ENTER:
						if (labC == 5) {
							if (dict_lst.contains(inp.toLowerCase())) {
								con.makeGuess(inp);
								labR += 1;
								labC = 0;
								inp = "";
							} else {
								Alert a = new Alert(Alert.AlertType.WARNING);
								a.setHeaderText("Try again");
								a.setContentText("Not in word list.");
								a.showAndWait();
							}
						}
						break;
						// else update current Label with letter input.
					default:
						if (labC < 5) {
							String input = ke.getCode().getName();
							if (input.charAt(0) >= 'A' && input.charAt(0) <= 'Z' && input.length() == 1) {
								for (Node node : gpU.getChildren()){
									if (gpU.getRowIndex(node) == labR && gpU.getColumnIndex(node) == labC ) {
										((Label)node).setText(input);
										ScaleTransition ani1 = new ScaleTransition(Duration.seconds(0.25), node);
										ani1.setFromX(1.0);
										ani1.setToX(1.1);
										ani1.setFromY(1.0);
										ani1.setToY(1.1);
										// Play the Animation
										ani1.play();
										ScaleTransition ani2 = new ScaleTransition(Duration.seconds(0.25), node);
										ani2.setFromX(1.1);
										ani2.setToX(1.0);
										ani2.setFromY(1.1);
										ani2.setToY(1.0);
										// Play the Animation
										ani2.play();
									}
								}
								inp += input;
								labC += 1;
							}
						}
				}
			}
		});
		stage.setScene(scene);
		stage.setTitle("W-O-R-D-L-E");
		stage.show();
	}
	/**
	 * This class set up the gridpane that represent the 6 row 5 col guesses
	 * boxes in the game.
	 * A box is represent by a Label object, text in a box is originally empty.
	 */
	private void setUpperGP() {
		gpU = new GridPane();
		gpU.setPrefSize(400, 400);
	    gpU.setPadding(new Insets(10, 10, 10, 10)); 
	    gpU.setVgap(GRID_GAP);
	    gpU.setHgap(GRID_GAP);
	    gpU.setAlignment(Pos.CENTER);
		lb = new Label[6][5];
		for(int i=0; i<lb.length; i++){
			for(int j=0; j < 5;j++){
				lb[i][j] = new Label();
				lb[i][j].setAlignment(Pos.CENTER);
				lb[i][j].setFont(Font.font("Neue Helvetica", FontWeight.EXTRA_BOLD,
						FontPosture.REGULAR, LETTER_FONT_SIZE));
				lb[i][j].setPrefSize(LETTER_SQUARE_SIZE, LETTER_SQUARE_SIZE);
				lb[i][j].setTextFill(Color.BLACK);
				lb[i][j].setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
				gpU.add(lb[i][j], j, i);
			}
		}
	}
	/**
	 *  Set up the dictionary file.
	 */
	private void dictSetUp() throws FileNotFoundException {
		Scanner dict = new Scanner(new File(con.getFileName()));
		dict_lst = new ArrayList<String>();
		while (dict.hasNext()) {
            dict_lst.add(dict.nextLine());
        }
        dict.close();
	}
	
	/**
	 * This class set up the gridpane that represent the keyboard-like
	 * boxes in the game.
	 * A box is represent by a Label object, text in a box is originally an character.
	 */
	private void setLowerGP() {
		gpD = new GridPane();
		gpD.setPrefSize(400, 400);
	    gpD.setPadding(new Insets(10, 10, 10, 10)); 
	    gpD.setVgap(GRID_GAP);
	    gpD.setHgap(GRID_GAP);
	    gpD.setAlignment(Pos.CENTER);
	    String kb1 = "QWERTYUIOP";
	    String kb2 = "ASDFGHJKL";
	    String kb3 = "ZXCVBNM";
	    for (int i = 0; i < kb1.length();i++) {
	    	Label toAdd = new Label(Character.toString(kb1.toCharArray()[i]));
	    	toAdd.setAlignment(Pos.CENTER);
	    	toAdd.setFont(Font.font("Neue Helvetica", FontWeight.EXTRA_BOLD,
					FontPosture.REGULAR, GUESS_F));
	    	toAdd.setPrefSize(GUESS_S, GUESS_S);
	    	toAdd.setTextFill(Color.BLACK);
	    	toAdd.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
	    	gpD.add(toAdd, i, 0);
	    }
	    for (int i = 0; i < kb2.length();i++) {
	    	Label toAdd = new Label(Character.toString(kb2.toCharArray()[i]));
	    	toAdd.setAlignment(Pos.CENTER);
	    	toAdd.setFont(Font.font("Neue Helvetica", FontWeight.EXTRA_BOLD,
					FontPosture.REGULAR, GUESS_F));
	    	toAdd.setPrefSize(GUESS_S, GUESS_S);
	    	toAdd.setTextFill(Color.BLACK);
	    	toAdd.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
	    	gpD.add(toAdd, i, 1);
	    }
	    for (int i = 0; i < kb3.length();i++) {
	    	Label toAdd = new Label(Character.toString(kb3.toCharArray()[i]));
	    	toAdd.setAlignment(Pos.CENTER);
	    	toAdd.setFont(Font.font("Neue Helvetica", FontWeight.EXTRA_BOLD,
					FontPosture.REGULAR, GUESS_F));
	    	toAdd.setPrefSize(GUESS_S, GUESS_S);
	    	toAdd.setTextFill(Color.BLACK);
	    	toAdd.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
	    	gpD.add(toAdd, i, 2);
	    }
	}
	
	/**
	 * launch the GUI view.
	 */
	public static void main(String[] args) throws FileNotFoundException   {
		launch(args);
	}
	
	@SuppressWarnings("static-access")
	@Override
	public void update(Observable o, Object arg) {
		// updating the upper gridpane
		Guess[] gue = (Guess[]) arg;
		INDEX_RESULT[] indices = gue[labR].getIndices();
		int acc = 0;
		for (Node node : gpU.getChildren()){
			if (gpU.getRowIndex(node) == labR) {
				((Label) node).setTextFill(Color.WHITE);
				if (indices[acc].getDescription().compareTo(INDEX_RESULT.CORRECT_WRONG_INDEX.getDescription()) == 0){
					((Label) node).setBackground(new Background(new BackgroundFill(Color.BURLYWOOD, CornerRadii.EMPTY, Insets.EMPTY)));
				} else if (indices[acc].getDescription().compareTo(INDEX_RESULT.CORRECT.getDescription()) == 0){
					((Label) node).setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
				} else if (indices[acc].getDescription().compareTo(INDEX_RESULT.INCORRECT.getDescription()) == 0){
					((Label) node).setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
				} 
				// animation to pop cur row all at once to makeguess.
				ScaleTransition ani1 = new ScaleTransition(Duration.seconds(0.25), node);
				ani1.setFromX(1.0);
				ani1.setToX(1.2);
				ani1.setFromY(1.0);
				ani1.setToY(1.2);
				ani1.play();
				ScaleTransition ani2 = new ScaleTransition(Duration.seconds(0.25), node);
				ani2.setFromX(1.2);
				ani2.setToX(1.0);
				ani2.setFromY(1.2);
				ani2.setToY(1.0);
				ani2.play();
				
				acc += 1;
			}
		}
		// check if the game is over.
		// if the game is over, pop an Alert.
		if (con.isGameOver()) {
			vbox.setDisable(true);
			Alert a = new Alert(Alert.AlertType.INFORMATION);
			a.setHeaderText("Good Game!");
			a.setContentText("Good game! The word was " + con.getAnswer() + ".");
			a.showAndWait();
		}
		
		// update the lower gridpane.
		INDEX_RESULT[] gc = mod.getGuessedCharacters();
		int ascii = 65;
		for (int c =0; c < 26; c++) {
			for (Node node : gpD.getChildren()){
				if (((char) (ascii+c)) == ((Label)node).getText().charAt(0)) {
					if (gc[c] != null) {
						((Label) node).setTextFill(Color.WHITE);
						if (gc[c].getDescription().compareTo(INDEX_RESULT.CORRECT_WRONG_INDEX.getDescription()) == 0){
							((Label) node).setBackground(new Background(new BackgroundFill(Color.BURLYWOOD, CornerRadii.EMPTY, Insets.EMPTY)));
						} else if (gc[c].getDescription().compareTo(INDEX_RESULT.CORRECT.getDescription()) == 0){
							((Label) node).setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
						} else if (gc[c].getDescription().compareTo(INDEX_RESULT.INCORRECT.getDescription()) == 0){
							((Label) node).setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
						} 
					}
				}
			}
		}
		
	}

}
