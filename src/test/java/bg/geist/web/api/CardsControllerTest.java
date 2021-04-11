package bg.geist.web.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CardsControllerTest {
    private static final String INDEX_FOUND = "1";
    private static final String INDEX_NOT_FOUND = "111";
    private static final String INDEX_BAD_INDEX = "---";

    @Autowired
    private MockMvc mockMvc;


    @Test
    void getIndexReturnsOk() throws Exception {
        mockMvc.perform(get("/api/cards")).andExpect(status().isOk());
    }

    @Test
    void getModelReturnsOk() throws Exception {
        mockMvc.perform(get("/api/cards/" + INDEX_FOUND)).andExpect(status().isOk());
    }

    @Test
    void getModelReturnsStatus404() throws Exception {
        mockMvc.perform(get("/api/cards/" + INDEX_NOT_FOUND)).andExpect(status().isNotFound());
    }

    @Test
    void getModelReturnsStatus400() throws Exception {
        mockMvc.perform(get("/api/cards/" + INDEX_BAD_INDEX)).andExpect(status().isBadRequest());
    }
}