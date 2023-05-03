package nk.service.NStory.service;

import nk.service.NStory.dto.record.RecordLogDTO;
import nk.service.NStory.dto.record.RecordLogList;

import java.util.ArrayList;

public interface RecordLogIF {
    ArrayList<RecordLogList> recordLogList(int start) throws Exception;
    void addLog(RecordLogDTO recordLogDTO) throws Exception;
    void deleteLog(int id, String email) throws Exception;
    int totalCount() throws Exception;
}
