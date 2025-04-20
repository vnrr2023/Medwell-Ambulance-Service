package com.medwell.ambulance.utils;

import com.medwell.ambulance.dto.PolylineDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Slf4j
@Service
public class GmapsService {

    @Autowired
    private  RestTemplate restTemplate;

    private final String GMAPS_SERVICE_URL="https://gmapsmedwell.vercel.app";

    public String getPolyline(double lat1, double lon1, double lat2, double lon2) {
        try {
            String url = String.format(
                    "%s/get-encoded-polyline/%f/%f/%f/%f",
                    GMAPS_SERVICE_URL, lat1, lon1, lat2, lon2
            );

            PolylineDTO polylineDTO = restTemplate.getForObject(url, PolylineDTO.class);

            return polylineDTO.getPolyline() != null ? polylineDTO.getPolyline() : null;
        } catch (Exception e) {
            log.error("Failed to fetch polyline from Gmaps Service Server");
            return null;
        }
    }


}
