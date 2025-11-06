package eu.fehuworks.djwishlist.controller;

import static eu.fehuworks.djwishlist.controller.GuestController.PATH;

import eu.fehuworks.djwishlist.model.User;
import eu.fehuworks.djwishlist.model.Vote;
import eu.fehuworks.djwishlist.model.Wish;
import eu.fehuworks.djwishlist.service.UserService;
import eu.fehuworks.djwishlist.service.WishlistService;
import jakarta.servlet.http.HttpSession;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping(PATH)
public class GuestController extends AbstractMvcController {

  static final String PATH = "/";

  private final WishlistService wishlistService;
  private final UserService userService;

  @Autowired
  public GuestController(WishlistService wishlistService, UserService userService) {
    super(PATH);
    this.wishlistService = wishlistService;
    this.userService = userService;
  }

  @GetMapping
  public String openStartPage(Model model, HttpSession session) {
    if (!userService.isUserKnown(session.getId())) {
      return super.redirectTo(UserController.PATH);
    }
    User user = userService.getUser(session.getId());
    var wishlist =
        wishlistService.getWishes().entrySet().stream()
            .sorted(Map.Entry.<Wish, Vote>comparingByValue().reversed())
            .collect(
                Collectors.toMap(
                    Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    model.addAttribute("wishlist", wishlist);
    Wish wish = new Wish();
    wish.setIssuer(user.getName());
    model.addAttribute("newWish", wish);
    model.addAttribute("sessionId", session.getId());
    model.addAttribute("user", user);
    model.addAttribute("isAdmin", user.isAdmin());
    return "start";
  }

  @PostMapping("/upvote/{id}")
  public String upvote(@PathVariable("id") UUID id, HttpSession session) {
    log.info("Upvoting {} as {}", id.toString(), session.getId());
    wishlistService.upvote(id, session.getId());
    return super.reloadPage();
  }

  @GetMapping("/downvote/{id}")
  public String downvote(@PathVariable("id") UUID id, HttpSession session) {
    log.info("Downvoting {} as {}", id.toString(), session.getId());
    wishlistService.downvote(id, session.getId());
    return super.reloadPage();
  }

  @PostMapping
  public String addWish(@ModelAttribute Wish wish, HttpSession session) {
    userService.add(session.getId(), wish.getIssuer());
    wish.setId(UUID.randomUUID());
    wish.setIssuerId(session.getId());
    log.info("saving wish: {} as {}", wish, session.getId());
    wishlistService.addWish(wish);
    wishlistService.updateUsernames(session.getId());
    return super.reloadPage();
  }

  @GetMapping("/delete/{id}")
  public String delete(@PathVariable("id") UUID id, HttpSession session) {
    log.info("deleting {} as {}", id, session.getId());
    wishlistService.removeWish(id, userService.getUser(session.getId()));
    return super.reloadPage();
  }
}
