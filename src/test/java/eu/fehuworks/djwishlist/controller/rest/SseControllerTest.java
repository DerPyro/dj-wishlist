package eu.fehuworks.djwishlist.controller.rest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import eu.fehuworks.djwishlist.service.SsePublisherService;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = SseController.class)
class SseControllerTest {

  @Autowired private MockMvc mockMvc;
  @MockitoBean private SsePublisherService ssePublisherService;

  @Test
  void subscribeToSee_returns_status_ok() throws Exception {
    mockMvc.perform(get("/sse")).andExpect(status().isOk());
  }

  @Test
  void subscribeToSse_calls_ssePublisherService_with_given_sessionId() throws Exception {
    MockHttpSession httpSession = createHttpSession();

    mockMvc.perform(get("/sse").session(httpSession));

    verify(ssePublisherService).createSseEmitter(httpSession.getId());
  }

  private static MockHttpSession createHttpSession() {
    return new MockHttpSession(null, "sessionId-" + UUID.randomUUID());
  }
}
