/**
 * The Game of patience main class
 * @author Faisal Rezwan, Chris Loftus and Lynda Thomas
 * @version 3.0
 */

import java.util.*;

import javafx.application.Application;
import javafx.stage.Stage;
import uk.ac.aber.dcs.cs12320.cards.gui.javafx.CardTable;

public class Game extends Application {

    //Stores the number values of the king, queen, jack, ace
    private Dictionary<Integer, Character> cardValues = new Hashtable<>();
    private ArrayList<String> deck = new ArrayList<>();
    private ArrayList<String> cardStrings = new ArrayList<>();
    private boolean hasStarted = false;


    private CardTable cardTable;
    private static char[] suitLetters;

    @Override
    public void start(Stage stage) {

        suitLetters = new char[]{'c', 'd', 's', 'h'};

        cardValues.put(10, 't');
        cardValues.put(11, 'j');
        cardValues.put(12, 'q');
        cardValues.put(13, 'k');
        cardValues.put(14, 'a');
        cardTable = new CardTable(stage);

        generateCards();

        // The interaction with this game is from a command line
        // menu. We need to create a separate non-GUI thread
        // to run this in. DO NOT REMOVE THIS.
        Runnable commandLineTask = () -> {
            // REPLACE THE FOLLOWING EXAMPLE WITH YOUR CODE
            cardTable.cardDisplay(cardStrings);
            runOptions();
            System.out.println("NO... WHAT DO YOU MEAN QUIT... GOODBYE.........");
        };
        Thread commandLineThread = new Thread(commandLineTask);
        // This is how we start the thread.
        // This causes the run method to execute.
        commandLineThread.start();
    }


    public static void main(String args[]) {
        //Game game = new Game();
        //game.playGame();
        Application.launch(args);
    }

    private void generateCards(){
        System.out.println("Generating cards");

        for(char suitLetter:suitLetters){

            for(int i = 2; i < 10; i++){
                String cardName = i+Character.toString(suitLetter);
                deck.add(cardName);
            }

            for(int i = 10; i < 15; i++){
                String cardName = cardValues.get(i)+Character.toString(suitLetter);
                deck.add(cardName);
            }
        }

        System.out.println("Generated cards");
    }

    private void runOptions(){

        if(deck.size() == 0){
            endRound();
        }

        String response;
        do {

            //If there are no more cards in the deck, end the round
            if(deck.size() == 0){
                endRound();
                return;
            }

            showMenu();
            System.out.println("What would you like to do:");
            Scanner scan = new Scanner(System.in);
            response = scan.nextLine().toUpperCase();

            switch(response){
                case "C":
                    //Print Deck
                    for(String cardString: deck){
                        System.out.println(cardString);
                    }
                    break;
                case "S":
                    //Shuffle Deck
                    if(!hasStarted) {
                        System.out.println("I have shuffled the deck for you.");
                        Collections.shuffle(deck);
                    }
                    else{
                        System.out.println("You cannot shuffle the deck after starting the game.");
                    }
                    break;
                case "D":
                    //Deal a card
                    if(!hasStarted) hasStarted = true;
                    cardStrings.add(deck.get(0));
                    deck.remove(0);
                    break;
                case "M":
                    //move last acrd onto previous pile
                    movePrevious();
                    break;
                case "N":
                    //Move last card over 2
                    moveTwoOver();
                    break;
                case "A":
                    //Amalgamate piles
                    amalgamate();

                    break;
                case "Y":
                    //Print card piles
                    for(String cardString:cardStrings){
                        System.out.println(cardString);
                    }
                case "P":
                    //PLay for me
                    playForMe();
                    break;
                case "T":
                    //Show top 10 scores
                    break;
                case "Q":
                    break;
                case "U":
                    cardTable.changeSidewaysMode();
                    break;
                case "F":
                    cardTable.changeRainbowMode();
                    break;
                default:
                    System.out.println("Not an option.");
                    runOptions();
            }
            cardTable.cardDisplay(cardStrings);
        }
        while(!response.equals("Q"));
    }

    private void endRound(){
        int score = cardStrings.size();
        System.out.println("Game over. Your score was: "+score);
        Scanner scan = new Scanner(System.in);
        System.out.println("Would you like to play again?");
        boolean again = scan.nextBoolean();

        if(again){

        }

        else{
            return;
        }

    }

    private void playForMe(){
        System.out.println("Computer is searching for moves...");
        for(int i = 0; i < cardStrings.size()+3; i++){
            if(moveAtIndexToIndex(i+3, i)){
                return;
            }
        }

        for(int i = 0; i < cardStrings.size()+1; i++){
            if(moveAtIndexToIndex(i+1, i)){
                return;
            }
        }

        System.out.println("Computer found no moves.");
    }

    private void amalgamate(){
        Scanner scan = new Scanner(System.in);
        //Get card to be moved and position to be moved to
        System.out.println("Which card are you moving");
        int firstCardIndex = scan.nextInt()-1;
        System.out.println("Where are you moving it to?");
        int secondCardIndex = scan.nextInt()-1;

        //Get difference between the two positions
        int indexDifference = firstCardIndex - secondCardIndex;

        //Get the absolute value of the difference
        int absDifference = Math.abs(indexDifference);

        //If the absolute value of the difference between the two positions does not equal
        //1 or 3, do not allow the move, this will prevent illegal moves, but still allow
        //I am using the absolute value to allow amalgamated cards to be moved to the right as well as the left
        if(absDifference == 3 || absDifference == 1){
            moveAtIndexToIndex(firstCardIndex, secondCardIndex);
        }

        else{
            System.out.println("You cannot make that move.");
        }
    }

    private boolean moveTwoOver(){
        return moveAtIndexToIndex(cardStrings.size()-1, cardStrings.size()-4);
    }

    private boolean movePrevious(){
        return moveAtIndexToIndex(cardStrings.size()-1, cardStrings.size()-2);
    }

    private boolean moveAtIndexToIndex(int index1, int index2){

        //Get the index of the last and second last card
        int lastCardIndex = index1;
        int secondLastCardIndex = index2;


        String lastCard;
        String secondLastCard;

        //get last drawn card and second last drawn card
        try {
            lastCard = cardStrings.get(lastCardIndex);
            secondLastCard = cardStrings.get(secondLastCardIndex);
        }catch(IndexOutOfBoundsException e){
            System.out.println("This card cannot move here.");
            return false;
        }

        //Get the suit and value of the last drawn card
        char lastCardSuit = lastCard.charAt(0);
        char lastCardValue = lastCard.charAt(1);

        //Get the suit and value of the second last drawn card
        char secondLastCardSuit = secondLastCard.charAt(0);
        char secondLastCardValue = secondLastCard.charAt(1);

        System.out.println(lastCard);
        System.out.println(secondLastCard);

        //Check if the move is valid, if not, return
        if(lastCardSuit != secondLastCardSuit && lastCardValue != secondLastCardValue){
            System.out.println("This card cannot move here.");
            return false;
        }

        System.out.println("Moved "+ lastCard + " to " + secondLastCard);

        cardStrings.set(secondLastCardIndex, lastCard);
        cardStrings.remove(lastCardIndex);
        return true;
    }


    private void showMenu(){
        System.out.println("C - Show Deck");
        System.out.println("S - Shuffle Deck");
        System.out.println("D - Deal a card");
        System.out.println("M - Move last card onto previous pile");
        System.out.println("N - Move last card onto the pile skipping over 2 piles");
        System.out.println("A - Amalgamate piles");
        System.out.println("Y - Print card piles");
        System.out.println("P - Play for me");
        System.out.println("T - Show top 10 scores");
        System.out.println("Q - QUIT");
    }

}
