package nk.service.NStory.repository;

import nk.service.NStory.dto.AccountDTO;
import org.apache.ibatis.annotations.Insert;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LoginMapper {
    @Insert("INSERT INTO account VALUE(null,#{email},#{password},#{name},#{comment},#{profileImg},#{role},#{creationDate},#{isEnable})")
    boolean register(AccountDTO accountDTO) throws Exception;
    @Select("SELECT * FROM account WHERE isEnable = 1 AND email = #{email}")
    AccountDTO login(String email) throws Exception;
}
