package com.merchant.checkout;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestExternalClients.class)
class CheckoutPlatformApplicationTests {

    @Test
    void contextLoads() {
    }
}
