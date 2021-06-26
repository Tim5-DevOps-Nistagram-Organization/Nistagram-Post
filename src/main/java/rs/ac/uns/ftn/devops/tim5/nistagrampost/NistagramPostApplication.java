package rs.ac.uns.ftn.devops.tim5.nistagrampost;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@EnableAsync
public class NistagramPostApplication {

    public static void main(String[] args) {
        SpringApplication.run(NistagramPostApplication.class, args);
    }

}
