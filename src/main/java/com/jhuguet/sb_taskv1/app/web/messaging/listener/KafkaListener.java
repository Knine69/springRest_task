package com.jhuguet.sb_taskv1.app.web.messaging.listener;

import org.springframework.stereotype.Component;

@Component
public class KafkaListener {

    @org.springframework.kafka.annotation.KafkaListener(topics = "userMessenger", groupId = "userMessagesID", autoStartup = "${listen.auto.start:true}")
    public void listenGroupFoo(String message) {
        System.out.println("Received Message in group userMessagesID: " + message);
    }
}
