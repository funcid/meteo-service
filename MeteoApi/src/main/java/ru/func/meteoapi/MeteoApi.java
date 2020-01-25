package ru.func.meteoapi;

import com.google.gson.Gson;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * @author func 23.01.2020
 */
public final class MeteoApi {

    private static final String WEB_ADDRESS = "https://func-weather.herokuapp.com/";

    private static final Gson GSON = new Gson();
    private static final CloseableHttpClient HTTP_CLIENT = HttpClients.createDefault();

    public static Notation[] getLastNotations() throws IOException {
        return GSON.fromJson(
                getResponse(WEB_ADDRESS + Request.REQUEST_NEW_DATA),
                Notation[].class
        );
    }

    public static Notation getNotationById(int id) throws IOException {
        return GSON.fromJson(
                getResponse(WEB_ADDRESS + String.format(Request.REQUEST_BY_ID.toString(), id)),
                Notation.class
        );
    }

    public static Notation[] getNotationLocation(String location) throws IOException {
        return GSON.fromJson(
                getResponse(WEB_ADDRESS + String.format(Request.REQUEST_BY_LOCATION.toString(), location)),
                Notation[].class
        );
    }

    public static Notation[] getNotationTimestamp(String timestamp) throws IOException {
        return GSON.fromJson(
                getResponse(WEB_ADDRESS + String.format(Request.REQUEST_BY_TIMESTAMP.toString(), timestamp)),
                Notation[].class
        );
    }

    private static String getResponse(String request) throws IOException {
        HttpGet httpRequest = new HttpGet(request);

        CloseableHttpResponse response = HTTP_CLIENT.execute(httpRequest);
        httpRequest.addHeader("Accept", "application/json");

        return EntityUtils.toString(response.getEntity());
    }
}
