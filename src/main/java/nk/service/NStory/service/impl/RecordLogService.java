package nk.service.NStory.service.impl;

import nk.service.NStory.dto.RecordLogDTO;
import nk.service.NStory.repository.RecordLogMapper;
import nk.service.NStory.service.RecordLogIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
public class RecordLogService implements RecordLogIF {
    @Autowired
    private RecordLogMapper recordLogMapper;

    @Override
    public ArrayList<RecordLogDTO> recordLogList(int start) throws Exception {
        if (start == 1) {
            return recordLogMapper.recordLogList(0);
        } else {
            return recordLogMapper.recordLogList((start - 1) * 50);
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
