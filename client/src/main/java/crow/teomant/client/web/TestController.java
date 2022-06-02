package crow.teomant.client.web;

import crow.teomant.client.domain.history.ClientState;
import crow.teomant.client.domain.service.ClientService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/test")
public class TestController {

    private final ClientService clientService;

    @GetMapping
    public ClientState create() {
        return clientService.register("test", UUID.randomUUID(), "test");
    }

    @GetMapping("/{id}")
    public ClientState get(@PathVariable("id") UUID id) {
        return clientService.get(id);
    }

    @PostMapping("/{id}")
    public ClientState update(@PathVariable("id") UUID id) {
        return clientService.update(id, UUID.randomUUID(), "test2");
    }
}
