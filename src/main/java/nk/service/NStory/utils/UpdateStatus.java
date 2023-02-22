package nk.service.NStory.utils;

import nk.service.NStory.dto.ExpTable;
import nk.service.NStory.repository.AccountMapper;
import nk.service.NStory.repository.ExpTableMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UpdateStatus {
    @Autowired
    private ExpTableMapper expTableMapper;
    @Autowired
    private AccountMapper accountMapper;

    @Transactional
    public void addExp(int exp, String email, int currentLevel) throws Exception {
        accountMapper.UpdateExp(exp,email); // 경험치 추가
        ExpTable newLevel = expTableMapper.getExpRange(email);
        if (currentLevel != newLevel.getLevel()) {
            accountMapper.UpdateLevel(newLevel.getLevel(), email);
        }
    }
}
