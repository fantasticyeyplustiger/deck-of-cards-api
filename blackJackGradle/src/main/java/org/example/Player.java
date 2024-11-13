package org.example;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.Scanner;

import static org.example.Main.gson;

public class Player {

    Deck deck;
    LinkedList<Card> cards = new LinkedList<>();
    double money = 2500.0;
    double bettingMoney;

    /**
     * For convenience.
     * @param deck The {@link Deck} that cards will be drawing from.
     */
    public Player(Deck deck){
        this.deck = deck;
    }

    public boolean checkForLoss(){
        return getTotalValue() > 21;
    }

    /**
     * Gets the total value of the {@link Card}s in the player's hand.
     * Aces will automatically give a value of 11, but if the total value is over 21,
     * they will only give a value of 1.
     * @return The total value.
     */
    public int getTotalValue(){
        int totalValue = 0;
        int amountOfAces = 0;

        //gets every value in the list of cards and adds them all together
        for(Card card : cards){
            try{
                totalValue += Integer.parseInt(card.value);
            }
            catch (NumberFormatException e) {

                if(card.value.equalsIgnoreCase("ACE")){
                    amountOfAces++;
                }

                totalValue += card.getWordCardValue();
            }
        }

        //this will lower the value of the deck if it is both possible and needed
        if(totalValue > 21 && amountOfAces > 0){
            totalValue = lowerAceValues(totalValue, amountOfAces);
        }

        return totalValue;

    }

    /**
     * Gives the player the amount of money they won from the round.
     * @param moneyMultiplier How much more money they should receive from their betting money.
     */
    public void winMoney(double moneyMultiplier){
        bettingMoney *= moneyMultiplier;
        money += bettingMoney;
        bettingMoney = 0;
    }

    /**
     * Subtracts 10 for every ace the player has until their {@link Card}s' total value
     * is less than or equal to 21.
     * @param totalValue The total value before being lowered.
     * @param amountOfAces The amount of aces the player has.
     * @return The lowered total value.
     */
    public int lowerAceValues(int totalValue, int amountOfAces){
        while(totalValue > 21 && amountOfAces > 0){
            totalValue -= 10;
            amountOfAces -= 1;
        }

        return totalValue;
    }

    public void printCards(){
        for(Card card : cards){
            System.out.println("   " + card.value + " of " + card.suit);
        }

        System.out.println("\n   Total value: " + getTotalValue() + "\n");
    }

    /**
     * Adds a {@link Card} to the player's hand.
     */
    public void addCard(){

        JsonElement rootNode = JsonParser.parseString(getCardDetails());

        //gets the actual card information
        JsonObject deckDetails = rootNode.getAsJsonObject();
        JsonArray cardDetails = deckDetails.getAsJsonArray("cards");

        String cardValues = String.valueOf(cardDetails);
        cardValues = removeBrackets(cardValues); //converts it from a json array to a json object

        cards.add(gson.fromJson(cardValues, Card.class));
    }

    /**
     * Removes all brackets from a {@link String}.
     * @param string The {@link String} having brackets removed.
     * @return The {@link String} without brackets.
     */
    private String removeBrackets(String string){

        StringBuilder returningString = new StringBuilder();

        for (int i = 0; i < string.length(); i++) {

            char letter = string.charAt(i);

            if(letter == '[' || letter == ']'){
                continue;
            }

            returningString.append(letter);
        }

        return String.valueOf(returningString);
    }

    /**
     * Gets an {@link InputStream} from the deck of cards API method, "Draw a Card",
     * and converts it into a {@link String}.
     * @return A {@link String} in JSON format containing {@link Card} information.
     */
    private String getCardDetails(){
        try {
            URLConnection url = new URI
                    ("https://www.deckofcardsapi.com/api/deck/" + deck.deck_id + "/draw/?count=1").toURL()
                    .openConnection();

            Scanner cardScan = new Scanner(url.getInputStream());

            return cardScan.useDelimiter("\\A").next();

        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds two {@link Card}s to the player's hand.
     */
    public void initializeHand(){
        addCard();
        addCard();
    }

    /**
     * Empties the {@link Card}s in the player's hand and re-initializes them.
     */
    public void resetCards(){
        cards = new LinkedList<>();
        initializeHand();
    }
}
