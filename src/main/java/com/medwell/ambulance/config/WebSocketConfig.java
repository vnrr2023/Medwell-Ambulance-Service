package com.medwell.ambulance.config;


import com.medwell.ambulance.websockets.AmbulanceWebSocketHandler;
import com.medwell.ambulance.websockets.BookingAmbulanceWebSocketHandler;
import com.medwell.ambulance.websockets.NearbyAmbulanceWebsocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class  WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private AmbulanceWebSocketHandler ambulanceWebSocketHandler;

    @Autowired
    private NearbyAmbulanceWebsocketHandler nearbyAmbulanceWebsocketHandler;

    @Autowired
    private BookingAmbulanceWebSocketHandler bookingAmbulanceWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

        registry.addHandler(ambulanceWebSocketHandler, "/ambulance-location").setAllowedOrigins("*");
        registry.addHandler(nearbyAmbulanceWebsocketHandler, "/nearby-ambulance-locations").setAllowedOrigins("*");
        registry.addHandler(bookingAmbulanceWebSocketHandler, "/send-real-time-location").setAllowedOrigins("*");


    }
}