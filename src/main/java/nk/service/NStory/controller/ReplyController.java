package nk.service.NStory.controller;

import lombok.RequiredArgsConstructor;
import nk.service.NStory.dto.ReplyDTO;
import nk.service.NStory.security.CustomUserDetails;
import nk.service.NStory.service.impl.ReplyService;
import nk.service.NStory.utils.CurrentTime;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReplyController {
    private final ReplyService replyService;

    @PostMapping(value = "/whiteview/reply/add")
    public ResponseEntity<String> addReply(@AuthenticationPrincipal CustomUserDetails userDetails
            , @RequestParam(required = false) String name, @RequestParam int id, @RequestParam int cid
            , @RequestParam String contents) throws Exception {
        contents = contents.replaceAll("(?i)<script.*?>.*?</script.*?>", "");
        if (userDetails != null && name == null && contents.length() > 0) {
            if (contents.length() > 300) {
                contents = contents.substring(0, 300);
            }
            replyService.addReply(new ReplyDTO(0, cid, id, userDetails.getEmail(), userDetails.getUsername()
                    , contents, CurrentTime.getTime4(), true));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok().body(null);
    }

    @PostMapping(value = "/whiteview/reply/edit")
    public ResponseEntity<String> editReply(@AuthenticationPrincipal CustomUserDetails userDetails
            ,@RequestParam int rid, @RequestParam(required = false) String name, @RequestParam String contents) throws Exception {
        contents = contents.replaceAll("(?i)<script.*?>.*?</script.*?>", "");
        if (userDetails != null && name == null && contents.length() > 0) {
            if (contents.length() > 300) {
                contents = contents.substring(0, 300);
            }
            replyService.replyEdit(new ReplyDTO(rid, userDetails.getEmail(), userDetails.getUsername(), contents));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok().body(null);
    }

    @PostMapping(value = "/whiteview/reply/delete")
    public ResponseEntity<String> deleteReply(@AuthenticationPrincipal CustomUserDetails userDetails
            ,@RequestParam int rid) throws Exception {
        ReplyDTO reply = replyService.getReply(rid);
        if (userDetails != null && reply.getEmail().equals(userDetails.getEmail())) {
            replyService.deleteReply(rid);
        } else {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok().body(null);
    }

}
