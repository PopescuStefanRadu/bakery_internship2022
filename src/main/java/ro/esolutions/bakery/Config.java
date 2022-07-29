package ro.esolutions.bakery;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
@ConfigurationProperties(prefix = "ro.esolutions.bakery")
@Getter
@Setter
@NoArgsConstructor
@Validated
public class Config {

    @NotNull
    private Boolean enableDailyMessagesEndpoint;


    @Bean(value = "msg1")
    public String dailyMessage() {
        return "Tinem legatura!";
    }
    @Bean
    public String dailyMessage2(@Qualifier("msg1") String asd) {
        return "Cum se face legatura!" + asd;
    }
}
