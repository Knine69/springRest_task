package com.jhuguet.sb_taskv1;

import com.jhuguet.sb_taskv1.app.BootUpInfo;
import com.jhuguet.sb_taskv1.app.repositories.GiftCertificateRepository;
import com.jhuguet.sb_taskv1.app.repositories.OrderRepository;
import com.jhuguet.sb_taskv1.app.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SbTaskv1Application {

    public static void main(String[] args) {
        SpringApplication.run(SbTaskv1Application.class, args);
    }

    @Bean
    public CommandLineRunner demoData(UserRepository userRepo, GiftCertificateRepository certificateRepository, OrderRepository orderRepository) {
        BootUpInfo bootUpInfo = new BootUpInfo(userRepo, certificateRepository, orderRepository);
        return args -> {
            bootUpInfo.prepareInfo();
        };
    }
}
