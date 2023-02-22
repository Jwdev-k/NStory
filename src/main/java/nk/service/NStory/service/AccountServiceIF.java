package nk.service.NStory.service;

import nk.service.NStory.dto.AccountDTO;

public interface AccountServiceIF {
    void register(AccountDTO accountDTO) throws Exception;
    boolean checkEmail(String email) throws Exception;
    void UpdateLastLoginDate(String lastLoginDate, String email) throws Exception;
    void findPassword(String email) throws Exception;
}