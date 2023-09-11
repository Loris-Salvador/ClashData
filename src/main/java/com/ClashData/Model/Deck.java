package com.ClashData.Model;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {

    //ID is equal to the CONCATENATION of the differents Card's ID
    private int id;
    private ArrayList<Integer> cardsId = new ArrayList<>(8);
    private int evoIndex;

    public Deck()
    {
        evoIndex = -1;
    }

    public int getId(){
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

    public int getEvoIndex() {
        return evoIndex;
    }

    public void setEvoIndex(int evoIndex) {
        this.evoIndex = evoIndex;
    }


    private void updateId()
    {
        id = 0;

        for(int i : cardsId)
        {
            id = id + i;
        }
    }



    //TESTS
    public static void main(String[] args) {

        Deck deck = new Deck();

        deck.addCardsId(5);
        deck.addCardsId(3);

        System.out.println(deck.getCardsId());
        System.out.println(deck.getId());

    }

}
