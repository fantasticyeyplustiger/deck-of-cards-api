package org.example;

import com.google.gson.*;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {

    public static Deck deck;
    public static LinkedList<Card> playerCards = new LinkedList<>();
    public static GsonBuilder builder = new GsonBuilder();
    public static Gson gson;
    public static Scanner scan = new Scanner(System.in);

    public static GUI window = new GUI();

    public static double money = 250.0;
    public static double bettingMoney;

    public static void main(String[] args) {

        window.hitButton.addActionListener(e -> hit());

        builder.setPrettyPrinting();
        gson = builder.create();

        Scanner shuffledDeckScan = new Scanner(getShuffledDeck());
        String shuffledDeck = shuffledDeckScan.useDelimiter("\\A").next();

        deck = gson.fromJson(shuffledDeck, Deck.class);
    }

    private static void play(){

        //hitButton.addActionListener(e -> hit());
        //standButton.addActionListener(e -> stand());
        //doubleButton.addActionListener(e -> doubleBet());
        //splitButton.addActionListener(e -> split());


        /*
        Enemy enemy = new Enemy();

        playerCards.add(getCard());
        enemy.cards.add(getCard());
        playerCards.add(getCard());
        enemy.cards.add(getCard());

        System.out.println("welcome to blackjack.");
        System.out.println("in this game, you have to get as close to 21 as possible without going over 21.");
        System.out.println("how much would you like to bet?\n");

        System.out.println("your money: " + money);

        bettingMoney = getBettingMoney();

        System.out.println("ok\n");


        while(true){

            System.out.println("your cards\n");

            printCards(playerCards);

            System.out.println("their cards\n");

            printCards(enemy.cards);

            System.out.println("add another card?\n[YES, NO]");

            if(scan.nextLine().equalsIgnoreCase("Yes")){
                System.out.println("ok!");
                playerCards.add(getCard());
            }

            enemy.cards.add(getCard());

            roundIsOver(enemy);

        }
         */

    }

    private static void hit(){

        playerCards.add(getCard());
        Image image = playerCards.getLast().getCardImage();
        window.addCardImageToBottom(image);

    }


    private static void roundIsOver(Enemy enemy, int currentRound){
        int playerStatus = getTotalValue(playerCards);
        int enemyStatus = getTotalValue(enemy.cards);
    }

    private static int getTotalValue(LinkedList<Card> cards){
        int totalValue = 0;
        for(Card card : cards){
            try{
                totalValue += Integer.parseInt(card.value);
            }
            catch (NumberFormatException e) {
                totalValue += 10;
            }
        }

        return totalValue;

    }

    public static void printCards(LinkedList<Card> cards){
        for (Card card : cards) {
            System.out.println(card.code + ", " + card.value);
        }
    }

    private static double getBettingMoney(){
        while(true){
            try{
                double returningBettingMoney = Double.parseDouble(scan.nextLine());

                if(returningBettingMoney > money){
                    System.out.println("you don't have that much money.");
                }
                else if(returningBettingMoney <= 0){
                    System.out.println("ah, yes, betting negative money.");
                }
                else{
                    return returningBettingMoney;
                }

            } catch (NumberFormatException e) {
                System.out.println("what?");
            }
        }
    }

    private static Card getCard(){

        Scanner cardScan = new Scanner(getCardDetails());
        String newCard = cardScan.useDelimiter("\\A").next();

        JsonElement rootNode = JsonParser.parseString(newCard);

        JsonObject deckDetails = rootNode.getAsJsonObject();
        JsonArray cardDetails = deckDetails.getAsJsonArray("cards");

        String cardValues = String.valueOf(cardDetails);
        cardValues = removeBrackets(cardValues); //converts it from a json array to a json object

        return gson.fromJson(cardValues, Card.class);
    }

    /**
     * Removes brackets from a String.
     * @param string The String having brackets removed.
     * @return The string without brackets.
     */
    private static String removeBrackets(String string){

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

    private static InputStream getCardDetails(){
        try {
            URLConnection url = new URL
                    ("https://www.deckofcardsapi.com/api/deck/" + deck.deck_id + "/draw/?count=1")
                    .openConnection();

            return url.getInputStream();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static InputStream getShuffledDeck() {
        try {
            URLConnection url = new URL("https://www.deckofcardsapi.com/" +
                    "api/deck/new/shuffle/?deck_count=6").openConnection();

            return url.getInputStream();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}