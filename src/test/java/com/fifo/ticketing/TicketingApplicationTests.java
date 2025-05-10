package com.fifo.ticketing;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@ActiveProfiles("ci")
@TestPropertySource(locations = "classpath:application-ci.yml")
@Sql(scripts = "classpath:data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class TicketingApplicationTests {

    @Test
    void contextLoads() {
    }

}
