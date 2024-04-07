package edu.java.bucket;

import edu.java.scrapper.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@DirtiesContext
@SpringBootTest
@AutoConfigureMockMvc
public class BucketTest extends IntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Value("${bucket4j.filters[0].rate-limits[0].bandwidths[0].capacity}")
    private int capacity;
    private static final String REGISTER_REQUEST = "/tg-chat/check-reg/10";

    @Test
    void bucketTest() throws Exception {

        for (int i = 0; i < capacity; ++i) {
            mockMvc.perform(MockMvcRequestBuilders.get(REGISTER_REQUEST))
                .andExpect(MockMvcResultMatchers.status().isOk());
        }
        mockMvc.perform(MockMvcRequestBuilders.get(REGISTER_REQUEST))
            .andExpect(MockMvcResultMatchers.status().isTooManyRequests());
    }
}
