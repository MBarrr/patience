/**
 * Displays the card images on a table (the Javafx stage)
 * @author Faisal Rezwan, Chris Loftus and Lynda Thomas
 * @version 3.0
 */
package uk.ac.aber.dcs.cs12320.cards.gui.javafx;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.ArrayList;

public class CardTable {
    private final Stage stage;
    private String[] cards;
    private boolean done;
    private boolean isSidewaysMode;
    private boolean isRainbowMode;
    private int rotation;

    public CardTable(Stage stage) {
        this.isSidewaysMode = false;
        this.stage = stage;
        this.rotation = 0;
        this.isRainbowMode = false;
        stage.setTitle("The Cards");
    }

    /**
     * Called when the user quits the game. It results
     * in the face-down pack of cards not being displayed.
     */
    public void allDone() {
        done = true;
    }

    /**
     * Displays all the face-up cards (just the top showing cards)
     * and if the game is not over then also displays the face-down deck.
     * @param c the list of face-up cards
     */
    public void cardDisplay(ArrayList<String> c) {

        // We need to do this within the GUI thread. We assume
        // that the method is called by a non-GUI thread
        Platform.runLater(() -> {
            cards = new String[c.size()];
            cards = c.toArray(cards);

            ScrollPane sp = new ScrollPane();






            Scene scene = new Scene(sp, 1920, 1080);

            scene.setFill(Color.BLACK);
            HBox box = new HBox();
            Image image = null;

            box.setRotate(rotation);

            if (cards != null) {
                for (String card : cards) {
                    //Changed this to add .gif to the end of the string
                    String file = "cards/" + card+".gif";
                    image = new Image(getClass().getClassLoader().getResource(file).toString(), true);
                    drawCards(box, image);
                }
            }

            if (!done) {
                // Draws the face-down top card of our pack of cards
                String file = "cards/b.gif";
                image = new Image(getClass().getClassLoader().getResource(file).toString(), true);
                drawCards(box, image);
            }

            if(isRainbowMode){
                String file = "cards/rainbow.gif";
                image = new Image(getClass().getClassLoader().getResource(file).toString(), true);

                BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);



                Background background = new Background(backgroundImage);



                sp.setBackground(background);

            }

            sp.setContent(box);
            sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

            stage.setScene(scene);
            stage.show();
        });

    }

    private void drawCards(HBox box, Image image) {
        ImageView iv;
        iv = new ImageView();
        // resizes the image to have width of 100 while preserving the ratio and using
        // higher quality filtering method; this ImageView is also cached to
        // improve performance
        iv.setImage(image);
        iv.setFitWidth(100);
        iv.setPreserveRatio(true);
        iv.setSmooth(true);
        iv.setCache(true);


        box.getChildren().add(iv);
    }

    public void changeSidewaysMode(){
        System.out.println("Sidewaysing...");
        this.isSidewaysMode = !this.isSidewaysMode;

        if(isSidewaysMode){
            rotation = 90;
        }
        else{
            rotation = 0;
        }
    }

    public void changeRainbowMode(){
        this.isRainbowMode = !this.isRainbowMode;

    }
}
