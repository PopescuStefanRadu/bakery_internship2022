package ro.esolutions.bakery;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Random;

@Profile("daily-message")

// see request/prototype
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@RestController
@AllArgsConstructor
public class DailyMessageController {
    // aici traiesc beanurile noastre
    private ApplicationContext applicationContext;
    private Map<String, String> messagesByBeanName;
    private List<String> messages;
    @Qualifier("msg1")
    private String message1;
    @Qualifier("dailyMessage2")
    private String message2;
    @Value("#{config.enableDailyMessagesEndpoint}")
    private Boolean enableDailyMessagesEndpoint;

    @GetMapping(value = "/daily-message", produces = "application/json")
    public String dailyMessage() {
        var msg = new Random().nextBoolean() ? message1 : message2;

        return "\"" + msg + "\"";
    }

    @GetMapping(value = "/daily-messages")
    public List<String> getDailyMessages() {
        if (enableDailyMessagesEndpoint) {
            return messages;
        }
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "feature disabled");
    }

    @GetMapping(value = "/daily-messages-by-bean-name")
    public Map<String, String> getMessagesByBeanName() {
        return messagesByBeanName;
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("blah");
    }

    @PreDestroy
    public void preDestroy() {
        System.out.println("pre destroy");
    }
}
