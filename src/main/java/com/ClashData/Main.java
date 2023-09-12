package com.ClashData;

import com.ClashData.Exceptions.AccesAPIException;
import com.ClashData.Model.Battle;
import com.ClashData.Model.Player;
import com.fasterxml.jackson.databind.JsonNode;


import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Properties;

import com.ClashData.Constants.*;

public class Main {

    public static void main(String[] args) {

        ArrayList<Player> players = new ArrayList<>();

        Player player = new Player();

        player.setTag("#2RC2C9Q2");

        players.add(player);

        addPlayersBattle(players);





    }


    /*
    Write every country from
    Clash Royale API to a local File
     */
    private static void getCountry() {

        String url = ClashRoyaleAPIURL.LOCATION_URL;

        ArrayList<String> countryIdsList = new ArrayList<>();

        try {
            JsonNode responseNode = AccessAPI.executeGetRequest(url);

            JsonNode itemNode = responseNode.get("items");

            for(JsonNode node : itemNode)
            {
                if(node.get("isCountry").asBoolean())
                {
                    countryIdsList.add(node.get("id").asText());
                }
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FileName.COUNTRY_FILE, true))) {

                for(String id : countryIdsList)
                {
                    writer.write(id);
                    writer.newLine();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        catch(AccesAPIException e)
        {
            System.out.println(e.getMessage());
        }
    }

    private static void createCardsIDProperties()
    {
        String url = ClashRoyaleAPIURL.CARDS_URL;

        Properties prop = new Properties();

        try {
            JsonNode responseNode = AccessAPI.executeGetRequest(url);

            JsonNode itemNode = responseNode.get("items");

            int i = 1;

            for(JsonNode node : itemNode)
            {
                prop.setProperty(node.get("name").asText(), String.valueOf(i));
                i++;
            }

            try (OutputStream output = new FileOutputStream(FileName.CARDS_ID_PROPERTIES)) {
                // Sauvegardez les propriétés dans un fichier
                prop.store(output, "Fichier de configuration");
                System.out.println("Fichier de configuration créé avec succès.");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        catch(AccesAPIException e)
        {
            System.out.println(e.getMessage());
        }

    }


    /*
     Get the current top 1000 in
     Path of Legend Ranking
     */
    private static ArrayList<Player> getTop1000() {

        ArrayList<Player> top1000List = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FileName.COUNTRY_FILE))) {

            String line;
            int i = 0;
            while ((line = reader.readLine()) != null) {
                int id = Integer.parseInt(line.trim());

                String url = "https://api.clashroyale.com/v1/locations/" + id + "/pathoflegend/players";

                JsonNode responseNode = AccessAPI.executeGetRequest(url);

                JsonNode itemNode = responseNode.get("items");

                for (JsonNode node : itemNode) {

                    Player player = new Player();

                    player.setELO(Integer.parseInt(node.get("eloRating").asText()));
                    player.setTag(node.get("tag").asText());

                    top1000List.add(player);
                }


                top1000List.sort(Comparator.comparingInt(Player::getELO).reversed());

                if(top1000List.size() > 1000)
                {
                    top1000List.subList(1001, top1000List.size()).clear();
                }

                System.out.println(i);
                i++;

            }
        } catch (IOException | AccesAPIException e) {
            System.out.println(e.getMessage());
        }

        return top1000List;

    }


    private static void addPlayersBattle(ArrayList<Player> players) {


        for(Player player : players)
        {
            try {

                String url = "https://api.clashroyale.com/v1/players/" + player.getTagForCRAPI() + "/battlelog";

                JsonNode responseNode = AccessAPI.executeGetRequest(url);

                for(JsonNode node : responseNode)
                {
                    if(!node.get("type").asText().equals("pathOfLegend"))
                        continue;

                    Battle battle = new Battle(node);

                    System.out.println(battle.toString() + "\n\n\n");

                    //add battlesdeck to DB


                }
            }
            catch (AccesAPIException e)
            {
                System.out.println(e.getMessage());
            }



        }

    }


}