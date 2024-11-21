package foderking.speculate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.net.http.HttpClient;

@SpringBootApplication
public class SpeculateApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpeculateApplication.class, args);
    }

    @Bean
    public HttpClient httpClient() {
        return HttpClient.newHttpClient();
    }
}
