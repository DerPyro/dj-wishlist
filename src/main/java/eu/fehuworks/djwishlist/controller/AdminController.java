package eu.fehuworks.djwishlist.controller;

import eu.fehuworks.djwishlist.model.AdminUser;
import eu.fehuworks.djwishlist.service.AdminService;
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
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

  private final AdminService adminService;

  @GetMapping
  public String showAdminPage(Model model) {
    log.info("Accessing admin page");
    model.addAttribute("adminUser", new AdminUser());
    return "admin";
  }

  @PostMapping
  public String handleAdminLogin(@ModelAttribute AdminUser adminUser, HttpSession session) {
    log.info("Handling admin login");
    if (adminService.authenticate(
        adminUser.getUsername(), adminUser.getPassword(), session.getId())) {
      log.info("Admin authenticated successfully");
      return "redirect:/";
    }
    log.warn("Admin authentication failed");
    return "redirect:/admin";
  }
}
