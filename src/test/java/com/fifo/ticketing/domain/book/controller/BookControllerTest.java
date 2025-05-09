package com.fifo.ticketing.domain.book.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fifo.ticketing.domain.book.dto.BookCreateRequest;
import com.fifo.ticketing.global.config.TestSecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    @DisplayName("예매 생성 테스트")
    void book_create_test() throws Exception {
        // given
        Long userId = 1L;

        BookCreateRequest request = BookCreateRequest.builder()
                .performanceId(1L)
                .seatIds(List.of(1L, 2L))
                .build();

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/book")
                        .param("userId", String.valueOf(userId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.bookId").exists())
                .andDo(print());

    }
}