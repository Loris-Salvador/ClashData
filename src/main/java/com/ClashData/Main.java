package com.ClashData;

import com.ClashData.Exceptions.AccesAPIException;
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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


public class Main {
    public static void main(String[] args) {

    }


    /*
    Write every country from
    Clash Royale API to a local File
     */
    private void getCountry() {

        String url = "https://api.clashroyale.com/v1/locations";

        try {
            String response = AccessAPI.executeGetRequest(url);
        }
        catch(AccesAPIException e)
        {
            System.out.println(e.getMessage());
        }





    }


}