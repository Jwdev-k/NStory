package nk.service.NStory.service.shopService;

import nk.service.NStory.dto.ItemShop.ItemsDTO;

import java.util.ArrayList;

public interface shopServiceIF {
    ArrayList<ItemsDTO> itemList() throws Exception;
}
