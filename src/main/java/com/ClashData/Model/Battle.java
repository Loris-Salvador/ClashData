package com.ClashData.Model;

import com.ClashData.Constants.FileName;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;

public class Battle {

    private String id;
    private String tagPlayer1;
    private Deck deckPlayer1;
    private String tagPlayer2;
    private Deck deckPlayer2;
    //isWin depend du deck1 si le joueur 1 win alors win = true
    private boolean isWin;
    private Date date;
    private Mode mode;
    private int top;




    public Battle(JsonNode node) {

        this.mode = Mode.PATH_OF_LEGEND;
        String battleTime = node.get("battleTime").asText();

        // Utilisez un SimpleDateFormat pour analyser la chaîne
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd'T'HHmmss.SSS'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            Date date = sdf.parse(battleTime);

            // Affichez la date résultante
            this.date = date;


        } catch (ParseException e) {
            e.printStackTrace();
        }



        JsonNode teamNode = node.get("team").get(0);
        this.tagPlayer1 = teamNode.get("tag").asText();

        JsonNode oppoNode = node.get("opponent").get(0);
        this.tagPlayer2 = oppoNode.get("tag").asText();

        if(teamNode.get("crowns").asInt() > oppoNode.get("crowns").asInt())
            this.isWin = true;
        else
            this.isWin = false;


        Properties prop = new Properties();

        try (InputStream input = new FileInputStream(FileName.CARDS_ID_PROPERTIES)) {
            // Chargez le fichier .properties
            prop.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //DECKS
        deckPlayer1 = new Deck();
        deckPlayer2 = new Deck();

        //TEAM
        JsonNode firstCardTeam = teamNode.get("cards").get(0);
        if(firstCardTeam.get("evolutionLevel") != null)
        {
            String evoCardName = firstCardTeam.get("name").asText();
            String id = (String) prop.get(evoCardName);
            deckPlayer1.setEvoCardID(Integer.parseInt(id));
        }


        JsonNode cardTeamNode = teamNode.get("cards");

        for(JsonNode n : cardTeamNode)
        {
            String cardName = n.get("name").asText();
            int id = Integer.parseInt(prop.getProperty(cardName));

            deckPlayer1.addCardsId(id);
        }


        //OPPONENT

        JsonNode firstCardOppo = oppoNode.get("cards").get(0);

        if(firstCardOppo.get("evolutionLevel") != null)
        {
            String evoCardName = firstCardOppo.get("name").asText();
            String id = (String) prop.get(evoCardName);

            deckPlayer2.setEvoCardID(Integer.parseInt(id));
        }

        JsonNode cardOppoNode = oppoNode.get("cards");

        for(JsonNode n : cardOppoNode)
        {
            String cardName = n.get("name").asText();
            int id = Integer.parseInt(prop.getProperty(cardName));

            deckPlayer2.addCardsId(id);
        }

        //////TOP/////////
        String topTeam = teamNode.get("globalRank").asText();
        String oppoTeam = oppoNode.get("globalRank").asText();

        int a, b ;

        if(topTeam.equals("null"))
            a = -1;
        else
            a = Integer.parseInt(topTeam);

        if(oppoTeam.equals("null"))
            b = -1;
        else
            b = Integer.parseInt(oppoTeam);


        if(a == -1 && b == -1)
            top = -1;
        else
        {
            if(a > b)
                this.top = a;
            else
                this.top = b;
        }



        /////////ID///////////

        if(tagPlayer1.compareTo(tagPlayer2) < 0)
        {
            id = tagPlayer1 + tagPlayer2 + date.toString();
        }
        else
        {
            id = tagPlayer2 + tagPlayer1 + date.toString();
        }

    }


    public Deck getDeckPlayer1() {
        return deckPlayer1;
    }

    public Deck getDeckPlayer2() {
        return deckPlayer2;
    }

    public String getId() {
        return id;
    }

    public String getTagPlayer1() {
        return tagPlayer1;
    }

    public String getTagPlayer2() {
        return tagPlayer2;
    }

    public boolean isWin() {
        return isWin;
    }

    public Date getDate() {
        return date;
    }

    public Mode getMode() {
        return mode;
    }

    public int getTop() {
        return top;
    }

    @Override
    public String toString() {
        return "Battle{" +
                "id='" + id + '\'' +
                ", deckPlayer2=" + deckPlayer2 +
                ", deckPlayer1=" + deckPlayer1 +
                ", tagPlayer1='" + tagPlayer1 + '\'' +
                ", tagPlayer2='" + tagPlayer2 + '\'' +
                ", isWin=" + isWin +
                ", date='" + date + '\'' +
                ", mode=" + mode +
                ", top=" + top +
                '}';
    }




    public static void main(String[] args) {

    }
}
