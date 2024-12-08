package ru.yandex.masterskaya;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MasterskayaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MasterskayaApplication.class, args);
    }

}
