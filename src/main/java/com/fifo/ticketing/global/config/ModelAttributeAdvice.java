package com.fifo.ticketing.global.config;

import com.fifo.ticketing.domain.user.dto.SessionUser;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class ModelAttributeAdvice {
    @ModelAttribute
    public void addUserIdToModel(HttpSession session, Model model) {
        SessionUser loginUser = (SessionUser) session.getAttribute("loginUser");
        if (loginUser != null) {
            model.addAttribute("userId", loginUser.id());
            model.addAttribute("userRole", loginUser.role());
        }
    }
}
