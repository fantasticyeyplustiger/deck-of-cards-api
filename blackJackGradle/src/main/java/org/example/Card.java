package org.example;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class Card {

    String code;
    String image;
    String value;
    String suit;

    public Card(){}

    public Image getCardImage() {
        if(image == null){
            throw new NullPointerException();
        }

        try {
            URL url = new URL(image);
            return ImageIO.read(url);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the value of non-numerical cards, such as KINGS.
     * @return 11 for ACES, 10 for KINGS, QUEENS, and JACKS, and -9999 for anything else.
     */
    public int getWordCardValue(){
        switch (value){
            case "KING", "QUEEN", "JACK" -> {return 10;}
            case "ACE" -> {return 11;}
            default -> {return -9999;} //for debugging purposes
        }
    }
}
