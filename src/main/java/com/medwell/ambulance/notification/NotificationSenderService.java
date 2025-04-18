package com.medwell.ambulance.notification;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.medwell.ambulance.entity.CustomUser;
import com.medwell.ambulance.enums.Status;
import com.medwell.ambulance.repository.CustomUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class NotificationSenderService {

    @Autowired
    private CustomUserRepository customUserRepository;

    @Autowired
    private FcmService fcmService;


    @Async
    public void sendStatusUpdateNotification(String customerId, Status status) throws FirebaseMessagingException {
        CustomUser customer=customUserRepository.getReferenceById(customerId);
        String token=customer.getToken();
        String customerName=customer.getName();
        String title="Booking Update";
        String message=switch (status) {
            case ASSIGNED -> String.format("Dear %s, your ambulance has been successfully assigned. The ambulance and team  will be with you shortly.", customerName);
            case ARRIVED -> String.format("Dear %s, the ambulance has arrived at your location. The medical team will assist you promptly and ensure a safe transfer.", customerName);
            case COMPLETED -> String.format("Dear %s, your ambulance service has been successfully completed. We wish you a smooth and speedy recovery.", customerName);
            default -> "";
        };

        fcmService.sendStatusUpdate(NotificationMessage.builder().message(message).title(title).token(token).build());

    }

}
