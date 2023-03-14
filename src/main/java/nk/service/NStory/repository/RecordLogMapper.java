package nk.service.NStory.repository;

import nk.service.NStory.dto.RecordLogDTO;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;

@Mapper
public interface RecordLogMapper {
    @Select("SELECT * FROM recordlog ORDER BY id DESC LIMIT #{start}, 20")
    ArrayList<RecordLogDTO> recordLogList(int start) throws Exception;
    @Insert("INSERT INTO recordlog VALUE(null, #{contents}, #{email}, #{name}, #{time})")
    void addLog(RecordLogDTO recordLogDTO) throws Exception;
    @Delete("DELETE FROM recordlog WHERE id = #{id} AND email = #{email}")
    void deleteLog(@Param("id") int id, @Param("email") String email) throws Exception;
    @Select("SELECT count(*) FROM recordlog")
    int totalCount() throws Exception;
}
