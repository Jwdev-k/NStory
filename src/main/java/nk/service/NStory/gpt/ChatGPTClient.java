package nk.service.NStory.gpt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ChatGPTClient {
    private static final String API_KEY = "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";
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    public static String AIChat(String message) {
        Map<String, Object> json = new HashMap<>();
        json.put("model", "gpt-3.5-turbo");
        json.put("messages", List.of(Map.of("role", "system", "content", message)));

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + API_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(json, headers);
        ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.POST, entity, String.class);
        log.info(response.getBody());
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            return null;
        }
    }
}
