package nk.service.NStory.controller;

import lombok.RequiredArgsConstructor;
import nk.service.NStory.dto.LikesHistory;
import nk.service.NStory.security.CustomUserDetails;
import nk.service.NStory.service.impl.LikeHistoryService;
import nk.service.NStory.service.impl.WhiteBoardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LikesController {
    private final WhiteBoardService whiteBoardService;
    private final LikeHistoryService likeHistoryService;

    @PostMapping(value = "/b_bup")
    public ResponseEntity<String> like(@AuthenticationPrincipal CustomUserDetails userDetails
            , @RequestParam(name = "id") int id) throws Exception {
        if (userDetails != null) {
            LikesHistory likesHistory = likeHistoryService.getLikeType(id, userDetails.getEmail());
            LikesHistory likeObj = new LikesHistory(1, id, userDetails.getEmail());
            if (likesHistory != null) {
                if (likesHistory.getLike_type() == 0) {
                    likeHistoryService.updateLikeHistory(likeObj);
                    whiteBoardService.updateDisLikeCancel(id);
                    whiteBoardService.updateLike(id);
                } else {
                    likeHistoryService.historyLikeCancel(likeObj);
                    whiteBoardService.updateLikeCancel(id);
                }
            } else {
                likeHistoryService.insertHistory(likeObj);
                whiteBoardService.updateLike(id);
            }
            return ResponseEntity.ok().body("");
        }
        return ResponseEntity.badRequest().body("");
    }

    @PostMapping(value = "/b_pdown")
    public ResponseEntity<String> dLike(@AuthenticationPrincipal CustomUserDetails userDetails
            , @RequestParam(name = "id") int id) throws Exception {
        if (userDetails != null) {
            LikesHistory likesHistory = likeHistoryService.getLikeType(id, userDetails.getEmail());
            LikesHistory likeObj = new LikesHistory(0, id, userDetails.getEmail());
            if (likesHistory != null) {
                if (likesHistory.getLike_type() == 1) {
                    likeHistoryService.updateDisLikeHistory(likeObj);
                    whiteBoardService.updateLikeCancel(id);
                    whiteBoardService.updateDLike(id);
                } else {
                    likeHistoryService.historyDisLikeCancel(likeObj);
                    whiteBoardService.updateDisLikeCancel(id);
                }
            } else {
                likeHistoryService.insertHistory(likeObj);
                whiteBoardService.updateDLike(id);
            }
            return ResponseEntity.ok().body("");
        }
        return ResponseEntity.badRequest().body("");
    }
}
