package com.medwell.ambulance.utils;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.util.Properties;

@Getter
@Component
public class SecretLoader {


    private final int redisPort;
    private final String redisHost,redisPassword;


    public SecretLoader() throws  Exception{
        Properties props=new Properties();
        // Load secrets.properties from the classpath
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("secrets.properties");
        if (inputStream == null) {
            throw new FileNotFoundException("Property file 'secrets.properties' not found in the classpath");
        }

        props.load(inputStream);
        this.redisHost=props.getProperty("redisHost");
        this.redisPassword=props.getProperty("redisPassword");
        this.redisPort=Integer.parseInt(props.getProperty("redisPort"));

    }


}
