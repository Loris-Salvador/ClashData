package com.ClashData.Model;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {

    //ID is equal to the CONCATENATION of the differents Card's ID
    private String id;
    private ArrayList<Integer> cardsId = new ArrayList<>(8);
    private int evoCardID;

    public Deck()
    {
        evoCardID = -1;
    }

    public String getId(){
        return id;
    }



    public ArrayList<Integer> getCardsId() {
        return cardsId;
    }
    public void addCardsId(int CardId) {
        cardsId.add(CardId);
        Collections.sort(cardsId);
        updateId();

    }

    public int getEvoCardID() {
        return evoCardID;
    }

    public void setEvoCardID(int evoIndex) {
        this.evoCardID = evoIndex;
    }


    private void updateId()
    {
        StringBuilder builder = new StringBuilder();

        for (Integer card : cardsId) {
            builder.append(card);
        }

        id = builder.toString();
    }

    @Override
    public String toString() {
        return "Deck{" +
                "id='" + id + '\'' +
                ", cardsId=" + cardsId +
                ", evoCardID=" + evoCardID +
                '}';
    }

    //TESTS
    public static void main(String[] args) {

        Deck deck = new Deck();

        deck.addCardsId(5);
        deck.addCardsId(3);
        deck.addCardsId(4);

        System.out.println(deck.getCardsId());
        System.out.println(deck.getId());

    }

}
