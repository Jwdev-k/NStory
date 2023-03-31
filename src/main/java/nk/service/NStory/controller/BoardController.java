package nk.service.NStory.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nk.service.NStory.dto.WhiteBoard;
import nk.service.NStory.security.CustomUserDetails;
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
import java.util.Base64;

@Controller @Slf4j
@RequiredArgsConstructor
public class BoardController {
    private final WhiteBoardService whiteBoardService;
    private static final PageUtil pageUtil = new PageUtil();

    @RequestMapping(value = "/whiteboard")
    public String boardList(Model model, @RequestParam(required = false, defaultValue = "1") int page) throws Exception {
        model.addAttribute("boardList", whiteBoardService.boardList(page));
        int totalCount = whiteBoardService.totalCount();
        pageUtil.setPerPageNum(10);
        pageUtil.setPage(page);
        pageUtil.setTotalCount(totalCount > 0 ? totalCount : 1);
        model.addAttribute("pageMaker", pageUtil);
        return "WhiteBoard";
    }

    @GetMapping(value = "/whitepost")
    public String addBoard(@AuthenticationPrincipal CustomUserDetails customUserDetails, HttpServletRequest request) {
        if (customUserDetails == null) {
            return "redirect:" + request.getHeader("referer");
        }
        return "WhiteBoardAdd";
    }
    @PostMapping(value = "/whiteboard/add")
    public String addBoard2(@AuthenticationPrincipal CustomUserDetails customUserDetails, HttpServletRequest request
            ,@RequestParam String title, @RequestParam String editordata) throws Exception {
        if (customUserDetails == null) {
            return "redirect:" + request.getHeader("referer");
        }
        whiteBoardService.insertBoard(new WhiteBoard(0, title, editordata, customUserDetails.getUsername()
                , customUserDetails.getEmail(), CurrentTime.getTime3(), true));
        log.info("요청주소 : /whiteboard/add\n" + "Action : whiteboard 작성" + "\n 요청자: " + customUserDetails.getEmail());
        return "redirect:/whiteboard";
    }

    @GetMapping(value = "/whiteview")
    public String boardView(@AuthenticationPrincipal CustomUserDetails customUserDetails, HttpServletRequest request
            ,@RequestParam int id) throws Exception {
        WhiteBoard wb = whiteBoardService.getBoardView(id);
        request.setAttribute("boardInfo", wb);
        return "WhiteBoardView";
    }

    @PostMapping(value = "/whiteboard/delete")
    public String deleteBoard(@AuthenticationPrincipal CustomUserDetails customUserDetails, HttpServletRequest request
            ,@RequestParam int id, @RequestParam String email) throws Exception {
        if (customUserDetails == null) {
            return "redirect:" + request.getHeader("referer");
        }
        if (customUserDetails.getEmail().equals(email)) {
            whiteBoardService.deleteBoard(id, customUserDetails.getEmail());
            log.info("요청주소 : /whiteboard/delete" + "Action : whiteboard 삭제" + "\n 요청자: " + customUserDetails.getEmail());
        }
        return "redirect:/whiteboard";
    }

    @PostMapping(value = "/summernote/upload", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<String> uploadImage(@RequestParam("summernote") MultipartFile file) throws IOException {
        // 이미지 파일 업로드 처리
        // 업로드된 이미지 URL 반환
        String ImageByteArray = new String(Base64.getEncoder().encode(file.getBytes()), StandardCharsets.UTF_8);
        return ResponseEntity.ok().body("data:image/jpeg;base64," + ImageByteArray);
    }
}
