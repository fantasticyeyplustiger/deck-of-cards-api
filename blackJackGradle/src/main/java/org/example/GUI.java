package org.example;

import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame {

    JSplitPane mainArea = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    JSplitPane dialogueAndPlayArea = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    JSplitPane cardAreas = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

    public JButton hitButton, standButton, doubleButton, splitButton = new JButton();
    public JPanel bottomCardArea, topCardArea, playOptionsArea = new JPanel();
    public JLabel moneyLabel, bettingMoneyLabel = new JLabel();
    public JTextArea dialogue = new JTextArea();

    public GUI(){

        this.setSize(1920, 1080);
        this.setLocationRelativeTo(null);
        this.setTitle("Blackjack");

        this.add(mainArea);

        mainArea.setLeftComponent(cardAreas);
        mainArea.setRightComponent(dialogueAndPlayArea);
        mainArea.setDividerLocation(1100);

        dialogueAndPlayArea.setLeftComponent(dialogue);
        dialogueAndPlayArea.setRightComponent(playOptionsArea);
        dialogueAndPlayArea.setDividerLocation(500);

        cardAreas.setTopComponent(topCardArea);
        cardAreas.setBottomComponent(bottomCardArea);
        cardAreas.setDividerLocation(540);

        playOptionsArea.add(moneyLabel);
        playOptionsArea.add(bettingMoneyLabel);

        playOptionsArea.add(hitButton);
        playOptionsArea.add(standButton);
        playOptionsArea.add(doubleButton);
        playOptionsArea.add(splitButton);

        this.setVisible(true);
    }



    public void addCardImageToTop(Image image){
        JLabel label = new JLabel(new ImageIcon(image));
        topCardArea.add(label);
    }

    public void addCardImageToBottom(Image image){
        JLabel label = new JLabel(new ImageIcon(image));
        bottomCardArea.add(label);
    }
}
