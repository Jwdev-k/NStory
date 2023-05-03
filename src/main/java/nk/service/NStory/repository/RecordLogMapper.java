package nk.service.NStory.repository;

import nk.service.NStory.dto.record.RecordLogDTO;
import nk.service.NStory.dto.record.RecordLogList;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;

@Mapper
public interface RecordLogMapper {
    @Select("SELECT r.*, a.id as aid FROM recordlog r INNER JOIN account a ON a.email = r.email ORDER BY r.id DESC LIMIT #{start}, 18")
    ArrayList<RecordLogList> recordLogList(int start) throws Exception;
    @Insert("INSERT INTO recordlog VALUE(null, #{contents}, #{email}, #{name}, #{time})")
    void addLog(RecordLogDTO recordLogDTO) throws Exception;
    @Delete("DELETE FROM recordlog WHERE id = #{id} AND email = #{email}")
    void deleteLog(@Param("id") int id, @Param("email") String email) throws Exception;
    @Select("SELECT count(*) FROM recordlog")
    int totalCount() throws Exception;
}
