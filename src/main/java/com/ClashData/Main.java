package com.ClashData;

import com.ClashData.Exceptions.AccesAPIException;
import com.ClashData.Model.Battle;
import com.ClashData.Model.Deck;
import com.ClashData.Model.Player;
import com.fasterxml.jackson.databind.JsonNode;


import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Properties;
import java.util.Set;

import com.ClashData.Constants.*;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        ArrayList<Player> players = getTop1000();

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


        int i = 0;
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

                    //System.out.println(battle.toString() + "\n\n\n");

                    //add battlesdeck to DB
                    addDeckToDB(battle.getDeckPlayer1());
                    addDeckToDB(battle.getDeckPlayer2());

                    addBattleToDB(battle);



                }
            }
            catch (AccesAPIException e)
            {
                System.out.println(e.getMessage());
            }

            System.out.println("\n\nBattles of player " + i + " added to DataBase\n\n\n");

            i++;


        }

    }

    private static void addCardsToDB()
    {
        Properties prop = new Properties();

        try (InputStream input = new FileInputStream(FileName.CARDS_ID_PROPERTIES)) {
            // Chargez le fichier .properties
            prop.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }


        String jdbcUrl = "jdbc:oracle:thin:@//192.168.203.140:1521/orcl"; // Remplacez avec votre URL de connexion Oracle
        String username = "ClashStats";
        String password = "ClashStats";


        try {
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

            String query = "INSERT INTO CARD (CARD_ID, CARD_NAME) VALUES (?, ?)";

            Set<String> keys = prop.stringPropertyNames();

            // Parcourez les clés et récupérez les valeurs correspondantes
            for (String key : keys) {
                String value = prop.getProperty(key);

                System.out.println("Key : " + key);
                System.out.println("Value : " + value);


                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, Integer.parseInt(value));
                preparedStatement.setString(2, key);

                preparedStatement.executeUpdate();

                preparedStatement.close();
            }

            connection.close();


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }


    private static void addDeckToDB(Deck deck)
    {
        String jdbcUrl = "jdbc:oracle:thin:@//192.168.203.140:1521/orcl"; // Remplacez avec votre URL de connexion Oracle
        String username = "ClashStats";
        String password = "ClashStats";


        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            connection.setAutoCommit(false); // Désactive le mode "auto-commit"




            String query = "SELECT * FROM DECK WHERE DECK_ID LIKE '" + deck.getId() + "'";

            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {

                System.out.println("Deck already exists");
                resultSet.close();
                statement.close();
                return;
            }

            resultSet.close();
            statement.close();

            query = "INSERT INTO DECK (DECK_ID, CARTE_1, CARTE_2, CARTE_3, CARTE_4, CARTE_5, CARTE_6, CARTE_7, CARTE_8, CARD_EVO_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, deck.getId());
                for (int i = 2; i < 10; i++) {
                    preparedStatement.setInt(i, deck.getCardsId().get(i - 2));
                }
                preparedStatement.setInt(10, deck.getEvoCardID());

                preparedStatement.executeUpdate();

                connection.commit(); // Valide la transaction
            } catch (SQLException e) {
                e.printStackTrace();
                connection.rollback(); // Annule la transaction en cas d'erreur
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }



    }


    private static void addBattleToDB(Battle battle) {
        String jdbcUrl = "jdbc:oracle:thin:@//192.168.203.140:1521/orcl";
        String username = "ClashStats";
        String password = "ClashStats";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            connection.setAutoCommit(false); // Désactive le mode "auto-commit"

            String query = "SELECT * FROM BATTLE WHERE BATTLE_ID LIKE '" + battle.getId() + "'";

            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {

                System.out.println("Battle already exists");
                resultSet.close();
                statement.close();
                return;
            }

            resultSet.close();
            statement.close();


            query = "INSERT INTO BATTLE (BATTLE_ID, TAG_PLAYER_1, DECK_ID_PLAYER_1, TAG_PLAYER_2, DECK_ID_PLAYER_2, IS_WIN, BATTLE_TIME, BATTLE_MODE, TOP) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, battle.getId());
                preparedStatement.setString(2, battle.getTagPlayer1());
                preparedStatement.setString(3, battle.getDeckPlayer1().getId());
                preparedStatement.setString(4, battle.getTagPlayer2());
                preparedStatement.setString(5, battle.getDeckPlayer2().getId());

                if (battle.isWin()) {
                    preparedStatement.setInt(6, 1);
                } else {
                    preparedStatement.setInt(6, 0);
                }

                java.sql.Date dateSql = new java.sql.Date(battle.getDate().getTime());
                preparedStatement.setDate(7, dateSql);

                // for now pathOfLegend
                preparedStatement.setString(8, "pathOfLegend");

                preparedStatement.setInt(9, battle.getTop());

                preparedStatement.executeUpdate();
                connection.commit(); // Valide la transaction
            } catch (SQLException e) {
                e.printStackTrace();
                connection.rollback(); // Annule la transaction en cas d'erreur
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}