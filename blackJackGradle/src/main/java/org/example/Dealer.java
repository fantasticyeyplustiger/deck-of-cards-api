package org.example;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

public class Dealer extends Player {

    ArrayList<Image> cardImages = new ArrayList<>();

    public Dealer(Deck deck, GUI window) {
        super(deck, window);
    }

    /**
     * Checks if the {@link Dealer} has to hit or stand by seeing
     * if the total value of all its {@link Card}s is less than 17.
     * @return true if the total value is less than 17; false otherwise.
     */
    public boolean checkIfHasToHit(){
        return getTotalValue() < 17;
    }

    public void printCards(){
        System.out.println("Dealers cards:");
        System.out.println("   " + cards.getFirst().value + " of " + cards.getFirst().suit);
        System.out.println("   An Unknown Card\n");
    }

    public void printAllCards(){
        System.out.println("Dealers cards:");
        super.printCards();
    }

    public void addCards(){
        while(checkIfHasToHit()){
            addCard();
        }
    }

    public void showFirstCards(){
        window.addCardImageToTop(cardImages.getFirst());
        window.addCardImageToTop(backOfCard());
    }

    public void showHand(){
        window.topCardArea.removeAll();
        for(Image image : cardImages){
            window.addCardImageToTop(image);
        }
    }

    public void addCardImage(Card card){
        Image image = card.getCardImage();
        cardImages.add(image);
    }

    public void resetCards(){
        removeCards();
        window.topCardArea.removeAll();
        initializeHand();
    }

    private Image backOfCard(){
        try {
            URL url = new URI("https://deckofcardsapi.com/static/img/back.png").toURL();
            return ImageIO.read(url);
        }
        catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeCards(){
        while(!cards.isEmpty()) {
            cards.remove();
            cardImages.removeLast();
        }
    }
}
