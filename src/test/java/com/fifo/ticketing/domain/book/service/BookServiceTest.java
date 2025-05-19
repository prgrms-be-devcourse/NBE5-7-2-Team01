package com.fifo.ticketing.domain.book.service;

import static org.junit.jupiter.api.Assertions.*;

import com.fifo.ticketing.domain.book.dto.BookCompleteDto;
import com.fifo.ticketing.domain.book.entity.Book;
import com.fifo.ticketing.domain.book.mapper.BookMapper;
import com.fifo.ticketing.domain.book.repository.BookRepository;
import com.fifo.ticketing.domain.book.repository.BookSeatRepository;
import com.fifo.ticketing.domain.performance.dto.PerformanceRequestDto;
import com.fifo.ticketing.domain.performance.entity.Category;
import com.fifo.ticketing.domain.performance.entity.Performance;
import com.fifo.ticketing.domain.performance.entity.Place;
import com.fifo.ticketing.domain.performance.mapper.PerformanceMapper;
import com.fifo.ticketing.domain.performance.repository.PerformanceRepository;
import com.fifo.ticketing.domain.seat.service.SeatService;
import com.fifo.ticketing.domain.user.repository.UserRepository;
import com.fifo.ticketing.global.entity.File;
import com.fifo.ticketing.global.exception.ErrorCode;
import com.fifo.ticketing.global.exception.ErrorException;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import com.fifo.ticketing.domain.user.entity.User;
import static org.mockito.BDDMockito.*;


@ActiveProfiles("ci")
@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PerformanceRepository performanceRepository;
    @Mock
    private SeatService seatService;
    @Mock
    private BookSeatRepository bookSeatRepository;
    @Mock
    private BookScheduleManager bookScheduleManager;
    private Place place;
    private Performance mockPerformance;
    private User mockUser;
    private Book mockBook;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(bookService, "urlPrefix", "https://picsum.photos/200");

        mockUser = User.builder()
            .id(1L)
            .email("example@gmail.com")
            .password("123")
            .username("테스트 유저")
            .build();

        place = new Place(1L, "서울특별시 서초구 서초동 1307", "강남아트홀", 100);
        File mockFile = File.builder()
            .id(1L)
            .encodedFileName("poster.jpg")
            .originalFileName("sample.jpg")
            .build();

        mockPerformance = new Performance(
            1L,
            "라따뚜이",
            "라따뚜이는 픽사의 영화입니다.",
            place,
            LocalDateTime.of(2025, 6, 1, 19, 0),
            LocalDateTime.of(2025, 6, 1, 21, 0),
            Category.MOVIE,
            false,
            false,
            LocalDateTime.of(2025, 5, 12, 19, 0),
            mockFile
        );



        mockBook = Book.create(mockUser, mockPerformance, 20000, 2);
    }

    @Test
    @DisplayName("getBookCompleteInfo_성공 테스트")
    void getBookCompleteInfo_success() throws Exception {
        // given
        Long bookId = 1L;
        given(bookRepository.findById(bookId)).willReturn(Optional.of(mockBook));

        // when
        BookCompleteDto result = bookService.getBookCompleteInfo(bookId);

        // then
        assertNotNull(result);
        assertEquals(mockBook.getPerformance().getId(), result.getPerformanceId());
        assertEquals(mockBook.getPerformance().getTitle(), result.getPerformanceTitle());
        assertEquals(mockBook.getTotalPrice(), result.getTotalPrice());
        assertEquals(mockBook.getQuantity(), result.getQuantity());
        assertEquals("https://picsum.photos/200/" + result.getEncodedFileName(), result.getUrl());
    }

    @Test
    @DisplayName("getBookCompleteInfo_실패")
    void getBookCompleteInfo_fail_notFound() throws Exception {
        // given
        Long bookId = 10L;
        given(bookRepository.findById(bookId)).willReturn(Optional.empty());

        // when & then
        ErrorException exception = assertThrows(ErrorException.class, () -> {
            bookService.getBookCompleteInfo(bookId);
        });

        assertEquals(ErrorCode.NOT_FOUND_BOOK, exception.getErrorCode());
        verify(bookRepository).findById(bookId);
    }
}