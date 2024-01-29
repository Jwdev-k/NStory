package nk.service.NStory.dto.ItemShop;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ItemsDTO {
    private int itemId;
    private String itemName;
    private int itemPrice;
    private String itemDsrp;
    private String itemImg;
}
