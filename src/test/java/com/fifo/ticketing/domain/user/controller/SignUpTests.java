package com.fifo.ticketing.domain.user.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.fifo.ticketing.domain.user.dto.form.SignUpForm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@ActiveProfiles("ci")
@AutoConfigureMockMvc
class SignUpTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void signUpTest_Success() throws Exception {
        SignUpForm testForm = new SignUpForm(
            "test@test.com",
            "testUser",
            "1234"
        );

        mockMvc.perform(post("/users/signup")
                .sessionAttr("emailVerified", "test@test.com")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", testForm.username())
                .param("email", testForm.email())
                .param("password", testForm.password())
            ).andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/users/signin?signupSuccess=true"));
    }

    @Test
    void signUpTest_Failure_email_verified() throws Exception {
        SignUpForm wrongForm = new SignUpForm(
            "test@test.com",
            "testUser",
            "1234"
        );

        mockMvc.perform(post("/users/signup")
                .sessionAttr("emailVerified", "wrong@test.com")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", wrongForm.username())
                .param("email", wrongForm.email())
                .param("password", wrongForm.password())
            ).andExpect(status().isOk())
            .andExpect(view().name("user/sign_up"));
    }
}
