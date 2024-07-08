package eu.fehuworks.djwishlist.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/")
public class GuestController {

	@GetMapping
	public String openStartPage() {
		return "start";
	}

}
