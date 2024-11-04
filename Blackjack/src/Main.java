import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static String[] shuffledDeckValues = new String[4];
    public static double money = 250.0;
    public static double betting_power = 1;

    public static void main(String[] args) {

        System.out.println("start gambling buddy");

        Scanner scan = new Scanner(shuffleDeck());
        String shuffledDeck = scan.useDelimiter("\\A").next();
        shuffledDeck = removeUnnecessaryLetters(shuffledDeck);

        System.out.println(shuffledDeck);
    }

    private static String setShuffledDeckValues(String value){
        //get a specific value from a string in java tab
    }

    /**
     * Removes quotation marks, spaces, and curly braces from a string.
     * @param string The string having letters removed.
     * @return The string with removed letters.
     */
    private static String removeUnnecessaryLetters(String string) {
        StringBuilder returnedString = new StringBuilder();

        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == ' ' || string.charAt(i) == '"' || string.charAt(i) == '{'
        || string.charAt(i) == '}') {
                continue;
            }
            returnedString.append(string.charAt(i));
        }

        return String.valueOf(returnedString);
    }

    public static InputStream shuffleDeck() {
        try {
            URLConnection url = new URL("https://www.deckofcardsapi.com/" +
                    "api/deck/new/shuffle/?deck_count=1").openConnection();

            return url.getInputStream();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String executePost(String targetURL, String urlParameters) {
        HttpURLConnection connection = null;

        try {
            //Create connection
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            connection.setRequestProperty("Content-Length",
                    Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches(false);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream(
                    connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.close();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}