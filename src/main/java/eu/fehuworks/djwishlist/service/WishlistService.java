package eu.fehuworks.djwishlist.service;

import eu.fehuworks.djwishlist.model.Wish;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WishlistService {

	@Getter
	private final Map<Wish, Integer> wishes = new ConcurrentHashMap<>();

	public boolean addWish(Wish wish) {
		wishes.put(wish, 0);
		return true;
	}

	public boolean removeWish(UUID id) {
		var wish = wishes.entrySet().stream().filter(w -> w.getKey().getId().equals(id)).findFirst();
		if (wish.isPresent()) {
			wishes.remove(wish.get().getKey());
			return true;
		}
		return false;
	}

	public boolean upvote(UUID id) {
		var wish = wishes.entrySet().stream().filter(w -> w.getKey().getId().equals(id)).findFirst();
		if (wish.isPresent()) {
			wishes.put(wish.get().getKey(), wish.get().getValue() + 1);
			return true;
		}
		return false;
	}


}
