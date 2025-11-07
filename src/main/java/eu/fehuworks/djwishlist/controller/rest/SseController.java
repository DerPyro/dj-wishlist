package eu.fehuworks.djwishlist.controller.rest;

import eu.fehuworks.djwishlist.service.SsePublisherService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/sse")
@RequiredArgsConstructor
public class SseController {

  private final SsePublisherService ssePublisherService;

  @GetMapping
  public SseEmitter subscribeToSee(HttpSession session) {
    return ssePublisherService.createSseEmitter(session.getId());
  }
}
