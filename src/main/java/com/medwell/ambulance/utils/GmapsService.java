package com.medwell.ambulance.utils;

import com.medwell.ambulance.dto.PolylineDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


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

        PolylineDTO polylineDTO = restTemplate.getForObject(url, PolylineDTO.class);
        assert polylineDTO != null;
        return polylineDTO.getPolyline()!=null ? polylineDTO.getPolyline() : null;
    }


}
