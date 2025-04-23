package web;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication // Spring Boot 핸들러를 위한 Annotation
@EnableJpaAuditing // JPA Auditing을 위한 Annotation
@EnableScheduling // 스케줄링을 위한 Annotation
public class AppStart {
    public static void main(String[] args) {
        SpringApplication.run(AppStart.class, args);
    } // end main
} // end AppStart
