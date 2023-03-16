package nk.service.NStory.utils;

import lombok.RequiredArgsConstructor;
import nk.service.NStory.dto.ExpTable;
import nk.service.NStory.repository.AccountMapper;
import nk.service.NStory.repository.ExpTableMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
}
