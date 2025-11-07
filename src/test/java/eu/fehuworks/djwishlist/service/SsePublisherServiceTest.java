package eu.fehuworks.djwishlist.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@ExtendWith(MockitoExtension.class)
class SsePublisherServiceTest {

  private SsePublisherService sut;
  @Mock private SseEmitterFactory sseEmitterFactory;

  @BeforeEach
  void setUp() {
    when(sseEmitterFactory.createSseEmitter()).thenReturn(spy(SseEmitter.class));
    sut = new SsePublisherService(sseEmitterFactory);
  }

  @Test
  void createSseEmitter_returns_created_sseEmitter() {
    SseEmitter expected = spy(SseEmitter.class);
    when(sseEmitterFactory.createSseEmitter()).thenReturn(expected);

    SseEmitter result = sut.createSseEmitter(UUID.randomUUID().toString());

    assertEquals(expected, result);
  }

  @Test
  void sendTo_sends_given_data_only_to_needed_emitter() throws Exception {
    SseEmitter expected = spy(SseEmitter.class);
    SseEmitter notThisOne = spy(SseEmitter.class);
    when(sseEmitterFactory.createSseEmitter()).thenReturn(expected, notThisOne);
    String sessionId = "sessionId-" + UUID.randomUUID();
    UUID expectedData = UUID.randomUUID();

    sut.createSseEmitter(sessionId);
    sut.createSseEmitter("otherSessionId-" + UUID.randomUUID());

    sut.sendTo(sessionId, expectedData);

    verify(expected).send(expectedData);
    verify(notThisOne, never()).send(any(Object.class));
  }

  @Test
  void sendToAll_sends_given_data_to_all_emitters() throws Exception {
    SseEmitter emitter1 = spy(SseEmitter.class);
    SseEmitter emitter2 = spy(SseEmitter.class);
    when(sseEmitterFactory.createSseEmitter()).thenReturn(emitter1, emitter2);
    UUID expectedData = UUID.randomUUID();

    sut.createSseEmitter("firstSessionId");
    sut.createSseEmitter("secondSessionId");

    sut.sendToAll(expectedData);

    verify(emitter1).send(expectedData);
    verify(emitter2).send(expectedData);
  }
}
