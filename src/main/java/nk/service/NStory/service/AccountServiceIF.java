package nk.service.NStory.service;

import nk.service.NStory.dto.AccountDTO;

public interface AccountServiceIF {
    void register(AccountDTO accountDTO) throws Exception;
    boolean checkEmail(String email) throws Exception;
    void UpdateLevel(int level) throws Exception;
    void UpdateExp(int exp) throws Exception;
    void UpdateCoin(int nCoin) throws Exception;
    void findPassword(String email) throws Exception;
}
