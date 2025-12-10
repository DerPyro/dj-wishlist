package eu.fehuworks.djwishlist.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
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

  @Test
  void sendTo_does_not_send_anything_if_emitter_not_found() {
    String sessionId = "nonExistingSessionId";
    UUID data = UUID.randomUUID();

    assertDoesNotThrow(() -> sut.sendTo(sessionId, data));
    verify(sseEmitterFactory, never()).createSseEmitter();
  }

  @Test
  void sendTo_removes_emitter_if_sending_raises_exception() throws Exception {
    SseEmitter faultyEmitter = spy(SseEmitter.class);
    UUID data = UUID.randomUUID();
    doThrow(new IOException("simulated Exception")).when(faultyEmitter).send(data);
    when(sseEmitterFactory.createSseEmitter()).thenReturn(faultyEmitter);
    String sessionId = "faultySessionId";

    sut.createSseEmitter(sessionId);
    sut.sendTo(sessionId, data);

    assertDoesNotThrow(() -> sut.sendTo(sessionId, data));
    verify(faultyEmitter).send(data);
  }

  @Test
  void createSseEmitter_returns_existing_emitter_if_already_created() {
    SseEmitter firstEmitter = spy(SseEmitter.class);
    SseEmitter secondEmitter = spy(SseEmitter.class);
    assertNotEquals(firstEmitter, secondEmitter);
    when(sseEmitterFactory.createSseEmitter()).thenReturn(firstEmitter, secondEmitter);
    String sessionId = "existingSessionId";

    SseEmitter emitter1 = sut.createSseEmitter(sessionId);
    SseEmitter emitter2 = sut.createSseEmitter(sessionId);

    assertEquals(emitter1, emitter2);
    verify(sseEmitterFactory).createSseEmitter();
  }
}
