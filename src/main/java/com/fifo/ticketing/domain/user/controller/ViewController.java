package com.fifo.ticketing.domain.user.controller;

import com.fifo.ticketing.domain.user.dto.SessionUser;
import com.fifo.ticketing.domain.user.dto.form.SignUpForm;
import com.fifo.ticketing.domain.user.service.UserFormService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ViewController {

    private final UserFormService userFormService;

    @GetMapping("/")
    public String homePage(HttpSession session, Model model) {
        SessionUser loginUser = (SessionUser) session.getAttribute("loginUser");
        if (loginUser != null) {
            model.addAttribute("username", loginUser.username());
        }
        return "index";
    }

    @GetMapping("/users/signup")
    public String signup() {
        return "sign_up";
    }

    @PostMapping("/users/signup")
    public String doSignup(SignUpForm signUpForm, HttpSession session, Model model) {

        String emailVerified = (String) session.getAttribute("emailVerified");
        if (emailVerified == null || !emailVerified.equals(signUpForm.email())) {
            model.addAttribute("emailVerified", signUpForm.email());
            return "sign_up";
        }

        userFormService.save(signUpForm);
        session.removeAttribute("emailVerified");

        return "redirect:/users/signin?signupSuccess=true";
    }

    @GetMapping("/users/signin")
    public String signin() {
        return "sign_in";
    }

}
