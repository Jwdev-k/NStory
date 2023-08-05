package nk.service.NStory.controller;

import lombok.RequiredArgsConstructor;
import nk.service.NStory.dto.myhistory.HistoryCommentsDTO;
import nk.service.NStory.dto.myhistory.HistoryPostsDTO;
import nk.service.NStory.security.CustomUserDetails;
import nk.service.NStory.service.impl.MyHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequiredArgsConstructor
public class MyHistoryController {
    private final MyHistoryService historyService;

    @GetMapping(value = "/recent_post")
    public ResponseEntity<ArrayList<HistoryPostsDTO>> getMyBoardList(@AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {
        return ResponseEntity.ok().body(historyService.getMyPosts(userDetails.getEmail()));
    }

    @GetMapping("/recent_comment")
    public ResponseEntity<ArrayList<HistoryCommentsDTO>> getMyCommentList(@AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {
        return ResponseEntity.ok().body(historyService.getMyComments(userDetails.getEmail()));
    }
}
