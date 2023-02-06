package nk.service.NStory.controller;

import lombok.extern.slf4j.Slf4j;
import nk.service.NStory.dto.RecordLogDTO;
import nk.service.NStory.security.CustomUserDetails;
import nk.service.NStory.service.impl.RecordLogService;
import nk.service.NStory.utils.CurrentTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
public class MainController {
    @Autowired
    private RecordLogService recordLogService;

    @RequestMapping(value = "/")
    public String main(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model) throws Exception {
        boolean isLogin = customUserDetails != null;
        model.addAttribute("IsLogin", isLogin);
        model.addAttribute("recordLogList", recordLogService.recordLogList());
        return "Main";
    }

    @PostMapping(value = "/frmlog")
    public String FrmLogAdd(@RequestParam String contents
            , @AuthenticationPrincipal CustomUserDetails customUserDetails) throws Exception {
        if (customUserDetails != null) {
            recordLogService.addLog(new RecordLogDTO(0, contents, customUserDetails.getUsername(), CurrentTime.getTime()));
        }
        return "redirect:/";
    }
}
