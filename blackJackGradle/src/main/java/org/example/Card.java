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
}
