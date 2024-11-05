package org.example;

import com.google.gson.*;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {

    public static Deck deck;
    public static LinkedList<Card> cards = new LinkedList<>();
    public static GsonBuilder builder = new GsonBuilder();
    public static double money = 250.0;
    public static double betting_power = 1;

    public static void main(String[] args) {

        builder.setPrettyPrinting();

        Gson gson = builder.create();

        System.out.println("start gambling buddy\n");

        Scanner shuffledDeckScan = new Scanner(getShuffledDeck());
        String shuffledDeck = shuffledDeckScan.useDelimiter("\\A").next();

        deck = gson.fromJson(shuffledDeck, Deck.class);

        Card card = getCard();

        cards.add(card);
        System.out.println(gson.toJson(card));

        //Card card = gson.fromJson(newCard, Card.class);
        //cards.add(card);

        //System.out.println(gson.toJson(card));

    }

    private static Card getCard(){

        Card card = new Card();
        JsonElement[] elements = new JsonElement[4];

        Scanner cardScan = new Scanner(getCardDetails());
        String newCard = cardScan.useDelimiter("\\A").next();

        JsonElement rootNode = JsonParser.parseString(newCard);

        System.out.println(rootNode);

        JsonObject deckDetails = rootNode.getAsJsonObject();
        JsonArray cardDetails = deckDetails.getAsJsonArray("cards");

        System.out.println(cardDetails);

        for (int i = 0; i < cardDetails.size(); i++) {
            elements[i] = cardDetails.get(i);
        }

        card.code = elements[0].getAsString();
        card.image = elements[1].getAsString();
        card.value = elements[2].getAsString();
        card.suit = elements[3].getAsString();

        return card;
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
                    "api/deck/new/shuffle/?deck_count=1").openConnection();

            return url.getInputStream();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}