package eu.fehuworks.djwishlist.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

class SseEmitterFactoryTest {

  private final SseEmitterFactory sut = new SseEmitterFactory();

  @Test
  void shouldReturnSseEmitterWithTimeoutOfLongMaxValueWhenCallingCreateSseEmitter() {
    SseEmitter result = sut.createSseEmitter();

    assertEquals(Long.MAX_VALUE, result.getTimeout());
  }

  @Test
  void shouldReturnDifferentSseEmittersWhenCallingCreateSseEmitterMultipleTimes() {
    assertNotEquals(sut.createSseEmitter(), sut.createSseEmitter());
  }
}
