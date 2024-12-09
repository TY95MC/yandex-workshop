package ru.yandex.masterskaya.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.yandex.masterskaya.model.dto.client.EventDto;

@FeignClient(name = "event-service", url = "${event-service.url}", configuration = ClientConfiguration.class)
public interface EventClient {
    @RequestMapping(method = RequestMethod.GET, value = "/events/{id}", consumes = "application/json")
    ResponseEntity<EventDto> getEventById(@PathVariable Long id);
}
