package com.medwell.ambulance.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class GmapsService {

    @Autowired
    private  RestTemplate restTemplate;

    private final String GMAPS_SERVICE_URL="https://gmapsmedwell.vercel.app";

    public String getPolyline(double lat1, double lon1, double lat2, double lon2) {
        String url = String.format(
                "%s/get-encoded-polyline/%f/%f/%f/%f",
                GMAPS_SERVICE_URL,lat1, lon1, lat2, lon2
        );

        // Make the GET request and parse the response into a Map
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        Map<String, String> responseBody = response.getBody();

        // Extract polyline key from the response
        if (response.getStatusCode()== HttpStatusCode.valueOf(400)) {
            return responseBody.get("polyline");
        } else {
            return "No polyline found";
        }
    }



}
