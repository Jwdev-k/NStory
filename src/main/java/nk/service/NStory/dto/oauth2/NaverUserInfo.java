package nk.service.NStory.dto.oauth2;

import lombok.ToString;

import java.util.Map;

@ToString
public class NaverUserInfo implements OAuth2UserInfo{
    private final Map<String, Object> attributes; //OAuth2User.getAttributes();

    public NaverUserInfo(Map<String, Object> attributes) {
        this.attributes = (Map<String, Object>) attributes.get("response");
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getEmail() {
        return attributes.get("email").toString();
    }

    @Override
    public String getName() {
        return attributes.get("nickname").toString();
    }
}