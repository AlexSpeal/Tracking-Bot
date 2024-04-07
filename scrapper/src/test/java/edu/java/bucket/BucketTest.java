package edu.java.bucket;

import edu.java.scrapper.IntegrationTest;
import edu.java.servises.interfaces.LinkService;
import edu.java.servises.jdbc.JdbcTgChatService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.mockito.Mockito.when;

@DirtiesContext
@SpringBootTest
@AutoConfigureMockMvc
public class BucketTest {
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
