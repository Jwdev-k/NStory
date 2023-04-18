package nk.service.NStory.utils;

import lombok.RequiredArgsConstructor;
import nk.service.NStory.dto.AccountDTO;
import nk.service.NStory.dto.ExpTable;
import nk.service.NStory.repository.AccountMapper;
import nk.service.NStory.repository.ExpTableMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class UpdateStatus {
    private final ExpTableMapper expTableMapper;
    private final AccountMapper accountMapper;

    @Transactional
    public void addExp(int exp, String email, int currentLevel) throws Exception {
        accountMapper.UpdateExp(exp,email); // 경험치 추가
        ExpTable newLevel = expTableMapper.getExpRange(email);
        if (currentLevel != newLevel.getLevel()) {
            accountMapper.UpdateLevel(newLevel.getLevel(), email);
            accountMapper.UpdateCoin(100, email);
        }
    }

    @Transactional
    public void addNCoin(int count, String email) throws Exception {
        accountMapper.UpdateCoin(count, email);
    }

    @Transactional
    public boolean checkingReward(AccountDTO account) throws Exception {
        boolean firstLogin = false;
            if (account.getLastDateTime() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate lastDate = LocalDate.parse(account.getLastDateTime().substring(0, 10), formatter);
                if (LocalDate.now().isAfter(lastDate)) {
                    firstLogin = true;
                    addExp(100, account.getEmail(), account.getLevel());
                }
            }
            return firstLogin;
    }
}
