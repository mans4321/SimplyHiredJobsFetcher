package simplyhired;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * This defines and configure our spring application
 */
@SpringBootApplication
@EnableAsync
public class App {


    public static void main (String [] args) {
        SpringApplication.run(App.class, args);
    }
}
