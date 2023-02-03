package nk.service.NStory.repository;

import nk.service.NStory.dto.RecordLogDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;

@Mapper
public interface RecordLogMapper {
    @Select("SELECT * FROM recordlog ORDER BY id DESC")
    ArrayList<RecordLogDTO> recordLogList() throws Exception;
    @Insert("INSERT INTO recordlog VALUE(null, #{contents}, #{name}, #{time})")
    void addLog(RecordLogDTO recordLogDTO) throws Exception;
}
