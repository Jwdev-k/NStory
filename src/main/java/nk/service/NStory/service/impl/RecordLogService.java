package nk.service.NStory.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nk.service.NStory.dto.RecordLogDTO;
import nk.service.NStory.repository.RecordLogMapper;
import nk.service.NStory.service.RecordLogIF;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service @Slf4j
@RequiredArgsConstructor
public class RecordLogService implements RecordLogIF {
    private final RecordLogMapper recordLogMapper;

    @Override
    public ArrayList<RecordLogDTO> recordLogList(int start) throws Exception {
        if (start == 1 || start < 1) {
            return recordLogMapper.recordLogList(0);
        } else {
            return recordLogMapper.recordLogList((start - 1) * 18);
        }
    }

    @Transactional
    @Override
    public void addLog(RecordLogDTO recordLogDTO) throws Exception {
        recordLogMapper.addLog(recordLogDTO);
        log.info("요청주소 : /record/frmlog\n" + "Action : record 작성" + "\n 요청자: " + recordLogDTO.getEmail());
    }

    @Transactional
    @Override
    public void deleteLog(int id, String email) throws Exception {
        recordLogMapper.deleteLog(id,email);
        log.info("요청주소 : /record/frmlog_delete\n" + "Action :" + id + "번 게시물 삭제\n" + "요청자: " + email);
    }

    @Override
    public int totalCount() throws Exception {
        return recordLogMapper.totalCount();
    }
}
