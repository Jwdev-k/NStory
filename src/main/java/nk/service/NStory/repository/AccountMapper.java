package nk.service.NStory.repository;

import nk.service.NStory.dto.AccountDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface AccountMapper {
    @Insert("INSERT INTO account VALUE(null,#{email},#{password},#{name},#{comment},#{profileImg},#{role},#{creationDate},#{isEnable})")
    boolean register(AccountDTO accountDTO) throws Exception;
    @Select("SELECT * FROM account WHERE isEnable = 1 AND email = #{email}")
    AccountDTO login(String email) throws Exception;
    @Select("SELECT EXISTS (SELECT email FROM account WHERE email = #{email})")
    boolean checkEmail(String email) throws Exception;
    @Update("UPDATE account SET level = #{level}")
    void UpdateLevel(int level) throws Exception;
    @Update("UPDATE account SET exp = #{exp}")
    void UpdateExp(int exp) throws Exception;
    @Update("UPDATE account SET level = #{nCoin}")
    void UpdateCoin(int nCoin) throws Exception;
}
