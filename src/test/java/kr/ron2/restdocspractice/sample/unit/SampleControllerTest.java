package kr.ron2.restdocspractice.sample.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.ron2.restdocspractice.sample.SampleController;
import kr.ron2.restdocspractice.sample.SampleRequest;
import kr.ron2.restdocspractice.sample.SampleResponse;
import kr.ron2.restdocspractice.utils.ApiDocumentUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SampleController.class)
@AutoConfigureRestDocs
class SampleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getSampleResponse_mockMvc() throws Exception {
        SampleRequest requestBody = new SampleRequest(1L, "ron2");
        SampleResponse responseBody = SampleResponse.from(requestBody);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))

                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(responseBody)))
                .andDo(document("sample-test",
                        ApiDocumentUtils.getDocumentRequest(),
                        ApiDocumentUtils.getDocumentResponse(),
                        requestFields(fieldWithPath("id").type(JsonFieldType.NUMBER).description("아이디"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("이름")),

                        responseFields(fieldWithPath("id").type(JsonFieldType.NUMBER).description("아이디"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("이름"))
                ));

    }


}
