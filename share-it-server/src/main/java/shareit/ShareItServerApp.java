package shareit;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "ShareIt", version = "1.0", description = "ShareIt app swagger"))
public class ShareItServerApp {

    public static void main(String[] args) {
        SpringApplication.run(ShareItServerApp.class, args);
    }

}
