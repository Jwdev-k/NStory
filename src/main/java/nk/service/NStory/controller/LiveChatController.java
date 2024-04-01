package nk.service.NStory.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import nk.service.NStory.security.CustomUserDetails;
import nk.service.NStory.service.liveChat.ChatRoomService;
import nk.service.NStory.utils.PageUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequiredArgsConstructor
public class LiveChatController {

    private final ChatRoomService chatRoomService;
    private final PageUtil pageUtil = new PageUtil();

    @GetMapping(value = "/livechat")
    public String LiveChat(HttpServletRequest request, Model model, @AuthenticationPrincipal CustomUserDetails userDetails
            , @RequestParam(name = "page", required = false, defaultValue = "1") int page) {
        request.setAttribute("RoomList", chatRoomService.getRooms());
        int totalCount = chatRoomService.getRooms().size();
        pageUtil.setPerPageNum(18);
        pageUtil.setPage(page);
        pageUtil.setTotalCount(totalCount > 0 ? totalCount : 1);
        model.addAttribute("pageMaker", pageUtil);
        model.addAttribute("crtPage", page);
        return "LiveChat";
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/chatroom/{roomId}")
    public String ChatRoom(Model model, @PathVariable(name = "roomId") String roomId) {
        model.addAttribute("RoomInfo",chatRoomService.getRoom(roomId));
        return "ChatRoom";
    }

    @GetMapping(value = "/livestream/videos/{roomId}.m3u8")
    public ResponseEntity<byte[]> getLiveStreamM3U8(@PathVariable(name = "roomId") String roomId) throws Exception {
        String videoFileName = roomId + ".m3u8";
        Path m3u8Path = Paths.get(System.getProperty("user.dir") + File.separator + "liveVideos" + File.separator + roomId + File.separator + videoFileName);
        if (Files.exists(m3u8Path)) {
            return ResponseEntity.ok().body(Files.readAllBytes(m3u8Path));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping(value = "/livestream/videos/{roomId}.ts")
    public ResponseEntity<byte[]> getLiveStreamTS(@PathVariable(name = "roomId") String roomId) throws Exception {
        String videoFileName = roomId + ".ts";
        Path tsPath = Paths.get(System.getProperty("user.dir") + File.separator + "liveVideos" + File.separator + roomId + File.separator + videoFileName);
        if (Files.exists(tsPath)) {
            return ResponseEntity.ok().body(Files.readAllBytes(tsPath));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
