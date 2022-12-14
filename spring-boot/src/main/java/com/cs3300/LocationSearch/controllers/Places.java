package com.cs3300.LocationSearch.controllers;

import okhttp3.CacheControl;
import org.springframework.web.bind.annotation.*;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;
import java.util.Arrays;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@CrossOrigin
@RestController
public class Places {
    private class ResultsAndNextPage {
        JSONArray jsonArray;
        String nextPageToken;

        ResultsAndNextPage(JSONArray jsonArray, String nextPageToken) {
            this.jsonArray = jsonArray;
            this.nextPageToken = nextPageToken;
        }
    }

    public List<String> UNSUPPORTED_TYPES = Arrays.asList(
            "administrative_area_level_1",
            "administrative_area_level_2",
            "administrative_area_level_3",
            "administrative_area_level_4",
            "administrative_area_level_5",
            "administrative_area_level_6",
            "administrative_area_level_7",
            "archipelago",
            "colloquial_area",
            "continent",
            "country",
            "geocode",
            "intersection",
            "locality",
            "neighborhood",
            "plus_code",
            "political",
            "post_box",
            "postal_code",
            "postal_code_prefix",
            "postal_code_suffix",
            "postal_town",
            "route",
            "street_address",
            "street_number",
            "sublocality",
            "sublocality_level_1",
            "sublocality_level_2",
            "sublocality_level_3",
            "sublocality_level_4",
            "sublocality_level_5",
            "subpremise"
    );

    private ResultsAndNextPage getPlacesUtil(double lat, double lng, double rad, String page_token) {
        String key = "AIzaSyBxLjV86B7Y7lh-NB1dwk_JWy0mYz4MsQM";

        OkHttpClient client = new OkHttpClient();
        String api_url = String.format("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=%f,%f&radius=%f&key=%s", lat, lng, rad, key);
        if (page_token.length() > 0) {
            api_url = String.format("https://maps.googleapis.com/maps/api/place/nearbysearch/json?pagetoken=%s&key=%s", page_token, key);
        }
        System.out.println("url " + api_url);
        Request request = new Request.Builder()
                .cacheControl(new CacheControl.Builder().noCache().build())
                .url(api_url)
                .build();

        Response response = null;
        String data = "";
        try {
            for (int j = 0; j < 5; j++) {
                response = client.newCall(request).execute();
                data = response.body().string();
                response.close();

                if (new JSONObject(data).getJSONArray("results").length() > 0) {
                    System.out.println("We made it!");
                    break;
                } else {
                    try {
                        TimeUnit.MILLISECONDS.sleep(300);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        JSONObject json_data = new JSONObject(data);

        JSONArray results = json_data.getJSONArray("results");
        JSONArray places = new JSONArray();
        for (int i = 0; i < results.length(); i++) {
            JSONObject result = results.getJSONObject(i);
            try {
                JSONArray types = result.getJSONArray("types");
                int count = 0;
                for (int j = 0; j < types.length(); j++) {
                    if (UNSUPPORTED_TYPES.contains(types.getString(j))) {
                        count++;
                    }
                }
                if (count < types.length()) {
                    places.put(result.getString("place_id"));
                }
            } catch (JSONException e) {
                System.out.println("No types");
            }
        }

        String next_page_token = "";
        try {
            next_page_token = json_data.getString("next_page_token");
            System.out.println(next_page_token);
        } catch (JSONException e) {}

        return new ResultsAndNextPage(places, next_page_token);
    }

    @CrossOrigin
    @RequestMapping(value="/api/places", method = RequestMethod.GET, produces="application/json")
    public String getPlaces(@RequestParam(value="lat") String lat, @RequestParam(value="lng") String lng,  @RequestParam(value="rad") String rad){
        ResultsAndNextPage resultsAndNextPage = getPlacesUtil(Double.parseDouble(lat), Double.parseDouble(lng), Double.parseDouble(rad) * 1609, "");
        JSONArray places = resultsAndNextPage.jsonArray;
        String next_page_token = resultsAndNextPage.nextPageToken;
        for (int i = 0; i < 3 && next_page_token.length() > 0; i++) {
            try {
                TimeUnit.MILLISECONDS.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            resultsAndNextPage = getPlacesUtil(Double.parseDouble(lat), Double.parseDouble(lng), Double.parseDouble(rad) * 1609, next_page_token);

            JSONArray more_places = resultsAndNextPage.jsonArray;
            for (int j = 0; j < more_places.length(); j++) {
                places.put(more_places.getString(j));
            }

            next_page_token = resultsAndNextPage.nextPageToken;
        }

        return places.toString();
    }
}
