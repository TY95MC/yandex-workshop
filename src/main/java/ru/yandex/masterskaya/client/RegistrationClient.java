package ru.yandex.masterskaya.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.yandex.masterskaya.model.dto.client.RegistrationDto;

@FeignClient(name = "registration-service", url = "${registration-service.url}", configuration = ClientConfiguration.class)
public interface RegistrationClient {
    @RequestMapping(method = RequestMethod.GET, value = "/{eventId}/status/{userId}", consumes = "application/json")
    RegistrationDto getStatusByEventIdAndUserId(@PathVariable Long eventId, @PathVariable Long userId);
}
