package nk.service.NStory.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import nk.service.NStory.dto.ReplyDTO;
import nk.service.NStory.security.CustomUserDetails;
import nk.service.NStory.service.impl.ReplyService;
import nk.service.NStory.utils.CurrentTime;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ReplyController {
    private final ReplyService replyService;

    @PostMapping(value = "/whiteview/reply/add")
    public String addReply(HttpServletRequest request, @AuthenticationPrincipal CustomUserDetails userDetails
            ,@RequestParam(required = false) String name, @RequestParam int id, @RequestParam int cid
            , @RequestParam String contents) throws Exception {
        if (userDetails != null && name == null) {
            replyService.addReply(new ReplyDTO(0, cid, id, userDetails.getEmail(), userDetails.getUsername()
                    , contents, CurrentTime.getTime4(), true));
        } else {
            // 비로그인 reply 코드
        }

        return "redirect:" + request.getHeader("referer");
    }

    @PostMapping(value = "/whiteview/reply/edit")
    public String editReply(HttpServletRequest request, @AuthenticationPrincipal CustomUserDetails userDetails
            ,@RequestParam int rid, @RequestParam(required = false) String name, @RequestParam String contents) throws Exception {
        if (userDetails != null && name == null) {
            replyService.replyEdit(new ReplyDTO(rid, userDetails.getUsername(), contents));
        } else {
            // 비로그인 reply 코드
        }

        return "redirect:" + request.getHeader("referer");
    }

    @GetMapping(value = "/whiteview/reply/delete")
    public String deleteReply(HttpServletRequest request, @AuthenticationPrincipal CustomUserDetails userDetails
            ,@RequestParam int rid) throws Exception {
        ReplyDTO reply = replyService.getReply(rid);
        if (userDetails != null && reply.getEmail().equals(userDetails.getEmail())) {
            replyService.deleteReply(rid);
        } else {
            // 비로그인 reply 코드
        }

        return "redirect:" + request.getHeader("referer");
    }

}
