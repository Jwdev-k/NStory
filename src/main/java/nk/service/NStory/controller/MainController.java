package nk.service.NStory.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import nk.service.NStory.dto.AccountDTO;
import nk.service.NStory.security.CustomUserDetails;
import nk.service.NStory.service.impl.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final AccountService accountService;

    @RequestMapping(value = "/")
    public String mainPage() {
        return "main";
    }

    @GetMapping(value = "/info")
    public String userInfo() {
        return "UserInfo";
    }

    @PostMapping(value = "/account/profile_update")
    public String updateAccountInfo(HttpServletRequest request, @RequestParam String nickname
            , @RequestParam String comment, Authentication authentication) throws Exception {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        accountService.UpdateAccountInfo(new AccountDTO(userDetails.getEmail(), nickname, comment), authentication);
        return "redirect:" + request.getHeader("referer");
    }

    @ResponseBody
    @PostMapping(value = "/account/prf_update")
    public ResponseEntity<String> updateAccountInfo2(HttpServletRequest request, @RequestParam MultipartFile profileImg
            , Authentication authentication) throws Exception {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        accountService.UpdateAccountInfo(new AccountDTO(userDetails.getEmail(), profileImg.getBytes()), authentication);

        JSONObject responseJson = new JSONObject();
        responseJson.put("result", "success");
        responseJson.put("redirectUrl", request.getHeader("referer"));
        return ResponseEntity.ok(responseJson.toJSONString());
    }

    @GetMapping(value = "/storage/prf/img", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getProfileImage(@AuthenticationPrincipal CustomUserDetails userDetails) {
        byte[] ImageByteArray = userDetails.getProfileImg();
        return new ResponseEntity<>(ImageByteArray, HttpStatus.OK);
    }
}
