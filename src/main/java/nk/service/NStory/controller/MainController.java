package nk.service.NStory.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import nk.service.NStory.dto.AccountDTO;
import nk.service.NStory.dto.ByteImageDTO;
import nk.service.NStory.security.CustomUserDetails;
import nk.service.NStory.service.impl.AccountService;
import nk.service.NStory.service.impl.BoardInfoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final AccountService accountService;
    private final BoardInfoService boardInfoService;

    @RequestMapping(value = "/")
    public String mainPage(HttpServletRequest request) throws Exception {
        request.setAttribute("boardInfoList", boardInfoService.getBoardNameList());
        return "main";
    }

    @GetMapping(value = "/info")
    public String userInfo(@AuthenticationPrincipal CustomUserDetails userDetails
            , HttpServletRequest request, Model model) {
        if (userDetails == null) {
            return "redirect:" + request.getHeader("referer");
        } else if (userDetails.getOAuth2UserInfo() != null) {
            model.addAttribute("pwValue", "SNS 로그인 사용중입니다.");
        } else {
            model.addAttribute("pwValue", "••••••••••••••••");
        }
        return "UserInfo";
    }

    @PostMapping(value = "/account/profile_update")
    public String updateAccountInfo(HttpServletRequest request, @RequestParam String nickname
            , @RequestParam String comment, Authentication authentication) throws Exception {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        if (comment.length() > 100) {
            comment = comment.substring(0, 100);
        }
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

    @GetMapping(value = "/general/prf/{id}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getProfileImage(HttpServletRequest request, @PathVariable int id) throws Exception {
        ByteImageDTO ImageByteArray = accountService.getUserImage(id);
        if (ImageByteArray != null && ImageByteArray.getImage().length > 0) {
            return new ResponseEntity<>(ImageByteArray.getImage(), HttpStatus.OK);
        }
        File defaultImg = ResourceUtils.getFile("classpath:static/images/default_profileImg.png");
        FileInputStream in = new FileInputStream(defaultImg);
        return new ResponseEntity<>(in.readAllBytes(), HttpStatus.OK);
    }
}
