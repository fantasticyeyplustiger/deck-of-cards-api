import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static String deckID;
    public static int remainingCards;
    public static double money = 250.0;
    public static double betting_power = 1;

    public static void main(String[] args) {

        System.out.println("start gambling buddy\n");

        Scanner scan = new Scanner(getShuffledDeck());
        String shuffledDeck = scan.useDelimiter("\\A").next();
        shuffledDeck = removeUnnecessaryLetters(shuffledDeck);

        setShuffledDeckValues(shuffledDeck);

        Scanner scann = new Scanner(getCard("1"));
        System.out.println(scann.useDelimiter("\\A").next());
    }

    private static void setShuffledDeckValues(String value){

        //looks at "value" as if it is a hash map
        Pattern pattern = Pattern.compile("(\\w+?):(\\w+?),");
        Matcher matcher = pattern.matcher(value);

        while(matcher.find()){
            switch (matcher.group(1)){ // the "key"
                case "deck_id" -> deckID = matcher.group(2); // the "value"
                case "remaining" -> remainingCards = Integer.parseInt(matcher.group(2));
            }
        }
    }

    /**
     * Removes quotation marks, spaces, and curly braces from a string.
     * @param string The string having letters removed.
     * @return The string with removed letters.
     */
    private static String removeUnnecessaryLetters(String string) {
        StringBuilder returnedString = new StringBuilder();

        for (int i = 0; i < string.length(); i++) {
            char letter = string.charAt(i);
            if (letter == ' ' || letter == '"' || letter == '{' || letter == '}') {
                continue;
            }
            returnedString.append(string.charAt(i));
        }

        return String.valueOf(returnedString);
    }

    private static InputStream getCard(String amountOfCards){
        try {
            URLConnection url = new URL
                    ("https://www.deckofcardsapi.com/api/deck/" + deckID + "/draw/?count=" + amountOfCards)
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