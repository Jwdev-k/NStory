package nk.service.NStory.service;

import nk.service.NStory.dto.AccountDTO;
import org.springframework.security.core.Authentication;

public interface AccountServiceIF {
    void register(AccountDTO accountDTO) throws Exception;
    AccountDTO login(String email) throws Exception;
    boolean checkEmail(String email) throws Exception;
    void UpdateLastLoginDate(String lastLoginDate, String email) throws Exception;
    void UpdateAccountInfo(AccountDTO accountDTO, Authentication authentication) throws Exception;
    void resetPassword(String email, String password) throws Exception;
}
