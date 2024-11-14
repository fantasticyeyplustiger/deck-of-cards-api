package org.example;

public class Dealer extends Player {

    public Dealer(Deck deck) {
        super(deck);
    }

    /**
     * Checks if the {@link Dealer} has to hit or stand by seeing
     * if the total value of all its {@link Card}s is less than 17.
     * @return true if the total value is less than 17; false otherwise.
     */
    public boolean checkIfHasToHit(){
        return getTotalValue() < 17;
    }

    public void printCards(){
        System.out.println("Dealers cards:");
        System.out.println("   " + cards.getFirst().value + " of " + cards.getFirst().suit);
        System.out.println("   An Unknown Card\n");
    }

    public void printAllCards(){
        System.out.println("Dealers cards:");
        super.printCards();
    }

    public void addCards(){
        while(checkIfHasToHit()){
            addCard();
        }
    }
}
