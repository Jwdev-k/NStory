package nk.service.NStory.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import nk.service.NStory.dto.AccountDTO;
import nk.service.NStory.dto.ByteImageDTO;
import nk.service.NStory.gpt.ChatGPTClient;
import nk.service.NStory.security.CustomUserDetails;
import nk.service.NStory.service.impl.AccountService;
import nk.service.NStory.service.impl.WhiteBoardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final WhiteBoardService whiteBoardService;

    @RequestMapping(value = "/")
    public String mainPage(HttpServletRequest request) throws Exception {
        request.setAttribute("bestList", whiteBoardService.getBestList());
        request.setAttribute("recentList", whiteBoardService.getRecentList());
        request.setAttribute("mainNoticeList", whiteBoardService.getMainNoticeList());
        return "main";
    }

    @GetMapping(value = "/info")
    public String userInfo(@AuthenticationPrincipal CustomUserDetails userDetails
            , HttpServletRequest request, Model model) {
        if (userDetails == null) {
            return "redirect:/";
        } else if (userDetails.isOAuth()) {
            model.addAttribute("pwValue", "SNS 로그인 사용중입니다.");
        } else {
            model.addAttribute("pwValue", "••••••••••••••••");
        }
        return "UserInfo";
    }

    @PostMapping(value = "/account/profile_update")
    public String updateAccountInfo(HttpServletRequest request, @RequestParam String nickname
            , @RequestParam String comment, @AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {
        if (comment.length() > 100) {
            comment = comment.substring(0, 100);
        }
        accountService.UpdateAccountInfo(new AccountDTO(userDetails.getEmail(), nickname, comment));
        return "redirect:" + request.getHeader("referer");
    }

    @ResponseBody
    @PostMapping(value = "/account/prf_update")
    public ResponseEntity<String> updateAccountInfo2(HttpServletRequest request, @RequestParam MultipartFile profileImg
            , @AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {
        accountService.UpdateAccountInfo(new AccountDTO(userDetails.getEmail(), profileImg.getBytes()));

        JSONObject responseJson = new JSONObject();
        responseJson.put("result", "success");
        responseJson.put("redirectUrl", request.getHeader("referer"));
        return ResponseEntity.ok(responseJson.toJSONString());
    }

    @GetMapping(value = "/general/prf_img/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getProfileImage(@PathVariable int id) throws Exception {
        ByteImageDTO ImageByteArray = accountService.getUserImage(id);
        if (ImageByteArray != null && ImageByteArray.getImage().length > 0) {
            return new ResponseEntity<>(ImageByteArray.getImage(), HttpStatus.OK);
        }
        File defaultImg = ResourceUtils.getFile("classpath:static/images/default_profileImg.png");
        FileInputStream in = new FileInputStream(defaultImg);
        return new ResponseEntity<>(in.readAllBytes(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/chatroom")
    public String ChatRoom() {
        return "ChatRoom";
    }

    /* ChatGPT API 컨트롤러 */
    @GetMapping(value = "/gpt")
    public String ChatGPT() {
        return "ai/ChatGPT";
    }
    @PostMapping(value = "/gpt/request")
    @ResponseBody
    public ResponseEntity<String> resultGPT(@RequestParam String message) {
        if (message != null) {
           return ResponseEntity.ok(ChatGPTClient.AIChat(message));
        }
        return ResponseEntity.badRequest().body("null");
    }
}
