package ru.yandex.masterskaya.client;

import feign.Response;
import feign.codec.ErrorDecoder;
import ru.yandex.masterskaya.exception.ConflictException;
import ru.yandex.masterskaya.exception.EntityNotFoundException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ClientErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        String answer = "";
        if (response.body() != null) {
            try (Response.Body body = response.body()) {
                byte[] resp = body.asInputStream().readAllBytes();
                answer = new String(resp, StandardCharsets.UTF_8);
            } catch (IOException e) {
                e.getStackTrace();
            }
        }

        switch (response.status()) {
            case 404 -> throw new EntityNotFoundException(answer.isBlank() ? "Не найдено!" : answer);
            case 400 -> throw new ConflictException(answer.isBlank() ? "Некорректные данные" : answer);
            default -> throw new IllegalStateException("Unexpected value: " + response.status());
        }
    }
}
