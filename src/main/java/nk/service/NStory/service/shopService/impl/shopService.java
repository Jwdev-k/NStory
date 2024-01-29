package nk.service.NStory.service.shopService.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nk.service.NStory.dto.ItemShop.ItemsDTO;
import nk.service.NStory.repository.ShopMapper;
import nk.service.NStory.service.shopService.shopServiceIF;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service @Slf4j
@RequiredArgsConstructor
public class shopService implements shopServiceIF {

    private final ShopMapper shopMapper;

    @Override
    public ArrayList<ItemsDTO> itemList() throws Exception {
        return shopMapper.itemList();
    }
}
