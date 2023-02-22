package nk.service.NStory.repository;

import nk.service.NStory.dto.AccountDTO;
import org.apache.ibatis.annotations.*;

@Mapper
public interface AccountMapper {
    @Insert("INSERT INTO account VALUE(null,#{email},#{password},#{name},#{comment},#{profileImg},#{role},#{creationDate},#{lastDateTime},#{isEnable})")
    boolean register(AccountDTO accountDTO) throws Exception;
    @Select("SELECT * FROM account WHERE isEnable = 1 AND email = #{email}")
    AccountDTO login(String email) throws Exception;
    @Select("SELECT EXISTS (SELECT email FROM account WHERE email = #{email})")
    boolean checkEmail(String email) throws Exception;
    @Update("UPDATE account SET level = #{level} WHERE email = #{email}")
    void UpdateLevel(@Param("level")int level, @Param("email")String email) throws Exception;
    @Update("UPDATE account SET exp = exp + #{exp} WHERE email = #{email}")
    void UpdateExp(@Param("exp")int exp, @Param("email")String email) throws Exception;
    @Update("UPDATE account SET nCoin = nCoin + #{nCoin} WHERE email = #{email}")
    void UpdateCoin(@Param("nCoin")int nCoin, @Param("email")String email) throws Exception;
    @Update("UPDATE account SET lastLoginDate = #{lastLoginDate} WHERE email = #{email}")
    void UpdateLastLoginDate(@Param("lastLoginDate")String lastLoginDate, @Param("email")String email) throws Exception;
}
