package com.ClashData;

import com.ClashData.Constants.FileName;
import com.ClashData.Exceptions.AccesAPIException;
import com.ClashData.Exceptions.TokenException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class AccessAPI {

    private static String getToken() throws TokenException {
        try (BufferedReader reader = new BufferedReader(new FileReader(FileName.TOKEN_FILE))) {
            String line;
            line = reader.readLine();

            return line;

        } catch (IOException ex) {
            throw new TokenException();
        }
    }

    static JsonNode executeGetRequest(String url) throws AccesAPIException {

        try {
            URIBuilder uriBuilder = new URIBuilder(url);

            URI uri = uriBuilder.build();

            HttpClient httpClient = HttpClients.createDefault();

            HttpGet httpGet = new HttpGet(uri);

            httpGet.setHeader("Authorization", "Bearer " + getToken());

            HttpResponse response = httpClient.execute(httpGet);

            int status = response.getStatusLine().getStatusCode();
            HttpEntity responseEntity = response.getEntity();
            String responseBody = EntityUtils.toString(responseEntity, ContentType.getOrDefault(responseEntity).getCharset());
            if(status != 200)
            {
                throw new AccesAPIException(responseBody);
            }

            ObjectMapper objectMapper = new ObjectMapper();

            return objectMapper.readTree(responseBody);
        }

        catch (URISyntaxException | IOException | TokenException | AccesAPIException e)
        {
            throw new AccesAPIException(e.getMessage());
        }


    }

    public static void main(String[] args) {

    }
}
