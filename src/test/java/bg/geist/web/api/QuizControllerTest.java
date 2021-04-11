package bg.geist.web.api;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class QuizControllerTest {
    private static final String INDEX_FOUND = "1";
    private static final String INDEX_NOT_FOUND = "111";
    private static final String INDEX_BAD_INDEX = "---";

    private static final String CERTIFICATION = "/certification";
    private static final String INDEX_WITH_CERTIFICATION = "9";
    private static final String INDEX_WITHOUT_CERTIFICATION = "1";

    private final static String JSON_ANSWERS_WRONG = "{\"answers\":[0, 0, 0, 0, 0, 0, 0]}";
    private final static String JSON_ANSWERS_RIGHT = "{\"answers\":[4, 4, 2, 8, 1, 8, 8]}";
    private final static String JSON_ANSWERS_MIX = "{\"answers\":[4, 4, 2, 0, 0, 0, 0]}";
    private final static String JSON_RESULTS_WRONG = "{\"correct\":[4,4,2,8,1,8,8],\"right\":0,\"wrong\":7}";
    private final static String JSON_RESULTS_RIGHT = "{\"correct\":[4,4,2,8,1,8,8],\"right\":7,\"wrong\":0}";
    private final static String JSON_RESULTS_MIX = "{\"correct\":[4,4,2,8,1,8,8],\"right\":3,\"wrong\":4}";


    @Autowired
    private MockMvc mockMvc;


    @Test
    void getIndexReturnsOk() throws Exception {
        mockMvc.perform(get("/api/quiz")).andExpect(status().isOk());
    }

    @Test
    void getQuizReturnsOk() throws Exception {
        mockMvc.perform(get("/api/quiz/" + INDEX_FOUND)).andExpect(status().isOk());
    }

    @Test
    void getQuizReturnsStatus404() throws Exception {
        mockMvc.perform(get("/api/quiz/" + INDEX_NOT_FOUND)).andExpect(status().isNotFound());
    }

    @Test
    void getQuizReturnsStatus400() throws Exception {
        mockMvc.perform(get("/api/quiz/" + INDEX_BAD_INDEX)).andExpect(status().isBadRequest());
    }

    @Test
    void certificationWithGetRequestReturnsMethodNotAllowed() throws Exception {
        mockMvc.perform(get("/api/quiz/" + INDEX_WITH_CERTIFICATION + CERTIFICATION))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void certificationWithIndexWithNoCertificationReturns400() throws Exception {
        mockMvc.perform(post("/api/quiz/" + INDEX_WITHOUT_CERTIFICATION + CERTIFICATION))
                .andExpect(status().isBadRequest());
    }

    @Test
    void certificationWithoutResultsReturns400() throws Exception {
        mockMvc.perform(post("/api/quiz/" + INDEX_WITH_CERTIFICATION + CERTIFICATION))
                .andExpect(status().isBadRequest());
    }

    @Test
    void certificationWithResultsReturnStatus202() throws Exception {
        mockMvc.perform(post("/api/quiz/" + INDEX_WITH_CERTIFICATION + CERTIFICATION)
                .content(JSON_ANSWERS_WRONG)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isAccepted());
    }

    @Test
    void certificationWithWrongResults() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/quiz/" + INDEX_WITH_CERTIFICATION + CERTIFICATION)
                .content(JSON_ANSWERS_WRONG)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isAccepted())
                .andReturn();
        String jsonResults = result.getResponse().getContentAsString();
        System.out.println(Assertions.assertThat(jsonResults).isEqualTo(JSON_RESULTS_WRONG));
    }

    @Test
    void certificationWithRightResults() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/quiz/" + INDEX_WITH_CERTIFICATION + CERTIFICATION)
                .content(JSON_ANSWERS_RIGHT)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isAccepted())
                .andReturn();
        String jsonResults = result.getResponse().getContentAsString();
        System.out.println(Assertions.assertThat(jsonResults).isEqualTo(JSON_RESULTS_RIGHT));
    }

    @Test
    void certificationWithMixResults() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/quiz/" + INDEX_WITH_CERTIFICATION + CERTIFICATION)
                .content(JSON_ANSWERS_MIX)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isAccepted())
                .andReturn();
        String jsonResults = result.getResponse().getContentAsString();
        System.out.println(Assertions.assertThat(jsonResults).isEqualTo(JSON_RESULTS_MIX));
    }
}