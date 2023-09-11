package com.ClashData.Model;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Date;

public class Battle {

    private Deck homeDeck;
    private Deck awayDeck;
    private String homePlayerTag;
    private String awayPlayerTag;
    private boolean isWin;
    private Date date;
    private Mode mode;

    public Battle(JsonNode jsonNode)
    {
        System.out.println("Constructor");
    }

    public Deck getHomeDeck() {
        return homeDeck;
    }

    public void setHomeDeck(Deck homeDeck) {
        this.homeDeck = homeDeck;
    }

    public Deck getAwayDeck() {
        return awayDeck;
    }

    public void setAwayDeck(Deck awayDeck) {
        this.awayDeck = awayDeck;
    }

    public String getHomePlayerTag() {
        return homePlayerTag;
    }

    public void setHomePlayerTag(String homePlayerTag) {
        this.homePlayerTag = homePlayerTag;
    }

    public String getAwayPlayerTag() {
        return awayPlayerTag;
    }

    public void setAwayPlayerTag(String awayPlayerTag) {
        this.awayPlayerTag = awayPlayerTag;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public static void main(String[] args) {

    }
}
