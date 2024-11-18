package org.example;

import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame {

    JSplitPane mainArea = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    JSplitPane dialogueAndPlayArea = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    JSplitPane cardAreas = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

    public JScrollPane dialogueArea = new JScrollPane();

    public JButton hitButton, standButton, doubleButton, splitButton;

    public JPanel bottomCardArea = new JPanel();
    public JPanel topCardArea = new JPanel();
    public JPanel playOptionsArea = new JPanel();

    public JTextArea dialogue = new JTextArea();

    public GUI(){

        hitButton = buttonSetUp("HIT");
        standButton = buttonSetUp("STAND");
        doubleButton = buttonSetUp("DOUBLE");
        splitButton = buttonSetUp("SPLIT");

        this.setSize(1920, 1080);
        this.setLocationRelativeTo(null);
        this.setTitle("Blackjack");

        this.add(mainArea);

        setSplitPaneComponents();
        addComponents();

        setComponentValues();

        this.setVisible(true);
    }

    public boolean isOff(JButton button){
        return button.getText().equals("...");
    }

    public void addCardImageToTop(Image image){
        JLabel label = new JLabel(new ImageIcon(image));
        topCardArea.add(label);
        repaint();
        revalidate();
    }

    public void addCardImageToBottom(Image image){
        JLabel label = new JLabel(new ImageIcon(image));
        label.setVisible(true);
        bottomCardArea.add(label);
        repaint();
        revalidate();
    }

    private JButton buttonSetUp(String text){
        JButton button = new JButton();

        button.setText(text);
        button.setPreferredSize(new Dimension(350, 250));
        button.setVisible(true);
        button.setEnabled(true);

        return button;
    }

    private void setComponentValues(){
        GridLayout verticalLayout = new GridLayout();
        verticalLayout.setColumns(1);
        verticalLayout.setRows(4);
        verticalLayout.setVgap(50);

        FlowLayout horizontalLayout = new FlowLayout();
        horizontalLayout.setAlignment(FlowLayout.LEFT);

        playOptionsArea.setLayout(verticalLayout);
        playOptionsArea.setMaximumSize(new Dimension(400, 1080));

        topCardArea.setLayout(horizontalLayout);
        bottomCardArea.setLayout(horizontalLayout);
    }

    private void setSplitPaneComponents(){
        mainArea.setLeftComponent(cardAreas);
        mainArea.setRightComponent(dialogueAndPlayArea);
        mainArea.setDividerLocation(1100);

        dialogueAndPlayArea.setLeftComponent(dialogueArea);
        dialogueAndPlayArea.setRightComponent(playOptionsArea);
        dialogueAndPlayArea.setDividerLocation(500);

        cardAreas.setTopComponent(topCardArea);
        cardAreas.setBottomComponent(bottomCardArea);
        cardAreas.setDividerLocation(540);
    }

    private void addComponents(){
        dialogueArea.add(dialogue);

        playOptionsArea.add(hitButton);
        playOptionsArea.add(standButton);
        playOptionsArea.add(doubleButton);
        playOptionsArea.add(splitButton);
    }
}
