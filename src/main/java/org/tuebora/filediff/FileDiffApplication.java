package org.tuebora.filediff;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.tuebora.filediff.domain.service.FileDiffProcessingService;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class FileDiffApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileDiffApplication.class, args);
    }

    @Bean
    public CommandLineRunner run(FileDiffProcessingService fileDiffProcessingService) {
        return args -> fileDiffProcessingService.process();
    }
}
