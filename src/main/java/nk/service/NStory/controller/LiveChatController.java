package nk.service.NStory.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nk.service.NStory.dto.liveChat.LiveRoom;
import nk.service.NStory.security.CustomUserDetails;
import nk.service.NStory.service.liveChat.ChatRoomService;
import nk.service.NStory.utils.PageUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LiveChatController {

    private final ChatRoomService chatRoomService;
    private final PageUtil pageUtil = new PageUtil();

    @GetMapping(value = "/livechat")
    public String LiveChat(HttpServletRequest request
            , Model model, @AuthenticationPrincipal CustomUserDetails userDetails
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
    public String ChatRoom(HttpServletRequest request, HttpSession session
            , @AuthenticationPrincipal CustomUserDetails userDetails
            , @PathVariable(name = "roomId") String roomId) {
        LiveRoom liveRoom = chatRoomService.getRoom(roomId);
        if (!liveRoom.isSecret()) {
            session.removeAttribute("LiveAuthSession");
            request.setAttribute("RoomInfo", liveRoom);
            return "ChatRoom";
        } else {
            if (session.getAttribute("LiveAuthSession") != null) {
                int liveAuth = Integer.parseInt(session.getAttribute("LiveAuthSession").toString());
                if (liveAuth == 1) {
                    session.removeAttribute("LiveAuthSession");
                    request.setAttribute("RoomInfo", liveRoom);
                    return "ChatRoom";
                } else {
                    return "redirect:" + request.getHeader("referer");
                }
            } else if(userDetails != null) {
                if (liveRoom.getAid() == userDetails.getAid()) {
                    request.setAttribute("RoomInfo", liveRoom);
                    return "ChatRoom";
                }
            }
        }
        return "redirect:" + request.getHeader("referer");
    }

    @PostMapping(value = "/chatroom/auth/{roomId}")
    @ResponseBody
    public ResponseEntity<Boolean> CheckPassword(HttpSession session, @PathVariable(name = "roomId") String roomId
            , @RequestHeader(name = "Room-Password", required = false) String password) {
        LiveRoom liveRoom = chatRoomService.getRoom(roomId);
        if(!liveRoom.isSecret()) {
            session.setAttribute("LiveAuthSession", "0");
            return ResponseEntity.ok(true);
        } else {
            if(liveRoom.getRoomPassword().equals(password)) {
                session.setAttribute("LiveAuthSession", "1");
                return ResponseEntity.ok(true);
            } else {
                return ResponseEntity.badRequest().body(false);
            }
        }
    }

    @PostMapping(value = "/chatroom/add")
    public String ChatRoomAdd(@AuthenticationPrincipal CustomUserDetails userDetails
            , @RequestParam(name = "roomName") String roomName
            , @RequestParam(name = "roomPassword", defaultValue = "") String roomPassword
            , @RequestParam(name = "isSecret", defaultValue = "0") boolean isSecret) {
        LiveRoom liveRoom = new LiveRoom();
        liveRoom.setRoomId(UUID.randomUUID().toString());
        liveRoom.setRoomName(roomName);
        liveRoom.setRoomPassword(roomPassword);
        liveRoom.setSecret(isSecret);
        liveRoom.setHostName(userDetails.getUsername());
        liveRoom.setAid(userDetails.getAid());
        liveRoom.setEmail(userDetails.getEmail());
        chatRoomService.addRoom(liveRoom);

        return "redirect:/chatroom/" + liveRoom.getRoomId();
    }

    @PostMapping(value = "/chatroom/remove")
    public String ChatRoomAdd(@AuthenticationPrincipal CustomUserDetails userDetails
            , @RequestParam(name = "roomId") String roomId) {
        LiveRoom liveRoom = chatRoomService.getRoom(roomId);
        if (liveRoom.getAid() == userDetails.getAid()) {
            chatRoomService.removeRoom(roomId);
        }
        return "redirect:/livechat?page=1";
    }

    @GetMapping(value = "/livestream/videos/{roomId}/{filename}")
    @ResponseBody
    public ResponseEntity<byte[]> getLiveStreamVideos(@PathVariable(name = "roomId") String roomId
            ,@PathVariable(name = "filename") String filename) throws Exception {
        Path videoPath = Paths.get(System.getProperty("user.dir") + File.separator + "liveVideos" + File.separator + roomId + File.separator + filename);

        log.info("Requested filename: " + filename);
        log.info("Generated roomId: " + roomId);
        log.info("Video path: " + videoPath);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentLength(videoPath.toFile().length());
        if (Files.exists(videoPath)) {
            if (filename.contains(".ts")) {
                headers.setContentType(MediaType.valueOf("video/MP2T")); // MPEG-2 Transport Stream
                headers.setContentDispositionFormData("inline", filename);
            } else if (filename.contains(".m4s")) {
                headers.setContentType(MediaType.valueOf("video/mp4")); // MPEG-4 for fragmented MP4 (m4s)
                headers.setContentDispositionFormData("inline", filename);
            } else {
                headers.setContentType(MediaType.valueOf("application/x-mpegURL")); // HLS MIME type
                headers.setContentDispositionFormData("inline", "playlist.m3u8");
            }

            return ResponseEntity.ok().headers(headers).body(Files.readAllBytes(videoPath));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
