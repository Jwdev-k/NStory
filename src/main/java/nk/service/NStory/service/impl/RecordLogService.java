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
    public ArrayList<RecordLogDTO> recordLogList() throws Exception {
        return recordLogMapper.recordLogList();
    }

    @Transactional
    @Override
    public void addLog(RecordLogDTO recordLogDTO) throws Exception {
        recordLogMapper.addLog(recordLogDTO);
    }
}
