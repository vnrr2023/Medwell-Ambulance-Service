package com.medwell.ambulance.notification;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import org.aspectj.bridge.IMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class FcmService {



    public void sendStatusUpdate(NotificationMessage notificationMessage) throws FirebaseMessagingException {
        Message message = Message.builder()
                .setToken(notificationMessage.getToken())
                .putData("title", notificationMessage.getTitle())
                .putData("body", notificationMessage.getMessage())
                .putData("image", notificationMessage.getImageUrl())
                .build();

        FirebaseMessaging.getInstance().send(message);
    }



}
