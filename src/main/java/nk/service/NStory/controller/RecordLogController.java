package nk.service.NStory.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import nk.service.NStory.dto.record.RecordLogDTO;
import nk.service.NStory.security.CustomUserDetails;
import nk.service.NStory.service.impl.RecordLogService;
import nk.service.NStory.utils.CurrentTime;
import nk.service.NStory.utils.PageUtil;
import nk.service.NStory.utils.ScriptUtils;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class RecordLogController {
    private final RecordLogService recordLogService;
    private final PageUtil pageUtil = new PageUtil();

    @RequestMapping(value = "/record")
    public String main(@AuthenticationPrincipal CustomUserDetails userDetails, Model model
            ,@RequestParam(required = false, defaultValue = "1") int page) throws Exception {
        model.addAttribute("Email", userDetails != null ? userDetails.getEmail() : null);
        model.addAttribute("recordLogList", recordLogService.recordLogList(page));
        int totalCount = recordLogService.totalCount();
        pageUtil.setPerPageNum(18);
        pageUtil.setPage(page);
        pageUtil.setTotalCount(totalCount > 0 ? totalCount : 1);
        model.addAttribute("pageMaker", pageUtil);
        model.addAttribute("crtPage", page);
        return "RecordLog";
    }

    @PostMapping(value = "/record/frmlog")
    public String FrmLogAdd(HttpServletRequest request, @RequestParam String contents
            , @AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {
        if (userDetails != null) {
            if (contents.length() > 50) {
                contents = contents.substring(0, 50);
            }
            recordLogService.addLog(new RecordLogDTO(0, contents, userDetails.getEmail(), userDetails.getUsername(), CurrentTime.getTime()));
        }
        return "redirect:" + request.getHeader("referer");
    }

    @PostMapping(value = "/record/frmlog_delete")
    public String FrmLogDelete(@AuthenticationPrincipal CustomUserDetails userDetails, HttpServletResponse response
            , HttpServletRequest request, @RequestParam int id, @RequestParam String email) throws Exception {
        if (userDetails.getEmail().equals(email)) {
            recordLogService.deleteLog(id, email);
        } else {
            ScriptUtils.alertAndBackPage(response, "[오류] 권한이 없습니다.");
        }
        return "redirect:" + request.getHeader("referer");
    }
}
