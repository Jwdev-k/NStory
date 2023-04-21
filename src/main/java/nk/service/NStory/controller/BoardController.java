package nk.service.NStory.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import nk.service.NStory.dto.CommentDTO;
import nk.service.NStory.dto.Enum.SearchType;
import nk.service.NStory.dto.LikesHistory;
import nk.service.NStory.dto.ReplyDTO;
import nk.service.NStory.dto.WhiteBoard;
import nk.service.NStory.security.CustomUserDetails;
import nk.service.NStory.service.impl.*;
import nk.service.NStory.utils.CurrentTime;
import nk.service.NStory.utils.PageUtil;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Base64;

@Controller
@RequiredArgsConstructor
public class BoardController {
    private final WhiteBoardService whiteBoardService;
    private final CommentService commentService;
    private final ReplyService replyService;
    private final LikeHistoryService likeHistoryService;
    private final BoardInfoService boardInfoService;
    private final PageUtil pageUtil = new PageUtil();

    @RequestMapping(value = "/whiteboard")
    public String boardList(HttpServletResponse response, HttpServletRequest request, Model model, @RequestParam String bid
            , @RequestParam(required = false, defaultValue = "1") int page, @RequestParam(required = false) String str
            , @RequestParam(required = false, defaultValue = "title") SearchType type) throws Exception {
        int totalCount;
        boolean isSearch;
        pageUtil.setPerPageNum(50);
        if (str != null && str.length() > 0) {
            model.addAttribute("boardList", whiteBoardService.searchList(bid, page, type, str));
            totalCount = whiteBoardService.searchTotalCount(bid, type, str);
            pageUtil.setPage(page);
            pageUtil.setTotalCount(totalCount > 0 ? totalCount : 1);
            request.setAttribute("type", type);
            request.setAttribute("str", str);

            isSearch = true;
        } else {
            model.addAttribute("boardList", whiteBoardService.boardList(bid, page));
            totalCount = whiteBoardService.totalCount(bid);
            pageUtil.setPage(page);
            pageUtil.setTotalCount(totalCount > 0 ? totalCount : 1);

            isSearch = false;
        }
        model.addAttribute("noticeList", whiteBoardService.getNoticeList(bid));
        request.setAttribute("boardInfo", boardInfoService.getBoardInfo(bid));
        request.setAttribute("bid", bid); //게시판 이름
        request.setAttribute("isSearch", isSearch);

        model.addAttribute("pageMaker", pageUtil);

        Cookie cookie = new Cookie("searchType", String.valueOf(type.getType())); //검색타입 쿠키저장
        cookie.setPath("/whiteboard");
        response.addCookie(cookie);

        Cookie cookie2 = new Cookie("lastPage", String.valueOf(page));
        cookie.setPath("/whiteview");
        response.addCookie(cookie2);

        return "WhiteBoard";
    }

    @GetMapping(value = "/whitepost")
    public String addBoard(@AuthenticationPrincipal CustomUserDetails userDetails, HttpServletRequest request
            , @RequestParam String bid) throws Exception {
        if (userDetails == null) {
            return "redirect:" + request.getHeader("referer");
        } else {
            boolean isAdmin = boardInfoService.getBoardInfo(bid).getEmail().equals(userDetails.getEmail());
            request.setAttribute("isAdmin", isAdmin);
            request.setAttribute("bid", bid);
        }
        return "WhiteBoardAdd";
    }

    @PostMapping(value = "/whiteboard/add")
    public String addBoard2(@AuthenticationPrincipal CustomUserDetails userDetails, HttpServletRequest request
            , @RequestParam String bid, @RequestParam String title, @RequestParam String editordata
            , @RequestParam(required = false) boolean isNotice) throws Exception {
        if (userDetails == null) {
            return "redirect:" + request.getHeader("referer");
        } else {
            whiteBoardService.insertBoard(new WhiteBoard(0, bid, title, editordata, userDetails.getUsername(), userDetails.getEmail()
                    , CurrentTime.getTime4(), 0, 0, 0, isNotice, true));
        }
        return "redirect:/whiteboard?bid=" + bid;
    }

    @GetMapping(value = "/whiteview")
    public String boardView(HttpServletRequest request, HttpServletResponse response, @RequestParam int id
            ,@AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {
        int lastPage = 1;
        boolean isViews = false;
        String viewsValue = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("lastPage")) {
                    lastPage = Integer.parseInt(cookie.getValue());
                    break;
                }
            }
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("views")) {
                    if (cookie.getValue().contains("[" + id + "]")) {
                        isViews = true;
                    } else {
                        whiteBoardService.updateViews(id);
                        viewsValue = cookie.getValue();
                    }
                    break;
                }
            }
        }
        if (!isViews) {
            long todayEndSecond = LocalDate.now().atTime(LocalTime.MAX).toEpochSecond(ZoneOffset.UTC);
            long currentSecond = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
            Cookie cookie = new Cookie("views", "[" + id + "]");
            if (viewsValue != null) {
                cookie.setValue(viewsValue + "[" + id + "]");
            }
            cookie.setPath("/whiteview");
            cookie.setMaxAge((int) (todayEndSecond - currentSecond));
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
        }
        WhiteBoard wb = whiteBoardService.getBoardView(id);
        if (wb != null) {
            request.setAttribute("boardInfo", wb);
            request.setAttribute("redirectURL", "/whiteboard?bid=" + wb.getBid() + "&page=" + lastPage);

            ArrayList<CommentDTO> commentList = commentService.getCommentList(id);
            ArrayList<ReplyDTO> replyList = replyService.getReplyList(id);
            request.setAttribute("commentList", commentList);
            request.setAttribute("replyList", replyList);
            request.setAttribute("totalCount"
                    , "(" + (commentList.size() + replyList.size()) + ")");
            if (userDetails != null) {
                LikesHistory likesHistory = likeHistoryService.getLikeType(id, userDetails.getEmail());
                request.setAttribute("LikeType", likesHistory != null ? likesHistory.getLike_type() : null);
            }

            return "WhiteBoardView";
        }
        return "redirect:" + request.getHeader("referer");
    }

    @ResponseBody
    @PostMapping(value = "/whiteboard/delete")
    public ResponseEntity<String> deleteBoard(@AuthenticationPrincipal CustomUserDetails userDetails
            , @RequestParam String bid, @RequestParam int id, @RequestParam String email) throws Exception {
        if (userDetails.getEmail().equals(email)) {
            whiteBoardService.deleteBoard(id, userDetails.getEmail());
        }
        return ResponseEntity.ok().body("/whiteboard?bid=" + bid);
    }

    @GetMapping(value = "/whitepostup")
    public String updateBoard(@AuthenticationPrincipal CustomUserDetails userDetails, HttpServletRequest request
            , @RequestParam int id) throws Exception {
        if (userDetails == null) {
            return "redirect:" + request.getHeader("referer");
        } else {
            WhiteBoard wb = whiteBoardService.getBoardView(id);
            if (wb != null && wb.getEmail().equals(userDetails.getEmail())) {
                request.setAttribute("boardInfo", wb);
                return "WhiteBoardEdit";
            } else {
                return "redirect:" + request.getHeader("referer");
            }
        }
    }

    @PostMapping(value = "/whiteboard/update")
    public String updateBoard2(@AuthenticationPrincipal CustomUserDetails userDetails, HttpServletRequest request
            , @RequestParam int id, @RequestParam String title, @RequestParam String editordata
            , @RequestParam(required = false) boolean isNotice) throws Exception {
        if (userDetails == null) {
            return "redirect:" + request.getHeader("referer");
        } else {
            whiteBoardService.updateBoard(
                    new WhiteBoard(id, title, editordata, userDetails.getUsername(), userDetails.getEmail(), isNotice));
        }
        return "redirect:/whiteview?id=" + id;
    }


    @PostMapping(value = "/summernote/upload", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<String> uploadImage(@RequestParam("summernote") MultipartFile file) throws IOException {
        // 이미지 파일 업로드 처리
        // 업로드된 이미지 URL 반환
        String ImageByteArray = new String(Base64.getEncoder().encode(file.getBytes()), StandardCharsets.UTF_8);
        return ResponseEntity.ok().body("data:image/jpeg;base64," + ImageByteArray);
    }
}
