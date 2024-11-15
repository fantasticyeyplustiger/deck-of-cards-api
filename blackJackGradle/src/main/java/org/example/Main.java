package org.example;

import com.google.gson.*;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.util.Scanner;

public class Main {

    public static Deck deck;
    public static GsonBuilder builder = new GsonBuilder();
    public static Gson gson;
    public static Scanner scan = new Scanner(System.in);

    public static GUI window = new GUI();

    private static Player player;
    private static Dealer dealer;
    private static boolean hasNatural = true;
    private static boolean didDouble = false;
    private static boolean playerLoss = false;

    public static void main(String[] args) {

        builder.setPrettyPrinting();
        gson = builder.create();

        deck = getShuffledDeck();

        player = new Player(deck);
        dealer = new Dealer(deck);

        player.initializeHand();
        dealer.initializeHand();

        //main game loop
        while(deck.remaining > 10) {

            if(player.money <= 0){
                System.out.println("Get yo broke ahh out of here");
                System.exit(69);
            }

            playOneRound();
            player.resetCards();
            dealer.resetCards();

            playerLoss = false;
            didDouble = false;
        }
    }

    private static void playOneRound(){

        boolean firstRound = true;
        boolean quit = false;

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

            default -> hasNatural = false;

        }

        if(hasNatural) {
            player.resetCards();
            dealer.resetCards();
            playOneRound();
        }

        hasNatural = true;
        //endregion

        while(true){

            dealer.printCards();

            System.out.println("Your cards:");
            player.printCards();

            if(!didDouble){
                quit = playerMove(firstRound);
            }

            if(quit){
                break;
            }

            if((didDouble || playerLoss) && !(dealer.checkIfHasToHit())){
                printEndResults( whoWon() );
                return;
            }
            else{
                dealer.addCards();
            }

            firstRound = false;

        }

    }

    private static void printEndResults(MatchEnd matchEnd){

        dealer.printAllCards();
        System.out.println("Player's cards:");
        player.printCards();

        switch (matchEnd){
            case PUSH -> {
                System.out.println("Push. No money lost.");
                player.winMoney(1);
            }
            case PLAYER_WIN -> {

                if(didDouble){
                    System.out.println("You won FOUR times your bet!");
                    player.winMoney(4);
                }
                else {
                    System.out.println("You won twice your bet!");
                    player.winMoney(2);
                }

            }
            case PLAYER_BLACKJACK -> {

                if(didDouble){
                    System.out.println("You won SIX times your bet!");
                    player.winMoney(6);
                }
                else {
                    System.out.println("You won three times your bet!");
                    player.winMoney(3);
                }

            }
            case DEALER_WIN, DEALER_BLACKJACK -> {

                if(didDouble){
                    System.out.println("You lost twice your bet...");
                    player.winMoney(-1);
                }
                else {
                    System.out.println("You lost your bet!");
                    player.winMoney(0);
                }

            }
        }

    }

    private static boolean playerMove(boolean firstRound){

        while(true) {

            if (firstRound) {
                System.out.println("HIT[1], STAND[2], OR DOUBLE[3]?");
            } else {
                System.out.println("HIT[1] or STAND[2]?");
            }

            String answer = scan.nextLine();

            switch (answer) {
                case "1", "hit", "Hit", "HIT" -> hit();
                case "2", "stand", "Stand", "STAND" -> {
                    dealer.addCards();
                    printEndResults(whoWon());
                    return true;
                }
                case "3", "double", "Double", "DOUBLE" -> {
                    if (firstRound) {
                        doDouble();
                    }
                }
                default -> {continue;}
            }

            if(player.checkForLoss()){
                printEndResults(MatchEnd.DEALER_WIN);
                return true;
            }
            if(player.checkForBlackjack()){
                printEndResults(MatchEnd.PLAYER_BLACKJACK);
                return true;
            }

            break;
        }
        return false;
    }

    private static MatchEnd checkForNatural(){

        if(player.checkForBlackjack()){
            return MatchEnd.PLAYER_NATURAL;
        }
        else if(dealer.cards.getFirst().value.equalsIgnoreCase("ACE")){
            return MatchEnd.NO_NATURAL;
        }
        else if(dealer.checkForBlackjack()){
            return MatchEnd.DEALER_NATURAL;
        }
        else{
            return MatchEnd.NO_NATURAL;
        }
    }

    private static MatchEnd whoWon(){

        int playerValue = player.getTotalValue();
        int dealerValue = dealer.getTotalValue();

        if(playerValue == dealerValue){
            return MatchEnd.PUSH;
        }

        if(player.checkForLoss()){
            return MatchEnd.DEALER_WIN;
        }
        else if(dealer.checkForLoss()){
            return MatchEnd.PLAYER_WIN;
        }
        else if(playerValue > dealerValue){
            return MatchEnd.PLAYER_WIN;
        }
        else{
            return MatchEnd.DEALER_WIN;
        }
    }

    private static void doDouble(){
        didDouble = true;
        player.addCard();
    }

    private static void hit(){
        player.addCard();
        Image image = player.cards.getLast().getCardImage();
        window.addCardImageToBottom(image);
    }

    private static double getBettingMoney(){
        while(true){
            try{
                System.out.println("How much do you want to bet? Current money: " + player.money);
                double returningBettingMoney = Double.parseDouble(scan.nextLine());

                if(returningBettingMoney > player.money){
                    System.out.println("You don't have that much money.");
                }
                else if(returningBettingMoney <= 0){
                    System.out.println("You can't bet negative money.");
                }
                else{
                    player.money -= returningBettingMoney;
                    return returningBettingMoney;
                }

            } catch (NumberFormatException e) {
                System.out.println("what?");
            }
        }
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