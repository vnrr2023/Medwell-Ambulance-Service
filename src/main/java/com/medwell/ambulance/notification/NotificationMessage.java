package com.medwell.ambulance.notification;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationMessage {

    private String message;
    private String token;
    private String title;
    private String imageUrl="https://github.com/vnrr2023/medwell-ambulance-app/blob/main/assets/images/adaptive-icon.png";

}
