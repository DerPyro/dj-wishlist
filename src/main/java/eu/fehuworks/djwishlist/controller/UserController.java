package eu.fehuworks.djwishlist.controller;

import eu.fehuworks.djwishlist.model.User;
import eu.fehuworks.djwishlist.service.UserService;
import eu.fehuworks.djwishlist.service.WishlistService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;
  private final WishlistService wishlistService;

  @GetMapping
  public String openUserCreationPage(Model model, HttpSession session) {
    model.addAttribute("user", userService.getUser(session.getId()));
    return "user";
  }

  @PostMapping
  public String addUser(@ModelAttribute User user, HttpSession session) {
    userService.add(session.getId(), user.getName());
    wishlistService.updateUsernames(session.getId());
    return "redirect:/";
  }
}
