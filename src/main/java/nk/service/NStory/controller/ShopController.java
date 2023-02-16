package nk.service.NStory.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ShopController {

    @RequestMapping(value = "/shop")
    public String ShopMain() {
        return "Shop";
    }
}
