# Wordle.Java
Game: Wordle simulator

The famous 5-letters-word-guessing game: wordle (https://www.nytimes.com/games/wordle/index.html) simulation using MVC pattern.

  * Foundation: Observed and learned the rules and layout from Wordle. Built up the game with the software architectural pattern MVC.
  * Animated: Designed and created the GUI view of the game including animation with JavaFX.
  * Handled different invalid inputs from the user.

## Implementation
GUI: Both the guesses and the guessed characters are Label objects placed inside GridPane objects. These two separate GridPane objects are placed inside of a VBox which is then placed inside of the Scene. 

Event Handling

Observer/Observable: For the GUI front end, when the controller changes the model, we’d like to change the view. The model can do this for us, by notifying us when the model changes if our view is an Observer and the model is an Observable. 

1.	Wordle – This is the main class. When invoked with a command line argument of “-text”, you will launch the text-oriented UI. When invoked with a command line argument of “-gui” you’ll launch the GUI view. The default will be the GUI view if no command line argument is given. This class should be very short.
2.	WordleGUIView – This is the JavaFX GUI as described above
3.	WordleTextView – This is the UI that we built in project 2 with some slight modifications described below.
4.	WordleController – This class contains all of the game logic, and must be shared by the textual and graphical UIs. You may not call into different controllers from the different UIs and all methods provided must be useful to both front ends.
5.	WordleModel – This class contains all of the game state and must also be shared between the two front ends.
