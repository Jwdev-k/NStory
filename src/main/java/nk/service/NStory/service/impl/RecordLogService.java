package nk.service.NStory.service.impl;

import lombok.RequiredArgsConstructor;
import nk.service.NStory.dto.RecordLogDTO;
import nk.service.NStory.repository.RecordLogMapper;
import nk.service.NStory.service.RecordLogIF;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class RecordLogService implements RecordLogIF {
    private final RecordLogMapper recordLogMapper;

    @Override
    public ArrayList<RecordLogDTO> recordLogList(int start) throws Exception {
        if (start == 1) {
            return recordLogMapper.recordLogList(0);
        } else {
            return recordLogMapper.recordLogList((start - 1) * 20);
        }
    }

    @Transactional
    @Override
    public void addLog(RecordLogDTO recordLogDTO) throws Exception {
        recordLogMapper.addLog(recordLogDTO);
    }

    @Transactional
    @Override
    public void deleteLog(int id, String email) throws Exception {
        recordLogMapper.deleteLog(id,email);
    }

    @Override
    public int totalCount() throws Exception {
        return recordLogMapper.totalCount();
    }
}
