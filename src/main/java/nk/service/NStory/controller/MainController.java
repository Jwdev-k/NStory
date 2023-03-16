package nk.service.NStory.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import nk.service.NStory.dto.AccountDTO;
import nk.service.NStory.security.CustomUserDetails;
import nk.service.NStory.service.impl.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final AccountService accountService;

    @RequestMapping(value = "/")
    public String mainPage() {
        return "main";
    }

    @PostMapping(value = "/account/profile_update")
    public String UpdateAccountInfo(HttpServletRequest request, @RequestParam String nickname, @RequestParam String comment
            , @RequestParam MultipartFile profileImg, Authentication authentication) throws Exception {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
/*        String base64Img = null;
        if (profileImg != null) {
            base64Img = Base64.getEncoder().encodeToString(profileImg.getBytes());
        }*/
        accountService.UpdateAccountInfo(
                new AccountDTO(userDetails.getEmail(), nickname, comment, profileImg.getBytes()), authentication);
        return "redirect:" + request.getHeader("referer");
    }

    @GetMapping(value = "/storage/prf/img", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getProfileImage(@AuthenticationPrincipal CustomUserDetails userDetails) {
        byte[] ImageByteArray = userDetails.getProfileImg();
        return new ResponseEntity<>(ImageByteArray, HttpStatus.OK);
    }
}
