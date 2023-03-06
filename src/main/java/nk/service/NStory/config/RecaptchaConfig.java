package nk.service.NStory.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import nk.service.NStory.utils.AES256;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;

@Configuration @Slf4j
public class RecaptchaConfig {
    public static final String url = "https://www.google.com/recaptcha/api/siteverify";
    private static final String secretKey = "u9aoKY/hirdTUeurtGvWzNecQbeTDX9TUxzmBEG+/F3b+aKv9rS9w6TSIzCLj0Vp";

    public static boolean verify(String token) throws Exception {
        if (token == null || token.equals("")) {
            return false;
        }
        RestTemplate restTemplate = new RestTemplate();

        // Parameters
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("secret", AES256.decrypt(secretKey));
        parameters.add("response", token);

        // Request
        HttpEntity<String> response = restTemplate.postForEntity(url, parameters, String.class);

        // Response 파싱
        ObjectMapper objectMapper = new ObjectMapper();
        LinkedHashMap<String, String> JsonString = objectMapper.readValue(response.getBody(), LinkedHashMap.class);
        return Boolean.parseBoolean(String.valueOf(JsonString.get("success")));
    }
}
