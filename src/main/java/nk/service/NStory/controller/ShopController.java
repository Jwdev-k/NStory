package nk.service.NStory.controller;

import lombok.RequiredArgsConstructor;
import nk.service.NStory.service.shopService.impl.shopService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/shop")
public class ShopController {

    private final shopService shopService;

    @GetMapping(value = "")
    public String ShopMain(Model model) throws Exception {
        model.addAttribute("ShopList", shopService.itemList());
        return "Shop";
    }

    @GetMapping(value = "/itemImg/{imgName}")
    public ResponseEntity<byte[]> getImage(@PathVariable String imgName) {
        try {
            String imgPath = System.getProperty("user.dir") + File.separator + "ShopImages" + File.separator + imgName;
            Path path = Paths.get(imgPath);
            if (Files.exists(path)) {
                byte[] imageBytes = Files.readAllBytes(path);
                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
