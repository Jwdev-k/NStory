package nk.service.NStory.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import nk.service.NStory.dto.RecordLogDTO;
import nk.service.NStory.security.CustomUserDetails;
import nk.service.NStory.service.impl.RecordLogService;
import nk.service.NStory.utils.CurrentTime;
import nk.service.NStory.utils.PageUtil;
import nk.service.NStory.utils.ScriptUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
public class RecordLogController {
    @Autowired
    private RecordLogService recordLogService;
    private static final PageUtil pageUtil = new PageUtil();

    @RequestMapping(value = "/record")
    public String main(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model
            ,@RequestParam(required = false, defaultValue = "1") int page) throws Exception {
        model.addAttribute("Email", customUserDetails != null ? customUserDetails.getEmail() : null);
        model.addAttribute("recordLogList", recordLogService.recordLogList(page));
        pageUtil.setTotalCount(recordLogService.totalCount());
        pageUtil.setPage(page);
        model.addAttribute("pageMaker", pageUtil);
        model.addAttribute("crtPage", page);
        return "RecordLog";
    }

    @PostMapping(value = "/record/frmlog")
    public String FrmLogAdd(HttpServletRequest request, @RequestParam String contents
            , @AuthenticationPrincipal CustomUserDetails customUserDetails) throws Exception {
        if (customUserDetails != null) {
            recordLogService.addLog(new RecordLogDTO(0, contents, customUserDetails.getEmail(), customUserDetails.getUsername(), CurrentTime.getTime()));
        }
        return "redirect:" + request.getHeader("referer");
    }
    @PostMapping(value = "/record/frmlog_delete")
    public String FrmLogDelete(@AuthenticationPrincipal CustomUserDetails customUserDetails, HttpServletResponse response
            , HttpServletRequest request, @RequestParam int id, @RequestParam String email) throws Exception {
        log.info(id + ", " + email);
        if (customUserDetails.getEmail().equals(email)) {
            recordLogService.deleteLog(id, email);
        } else {
            ScriptUtils.alertAndBackPage(response, "[오류] 권한이 없습니다.");
        }
        return "redirect:" + request.getHeader("referer");
    }
}
