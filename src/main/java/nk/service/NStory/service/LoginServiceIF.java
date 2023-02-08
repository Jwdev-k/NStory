package nk.service.NStory.service;

import nk.service.NStory.dto.AccountDTO;

public interface LoginServiceIF {
    void register(AccountDTO accountDTO) throws Exception;
    boolean checkEmail(String email) throws Exception;
    void findPassword(String email) throws Exception;
}
