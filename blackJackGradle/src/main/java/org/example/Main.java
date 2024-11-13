package org.example;

import com.google.gson.*;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.sql.SQLOutput;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {

    public static Deck deck;
    public static GsonBuilder builder = new GsonBuilder();
    public static Gson gson;
    public static Scanner scan = new Scanner(System.in);

    //public static GUI window = new GUI();

    public static Player player;
    public static Dealer dealer;
    public static boolean hasNatural = true;
    public static boolean didDouble = false;
    public static boolean isStanding = false;

    public static void main(String[] args) {

        //window.hitButton.addActionListener(e -> hit());

        builder.setPrettyPrinting();
        gson = builder.create();

        deck = getShuffledDeck();

        player = new Player(deck);
        dealer = new Dealer(deck);

        player.initializeHand();
        dealer.initializeHand();

        playOneRound();
    }

    private static void playOneRound(){

        boolean playerLoss = false;
        int currentRound = 0;

        player.bettingMoney = getBettingMoney();

        //region deal with naturals
        switch (checkForNatural()) {

            case PLAYER_NATURAL -> {
                player.winMoney(1.5);
                System.out.println("Natural Win!\n\nMoney: " + player.money + "\n\n");
            }

            case DEALER_NATURAL -> {
                player.bettingMoney = 0;
                System.out.println("Natural Loss!\n\nMoney: " + player.money + "\n\n");
            }

            case BOTH_NATURAL -> {
                player.money += player.bettingMoney;
                player.bettingMoney = 0;
                System.out.println("Both Naturals! No money lost.\n\nMoney: " + player.money + "\n\n");
            }

            default -> {
                hasNatural = false;
            }
        }

        if(hasNatural) {
            player.resetCards();
            dealer.resetCards();
            playOneRound();
        }

        hasNatural = true;
        //endregion

        while(currentRound < 5){

            System.out.println("Dealers cards:");

            if(currentRound == 0) {
                dealer.printCards();
            }
            else{
                dealer.printAllCards();
            }

            System.out.println("Your cards:");
            player.printCards();

            if(!didDouble){
                playerMove(currentRound);
                playerLoss = player.checkForLoss();
            }

            if(dealer.checkIfHasToHit()){
                dealer.addCard();
            }
            if((didDouble || isStanding || playerLoss) && !(dealer.checkIfHasToHit())){
                printEndResults( whoWon() );
                return;
            }

            currentRound++;

        }

    }

    private static void printEndResults(MatchEnd matchEnd){

        switch (matchEnd){
            case PUSH -> {
                System.out.println("Push. No money lost.");
                player.money += player.bettingMoney;
                player.bettingMoney = 0;
            }
            case PLAYER_WIN, PLAYER_BLACKJACK -> {
                System.out.println("You won twice your bet!");
                player.money += (player.bettingMoney * 2);
                player.bettingMoney = 0;
            }
            case DEALER_WIN, DEALER_BLACKJACK -> {
                System.out.println("You lost your bet!");
                player.bettingMoney = 0;
            }
        }

    }

    private static void playerMove(int currentRound){

        while(true) {

            if (currentRound == 0) {
                System.out.println("HIT[1], STAND[2], OR DOUBLE[3]?");
            } else {
                System.out.println("HIT[1] or STAND[2]?");
            }

            String answer = scan.nextLine();

            switch (answer) {
                case "1" -> hit();
                case "2" -> isStanding = true;
                case "3" -> {
                    if (currentRound == 0) {
                        doDouble();
                    }
                }
                default -> {continue;}
            }

            break;
        }
    }

    private static MatchEnd checkForNatural(){
        MatchEnd endResults = whoWon();

        switch(endResults){
            case PLAYER_BLACKJACK -> {return MatchEnd.PLAYER_NATURAL;}
            case DEALER_BLACKJACK -> {return MatchEnd.DEALER_NATURAL;}
            case PUSH_BLACKJACK -> {return MatchEnd.BOTH_NATURAL;}
            default -> {
                hasNatural = false;
                return MatchEnd.NO_NATURAL;
            }
        }
    }

    private static MatchEnd whoWon(){

        int playerValue = player.getTotalValue();
        int dealerValue = dealer.getTotalValue();

        if(playerValue == 21 && dealerValue == 21){
            return MatchEnd.PUSH_BLACKJACK;
        }
        else if(playerValue == 21){
            return MatchEnd.PLAYER_BLACKJACK;
        }
        else if(dealerValue == 21){
            return MatchEnd.DEALER_BLACKJACK;
        }

        if(playerValue == dealerValue){
            return MatchEnd.PUSH;
        }
        else if(playerValue > dealerValue){
            return MatchEnd.PLAYER_WIN;
        }
        else {
            return MatchEnd.DEALER_WIN;
        }
    }

    private static void doDouble(){
        didDouble = true;
        player.addCard();

    }

    private static void hit(){

        player.addCard();


        //Image image = player.cards.getLast().getCardImage();
        //window.addCardImageToBottom(image);

    }


    private static void roundIsOver(int currentRound){
        int playerValue = player.getTotalValue();
        int enemyValue = dealer.getTotalValue();


    }

    public static void printCards(LinkedList<Card> cards){
        for (Card card : cards) {
            System.out.println(card.code + ", " + card.value);
        }
    }

    private static double getBettingMoney(){
        //while(true){
            try{
                double returningBettingMoney = Double.parseDouble(scan.nextLine());

                if(returningBettingMoney > player.money){
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

            return -10;
        //}
    }

    public static Deck getShuffledDeck() {
        try {
            URLConnection url = new URI("https://www.deckofcardsapi.com/" +
                    "api/deck/new/shuffle/?deck_count=6").toURL().openConnection();

            Scanner shuffledDeckScan = new Scanner(url.getInputStream());
            String shuffledDeck = shuffledDeckScan.useDelimiter("\\A").next();

            return gson.fromJson(shuffledDeck, Deck.class);

        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}