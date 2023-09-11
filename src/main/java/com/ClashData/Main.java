package com.ClashData;

import com.ClashData.Exceptions.AccesAPIException;
import com.ClashData.Model.Deck;
import com.ClashData.Model.Player;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.sound.sampled.FloatControl;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;

import com.ClashData.Constants.*;

public class Main {

    public static void main(String[] args) {


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


    /*
     Get the current top 1000 in
     Path of Legend Ranking
     */
    static ArrayList<Player> getTop1000() {

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


}