package eu.fehuworks.djwishlist.service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Service
@RequiredArgsConstructor
public class SsePublisherService {

  private final SseEmitterFactory sseEmitterFactory;
  private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

  public SseEmitter createSseEmitter(String sessionId) {
    SseEmitter sseEmitter = sseEmitterFactory.createSseEmitter();
    sseEmitter.onCompletion(
        () -> {
          log.info(
              "Removing SSE-Emitter from User with SessionID '{}' because of completion",
              sessionId);
          emitters.remove(sessionId);
        });
    sseEmitter.onTimeout(
        () -> {
          log.warn(
              "Removing SSE-Emitter from User with SessionID '{}' because of timeout", sessionId);
          emitters.remove(sessionId);
        });
    sseEmitter.onError(
        ex -> {
          log.warn(
              "Removing SSE-Emitter from User with SessionID '{}' because of error: {}",
              sessionId,
              ex.getMessage());
          log.trace(ex.getMessage(), ex);
          emitters.remove(sessionId);
        });
    SseEmitter alreadyExisting = emitters.putIfAbsent(sessionId, sseEmitter);
    if (alreadyExisting == null) {
      log.info("Created new SSE-Emitter for User with SessionID '{}'", sessionId);
      return sseEmitter;
    } else {
      log.debug(
          "Did not create new SSE-Emitter for User with SessionID '{}' because there was already a emitter",
          sessionId);
      return alreadyExisting;
    }
  }

  public void sendToAll(Object data) {
    emitters.keySet().forEach(sessionId -> sendTo(sessionId, data));
  }

  public void sendTo(String sessionId, Object data) {
    SseEmitter sseEmitter = emitters.get(sessionId);
    if (sseEmitter == null) {
      log.warn("No SSE-Emitter for SessionID '{}' found", sessionId);
    } else {
      try {
        sseEmitter.send(data);
        log.debug("Sent '{}' to SessionID '{}'", data, sessionId);
      } catch (Throwable e) {
        log.error(
            "Sending '{}' to SessionID '{}' raised an error: {}", data, sessionId, e.getMessage());
        log.trace(e.getMessage(), e);
      }
    }
  }
}
