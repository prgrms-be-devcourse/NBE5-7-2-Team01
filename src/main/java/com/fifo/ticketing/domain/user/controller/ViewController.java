package com.fifo.ticketing.domain.user.controller;

import com.fifo.ticketing.domain.book.dto.BookedView;
import com.fifo.ticketing.domain.book.service.BookService;
import com.fifo.ticketing.domain.user.dto.SessionUser;
import com.fifo.ticketing.domain.user.dto.form.SignUpForm;
import com.fifo.ticketing.domain.user.service.UserFormService;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ViewController {

    private final UserFormService userFormService;
    private final BookService bookService;

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
        return "user/sign_up";
    }

    @PostMapping("/users/signup")
    public String doSignup(SignUpForm signUpForm, HttpSession session, Model model) {

        String emailVerified = (String) session.getAttribute("emailVerified");
        if (emailVerified == null || !emailVerified.equals(signUpForm.email())) {
            model.addAttribute("emailVerified", signUpForm.email());
            return "user/sign_up";
        }

        userFormService.save(signUpForm);
        session.removeAttribute("emailVerified");

        return "redirect:/users/signin?signupSuccess=true";
    }

    @GetMapping("/users/signin")
    public String signin() {
        return "user/sign_in";
    }

    @GetMapping("/users/books")
    public String getBookList(HttpSession session, Model model) {
        SessionUser loginUser = (SessionUser) session.getAttribute("loginUser");
        List<BookedView> bookedList = bookService.getBookedList(loginUser.id());

        model.addAttribute("bookedList", bookedList);
        return "user/bookList";
    }

    @GetMapping("/users/books/{bookId}")
    public String getBookDetail(HttpSession session,
        @PathVariable Long bookId,
        Model model) {
        SessionUser loginUser = (SessionUser) session.getAttribute("loginUser");
        BookedView bookDetail = bookService.getBookDetail(loginUser.id(), bookId);

        model.addAttribute("bookDetail", bookDetail);
        model.addAttribute("userName", loginUser.username());

        return "book/detail";
    }

    @GetMapping("/users")
    public String myPage(HttpSession session, Model model) {
        SessionUser loginUser = (SessionUser) session.getAttribute("loginUser");
        if (loginUser != null) {
            model.addAttribute("username", loginUser.username());
        }
        return "user/my_page";
    }

}
