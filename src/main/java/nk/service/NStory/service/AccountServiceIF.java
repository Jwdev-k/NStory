package nk.service.NStory.service;

import nk.service.NStory.dto.AccountDTO;
import org.springframework.security.core.Authentication;

public interface AccountServiceIF {
    void register(AccountDTO accountDTO) throws Exception;
    boolean checkEmail(String email) throws Exception;
    void UpdateLastLoginDate(String lastLoginDate, String email) throws Exception;
    void UpdateAccountInfo(AccountDTO accountDTO, Authentication authentication) throws Exception;
    void findPassword(String email) throws Exception;
}
