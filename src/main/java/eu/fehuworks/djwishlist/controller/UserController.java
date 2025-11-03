package eu.fehuworks.djwishlist.controller;

import static eu.fehuworks.djwishlist.controller.UserController.PATH;

import eu.fehuworks.djwishlist.model.User;
import eu.fehuworks.djwishlist.service.UserService;
import eu.fehuworks.djwishlist.service.WishlistService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping(PATH)
public class UserController extends AbstractMvcController {

  static final String PATH = "/user";

  private final UserService userService;
  private final WishlistService wishlistService;

  @Autowired
  public UserController(UserService userService, WishlistService wishlistService) {
    super(PATH);
    this.userService = userService;
    this.wishlistService = wishlistService;
  }

  @GetMapping
  public String openUserCreationPage(Model model, HttpSession session) {
    model.addAttribute("user", userService.getUser(session.getId()));
    return "user";
  }

  @PostMapping
  public String addUser(@ModelAttribute User user, HttpSession session) {
    userService.add(session.getId(), user.getName());
    wishlistService.updateUsernames(session.getId());
    return super.redirectTo(GuestController.PATH);
  }
}
