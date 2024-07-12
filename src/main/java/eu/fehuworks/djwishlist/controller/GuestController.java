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

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class GuestController {

	private final WishlistService wishlistService;

	@GetMapping
	public String openStartPage(Model model) {
		var wishlist = wishlistService.getWishes().entrySet().stream()
				.sorted(Map.Entry.<Wish, Integer>comparingByValue().reversed())
				.collect(Collectors.toMap(
						Map.Entry::getKey,
						Map.Entry::getValue,
						(e1, e2) -> e1,
						LinkedHashMap::new
				));
		model.addAttribute("wishlist", wishlist);
		model.addAttribute("newWish", new Wish());
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
		log.info("saving wish: " + wish);
		wishlistService.addWish(wish);
		return "redirect:/";
	}

}
