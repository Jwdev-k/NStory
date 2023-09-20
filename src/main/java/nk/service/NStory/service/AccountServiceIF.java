package nk.service.NStory.service;

import nk.service.NStory.dto.AccountDTO;
import nk.service.NStory.dto.ByteImageDTO;

public interface AccountServiceIF {
    void register(AccountDTO accountDTO) throws Exception;
    AccountDTO login(String email) throws Exception;
    boolean checkEmail(String email) throws Exception;
    boolean checkEmail2(String email) throws Exception;
    void UpdateLastLoginDate(String lastLoginDate, String email) throws Exception;
    void UpdateAccountInfo(AccountDTO accountDTO) throws Exception;
    void resetPassword(String email, String password) throws Exception;
    ByteImageDTO getUserImage(int id) throws Exception;
    void deleteAccount(int aid) throws Exception;
}
