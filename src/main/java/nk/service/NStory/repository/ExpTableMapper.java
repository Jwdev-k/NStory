package nk.service.NStory.repository;

import nk.service.NStory.dto.ExpTable;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ExpTableMapper {
    @Select("SELECT e.level, e.minExp, e.maxExp FROM ExpTable e" +
            " INNER JOIN account a ON a.email = #{email} AND e.maxExp > a.exp AND e.minExp <= a.exp")
    ExpTable getExpRange(String email) throws Exception;
}
