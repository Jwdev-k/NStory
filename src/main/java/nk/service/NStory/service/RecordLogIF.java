package nk.service.NStory.service;

import nk.service.NStory.dto.RecordLogDTO;

import java.util.ArrayList;

public interface RecordLogIF {
    ArrayList<RecordLogDTO> recordLogList() throws Exception;
    void addLog(RecordLogDTO recordLogDTO) throws Exception;
}
