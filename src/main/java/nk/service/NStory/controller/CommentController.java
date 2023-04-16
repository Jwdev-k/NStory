package nk.service.NStory.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import nk.service.NStory.dto.CommentDTO;
import nk.service.NStory.security.CustomUserDetails;
import nk.service.NStory.service.impl.CommentService;
import nk.service.NStory.utils.CurrentTime;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping(value = "/whiteview/comment/add")
    public String addComment(HttpServletRequest request, @AuthenticationPrincipal CustomUserDetails userDetails
            , @RequestParam int id, @RequestParam(required = false) String name
            , @RequestParam String contents) throws Exception {
        if (userDetails != null && name == null) {
            commentService.addComment(new CommentDTO(0, id, userDetails.getEmail(), userDetails.getUsername()
                    , contents, CurrentTime.getTime4(), true));
        } else {
            // 비로그인 댓글 작성 코드
        }

        return "redirect:" + request.getHeader("referer");
    }

    @PostMapping(value = "/whiteview/comment/edit")
    public String editComment(HttpServletRequest request, @AuthenticationPrincipal CustomUserDetails userDetails
            , @RequestParam int cid, @RequestParam(required = false) String name
            , @RequestParam String contents) throws Exception {
        if (userDetails != null && name == null) {
            commentService.commentEdit(new CommentDTO(cid, userDetails.getUsername(), contents));
        } else {
            // 비로그인 댓글 수정 코드
        }

        return "redirect:" + request.getHeader("referer");
    }

    @GetMapping(value = "/whiteview/comment/delete")
    public String deleteComment(HttpServletRequest request, @AuthenticationPrincipal CustomUserDetails userDetails
            , @RequestParam int cid) throws Exception {
        CommentDTO comment = commentService.getComment(cid);
        if (comment.getEmail().equals(userDetails.getEmail())) {
            commentService.deleteComment(cid);
        }
        return "redirect:" + request.getHeader("referer");
    }
}
