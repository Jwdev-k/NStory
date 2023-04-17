package nk.service.NStory.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nk.service.NStory.dto.CommentDTO;
import nk.service.NStory.dto.Enum.SearchType;
import nk.service.NStory.dto.ReplyDTO;
import nk.service.NStory.dto.WhiteBoard;
import nk.service.NStory.security.CustomUserDetails;
import nk.service.NStory.service.impl.CommentService;
import nk.service.NStory.service.impl.ReplyService;
import nk.service.NStory.service.impl.WhiteBoardService;
import nk.service.NStory.utils.CurrentTime;
import nk.service.NStory.utils.PageUtil;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;

@Controller @Slf4j
@RequiredArgsConstructor
public class BoardController {
    private final WhiteBoardService whiteBoardService;
    private final CommentService commentService;
    private final ReplyService replyService;
    private final PageUtil pageUtil = new PageUtil();

    @RequestMapping(value = "/whiteboard")
    public String boardList(HttpServletResponse response, HttpServletRequest request, Model model, @RequestParam String bid
            , @RequestParam(required = false, defaultValue = "1") int page, @RequestParam(required = false) String str
            , @RequestParam(required = false, defaultValue = "title") SearchType type) throws Exception {
        int totalCount;
        boolean isSearch;
        pageUtil.setPerPageNum(10);
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
        request.setAttribute("bid", bid); //게시판 이름
        request.setAttribute("isSearch", isSearch);

        model.addAttribute("pageMaker", pageUtil);

        Cookie cookie = new Cookie("searchType", String.valueOf(type.getType())); //검색타입 쿠키저장
        cookie.setPath("/whiteboard");
        response.addCookie(cookie);

        Cookie cookie2 = new Cookie("lastPage", String.valueOf(page));
        cookie.setPath("/whiteview");
        response.addCookie(cookie2);

        log.info("검색타입: " + type);
        return "WhiteBoard";
    }

    @GetMapping(value = "/whitepost")
    public String addBoard(@AuthenticationPrincipal CustomUserDetails userDetails, HttpServletRequest request
            ,@RequestParam String bid) {
        if (userDetails == null) {
            return "redirect:" + request.getHeader("referer");
        }
        request.setAttribute("bid", bid);
        return "WhiteBoardAdd";
    }

    @PostMapping(value = "/whiteboard/add")
    public String addBoard2(@AuthenticationPrincipal CustomUserDetails userDetails, HttpServletRequest request
            ,@RequestParam String bid, @RequestParam String title, @RequestParam String editordata) throws Exception {
        if (userDetails == null) {
            return "redirect:" + request.getHeader("referer");
        } else {
            whiteBoardService.insertBoard(new WhiteBoard(0, bid, title, editordata, userDetails.getUsername()
                    , userDetails.getEmail(), CurrentTime.getTime3(), true));
            log.info("요청주소 : /whiteboard/add\n" + "Action : whiteboard 작성" + "\n 요청자: " + userDetails.getEmail());
        }
        return "redirect:/whiteboard?bid=" + bid;
    }

    @GetMapping(value = "/whiteview")
    public String boardView(HttpServletRequest request, @RequestParam int id) throws Exception {
        WhiteBoard wb = whiteBoardService.getBoardView(id);
        if (wb != null) {
            request.setAttribute("boardInfo", wb);

            int lastPage = 1;
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("lastPage")) {
                        lastPage = Integer.parseInt(cookie.getValue());
                    }
                }
            }
            request.setAttribute("redirectURL", "/whiteboard?bid=" + wb.getBid() + "&page=" + lastPage);

            ArrayList<CommentDTO> commentList = commentService.getCommentList(id);
            ArrayList<ReplyDTO> replyList = replyService.getReplyList(id);
            request.setAttribute("commentList", commentList);
            request.setAttribute("replyList", replyList);
            request.setAttribute("totalCount"
                    , "(" + (commentList.size() + replyList.size()) + ")");
            return "WhiteBoardView";
        }
        return "redirect:" + request.getHeader("referer");
    }

    @PostMapping(value = "/whiteboard/delete")
    public String deleteBoard(@AuthenticationPrincipal CustomUserDetails userDetails, HttpServletRequest request
            ,@RequestParam String bid, @RequestParam int id, @RequestParam String email) throws Exception {
        if (userDetails == null) {
            return "redirect:" + request.getHeader("referer");
        }
        else if (userDetails.getEmail().equals(email)) {
            whiteBoardService.deleteBoard(id, userDetails.getEmail());
            log.info("요청주소 : /whiteboard/delete" + "Action : whiteboard 삭제" + "\n 요청자: " + userDetails.getEmail());
        }
        return "redirect:/whiteboard?bid=" + bid;
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
            , @RequestParam int id, @RequestParam String title, @RequestParam String editordata) throws Exception {
        if (userDetails == null) {
            return "redirect:" + request.getHeader("referer");
        } else {
            whiteBoardService.updateBoard(
                    new WhiteBoard(id, title, editordata, userDetails.getUsername(), userDetails.getEmail()));
            log.info("요청주소 : /whiteboard/update\n" + "Action : whiteboard 수정" + "\n 요청자: " + userDetails.getEmail());
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
