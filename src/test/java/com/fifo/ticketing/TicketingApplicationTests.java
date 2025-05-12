package com.fifo.ticketing;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@ActiveProfiles("ci")
class TicketingApplicationTests {

    @Test
    void contextLoads() {
    }

}
