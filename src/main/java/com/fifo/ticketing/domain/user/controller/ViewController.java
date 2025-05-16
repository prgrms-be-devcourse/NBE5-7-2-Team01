package com.fifo.ticketing.domain.user.controller;

import com.fifo.ticketing.domain.book.dto.BookedView;
import com.fifo.ticketing.domain.book.entity.BookStatus;
import com.fifo.ticketing.domain.book.service.BookService;
import com.fifo.ticketing.domain.user.dto.SessionUser;
import com.fifo.ticketing.domain.user.dto.form.SignUpForm;
import com.fifo.ticketing.domain.user.service.UserFormService;
import com.fifo.ticketing.global.util.UserValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String signup(HttpServletRequest request) {
        SessionUser loginUser = (SessionUser) request.getSession().getAttribute("loginUser");
        if (loginUser != null) {
            return "redirect:/";
        }
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
    public String signin(HttpServletRequest request, Model model) {
        SessionUser loginUser = (SessionUser) request.getSession().getAttribute("loginUser");
        if (loginUser != null) {
            return "redirect:/";
        }
        String errormessage = (String) request.getSession().getAttribute("errormessage");
        if (errormessage != null) {
            model.addAttribute("errorMessage", errormessage);
            request.getSession().removeAttribute("errormessage");
        }

        return "user/sign_in";
    }

    @GetMapping("/users/books")
    public String getBookList(HttpSession session,
        @RequestParam(value = "page", defaultValue = "0", required = false) int page,
        @RequestParam(value = "size", defaultValue = "3", required = false) int size,
        @RequestParam(required = false) String performanceTitle,
        @RequestParam(required = false) BookStatus bookStatus,
        Model model
    ) {
        SessionUser loginUser = UserValidator.validateSessionUser(session);
        PageRequest pageable = PageRequest.of(page, size);
        Page<BookedView> bookedList = bookService.getBookedList(loginUser.id(), performanceTitle,
            bookStatus, pageable);

        model.addAttribute("bookedList", bookedList);
        return "user/bookList";
    }

    @GetMapping("/users/books/{bookId}")
    public String getBookDetail(HttpSession session,
        @PathVariable Long bookId,
        Model model) {
        SessionUser loginUser = UserValidator.validateSessionUser(session);
        BookedView bookDetail = bookService.getBookDetail(loginUser.id(), bookId);

        model.addAttribute("bookDetail", bookDetail);
        model.addAttribute("userName", loginUser.username());

        return "book/detail";
    }


    @DeleteMapping("/users/books/{bookId}")
    public String cancelBook(
        HttpSession session,
        @PathVariable Long bookId,
        RedirectAttributes redirectAttributes
    ) {
        SessionUser loginUser = UserValidator.validateSessionUser(session);

        bookService.cancelBook(bookId, loginUser.id());
        redirectAttributes.addFlashAttribute("alertMessage", "예매가 성공적으로 취소되었습니다.");
        return "redirect:/users/books";
    }

    @GetMapping("/users")
    public String myPage(HttpSession session, Model model) {
        SessionUser loginUser = UserValidator.validateSessionUser(session);

        model.addAttribute("username", loginUser.username());
        return "user/my_page";
    }

    @GetMapping("/admin/users")
    public String adminPage(HttpSession session, Model model) {
        UserValidator.validateSessionUser(session);
        return "admin/manage_admin";
    }

}
