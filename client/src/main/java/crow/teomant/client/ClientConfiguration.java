package crow.teomant.client;

import crow.teomant.domain.events.DomainEvents;
import crow.teomant.domain.events.SimpleSynchronousDomainEvents;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfiguration {

    @Bean
    public DomainEvents domainEvents() {
        return new SimpleSynchronousDomainEvents();
    }
}
