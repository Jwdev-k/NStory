package nk.service.NStory.service.leave;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nk.service.NStory.security.CustomUserDetails;
import nk.service.NStory.service.impl.AccountService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service @Slf4j
@RequiredArgsConstructor
public class leaveService {
    private final AccountService accountService;
    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String naverClientId;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String naverClientSecret;

    public void deleteGoogleAccount(CustomUserDetails userDetails, String accessToken) throws Exception {
        String data = "token=" + accessToken;
        if(sendRevokeRequest(userDetails.getProviderName(), data) == HttpStatus.OK) {
            accountService.deleteAccount(userDetails.getAid());
        }
    }

    public void deleteNaverAccount(CustomUserDetails userDetails, String accessToken) throws Exception {
        String data = "client_id=" + naverClientId +
                "&client_secret=" + naverClientSecret +
                "&access_token=" + accessToken +
                "&service_provider=NAVER" +
                "&grant_type=delete";
        if(sendRevokeRequest(userDetails.getProviderName(), data) == HttpStatus.OK) {
            accountService.deleteAccount(userDetails.getAid());
        }
    }

    public void deleteKakaoAccount(CustomUserDetails userDetails, String accessToken) throws Exception {
        if(sendRevokeRequest(userDetails.getProviderName(), accessToken) == HttpStatus.OK) {
            accountService.deleteAccount(userDetails.getAid());
        }
    }

    private HttpStatus sendRevokeRequest(String providerName, String data) throws IllegalStateException {
        String googleRevokeUrl = "https://accounts.google.com/o/oauth2/revoke";
        String naverRevokeUrl = "https://nid.naver.com/oauth2.0/token";
        String kakaoRevokeUrl = "https://kapi.kakao.com/v1/user/unlink";

        RestTemplate restTemplate = new RestTemplate();
        String revokeUrl = "";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> entity = new HttpEntity<>(data, headers);

        switch (providerName) {
            case "google" -> revokeUrl = googleRevokeUrl;
            case "naver" -> revokeUrl = naverRevokeUrl;
            case "kakao" -> {
                entity = new HttpEntity<>(null, headers);
                headers.setBearerAuth(data);
                revokeUrl = kakaoRevokeUrl;
            }
            default -> throw new IllegalStateException("Unexpected value: " + providerName);
        }
        ResponseEntity<String> responseEntity = restTemplate.exchange(revokeUrl, HttpMethod.POST, entity, String.class);
        HttpStatus statusCode = (HttpStatus) responseEntity.getStatusCode();
        String responseBody = responseEntity.getBody();
        log.info(providerName + "회원탈퇴 request 결과 : " + responseBody + "\nStatus Code : " + statusCode);
        return statusCode;
    }
}
