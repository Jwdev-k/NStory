package nk.service.NStory.service;

import nk.service.NStory.dto.RecordLogDTO;

import java.util.ArrayList;

public interface RecordLogIF {
    ArrayList<RecordLogDTO> recordLogList(int start) throws Exception;
    void addLog(RecordLogDTO recordLogDTO) throws Exception;
    void deleteLog(int id, String email) throws Exception;
    int totalCount() throws Exception;
}
