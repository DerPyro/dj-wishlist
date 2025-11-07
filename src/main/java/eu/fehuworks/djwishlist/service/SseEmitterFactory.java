package eu.fehuworks.djwishlist.service;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
public class SseEmitterFactory {

  public SseEmitter createSseEmitter() {
    return new SseEmitter(Long.MAX_VALUE);
  }
}
