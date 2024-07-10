package eu.fehuworks.djwishlist.controller;

import eu.fehuworks.djwishlist.model.Wish;
import eu.fehuworks.djwishlist.service.WishlistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class GuestController {

	private final WishlistService wishlistService;

	@GetMapping
	public String openStartPage(Model model) {
		model.addAttribute("wishlist", wishlistService.getWishes());
		return "start";
	}

	@PostMapping("/upvote/{id}")
	public String upvote(@PathVariable("id") UUID id) {
	  log.info("Upvoting " + id.toString());
		wishlistService.upvote(id);
		return "redirect:/";
	}

	@PostMapping
	public String addWish(@ModelAttribute Wish wish) {
		wish.setId(UUID.randomUUID());
		wishlistService.addWish(wish);
		return "redirect:/";

	}

}
