package com.medwell.ambulance.utils;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.util.Properties;

@Getter
@Component
public class SecretLoader {


    private final int redisPort;
    private final String redisHost,redisPassword;


    public SecretLoader() throws  Exception{
        Properties props=new Properties();
        FileReader reader=new FileReader("src/main/resources/secrets.properties");
        props.load(reader);
        this.redisHost=props.getProperty("redisHost");
        this.redisPassword=props.getProperty("redisPassword");
        this.redisPort=Integer.parseInt(props.getProperty("redisPort"));

    }


}
