# Real Time Ambulance Booking Service Using Spring Boot + Confluent Kafka

## About The Project
Medwell Ambulance is a real time ambulance booking system. Users can seamlessly book ambulances on a single click. They can view the nearby ambulances at a specified radius. They can track the ambulances in real time similar to Uber and Zomato.



### Core Functionalities
1. Real time view of moving ambulances.
2. Seamless booking with a single click based on type.
3. Efficient and Faster ambulance matching service based on distance.
4. Notification Updated using firebase notification service.
5. Real time tracking of ambulance once successfull booking.
6. Polyline creation and embedding using gmaps.
7. Drop Off location setup.
8. Pessimistic locking mechanism at the Database level.

### Why is the System better ?
* Built using Spring Boot.
* Use of Confluent Kafka (Cloud Based) for real time tracking and location handling.
* Redis for Fast data storage.
* Scalable for around 10k users per minute.
* Use of websockets for location sending and getting real time updates.
* Higher Fault tolerance.
* Efficient and Modular.
* Higher Readability and Error handling.
* Better Response Time.



## Technology Stack
[![My Skills](https://skillicons.dev/icons?i=java,spring,kafka,postgres,firebase,aws,redis,supabase)](https://skillicons.dev)



## System Design

![system_design](https://github.com/vnrr2023/Medwell-Ambulance-Service/blob/main/ambulance.gif?raw=true)

## ER-Diagram

![system_design](https://github.com/vnrr2023/Medwell-Ambulance-Service/blob/main/ambualance-er.png?raw=true)

## Run Locally (Create the Below two files in the resources folder)
```
# application.properties

spring.application.name=ambulance

spring.datasource.url=jdbc:postgresql://<Your Credentials>

spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect


firebaseServiceJson="<your-firebase-service-account.json>"


# Kafka bootstrap servers (Confluent Cloud)
spring.kafka.bootstrap-servers=

# SASL authentication for Confluent Cloud
spring.kafka.properties.security.protocol=
spring.kafka.properties.sasl.mechanism=
spring.kafka.properties.sasl.jaas.config=

# Producer config
spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.apache.kafka.common.serialization.StringSerializer

# Consumer config
spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.consumer.value-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.consumer.group-id=my-spring-group
```

```
# secrets.properties
redisPassword=<Your-Redis-Cloud_password>
redisHost=<The Cloud Host Name>
redisPort=<Cloud Port>
```

## 🔗 Demo

Click the youtube icon to see the demo.
[![youtube](https://img.shields.io/badge/youtube-FF0000?style=for-the-badge&logo=youtube&logoColor=white)](https://youtu.be/qRZDOtLxSw4)



## Authors

- [Rehan Sayyed](https://www.linkedin.com/in/rehan42/)
- [Rohit Deshmukh](https://www.linkedin.com/in/rohitddev/)
- [Nishikant Raut](https://www.linkedin.com/in/nishidev/)
- [Vivek Chouhan](https://www.linkedin.com/in/vivek-chouhan/)



    


