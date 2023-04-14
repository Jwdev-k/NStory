package nk.service.NStory.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import nk.service.NStory.dto.SecurityCodeDTO;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class SecurityCode {

    public String generateSecurityCode() {
        Random random = new Random();
        int code = 10000 + random.nextInt(90000);
        return Integer.toString(code);
    }

    public void saveSecurityCode(String email, String code, HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute("SecurityCode+" + email, new SecurityCodeDTO(code, System.currentTimeMillis()));
    }

    public SecurityCodeDTO getSecurityCode(String email, HttpServletRequest request) {
        HttpSession session = request.getSession();
        return (SecurityCodeDTO) session.getAttribute("SecurityCode+" + email);
    }

    public void deleteSecurityCode(String email, HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute("SecurityCode+" + email);
    }
}
