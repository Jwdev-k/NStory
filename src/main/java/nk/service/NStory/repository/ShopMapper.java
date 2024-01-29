package nk.service.NStory.repository;

import nk.service.NStory.dto.ItemShop.ItemsDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;

@Mapper
public interface ShopMapper {
    @Select("SELECT * FROM shop_items")
    ArrayList<ItemsDTO> itemList() throws Exception;
}
