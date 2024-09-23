package com.example.demo;


import static org.hamcrest.Matchers.equalTo;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.removeHeaders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.actuate.observability.AutoConfigureObservability;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@AutoConfigureRestDocs(outputDir = "target/snippets")
@AutoConfigureMockMvc
@AutoConfigureObservability
class DemoControllerTestIT {

    private static final String USER_UUID = UUID.randomUUID().toString();
    private static final String[] IGNORE_HEADERS = new String[]{"X-Content-Type-Options",
            "X-XSS-Protection", "Cache-Control", "Pragma", "Expires", "Strict-Transport-Security", "X-Frame-Options",
            "X-Application-Context", "Content-Length", "Transfer-Encoding", "Server"};

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setup(RestDocumentationContextProvider restDocumentationContextProvider) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentationContextProvider)
                        .operationPreprocessors()
                        .withRequestDefaults(prettyPrint(), removeHeaders(IGNORE_HEADERS))
                        .withResponseDefaults(prettyPrint(), removeHeaders(IGNORE_HEADERS)))
                .build();
    }

    @Test
    void shouldPropagateRemoteBaggageField() throws Exception {
        this.mockMvc.perform(get("/v1/demo")
                        .header("user-uuid", USER_UUID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(USER_UUID)));
    }

}
