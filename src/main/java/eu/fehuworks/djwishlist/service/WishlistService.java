package eu.fehuworks.djwishlist.service;

import eu.fehuworks.djwishlist.model.User;
import eu.fehuworks.djwishlist.model.Vote;
import eu.fehuworks.djwishlist.model.Wish;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WishlistService {

  private final UserService userService;

  @Getter private final Map<Wish, Vote> wishes = new ConcurrentHashMap<>();

  public void addWish(Wish wish) {
    wishes.put(wish, new Vote(0, 0));
  }

  public void updateUsernames(String sessionId) {
    wishes.keySet().stream()
        .filter(wish -> wish.getIssuerId().equals(sessionId))
        .forEach(
            wish -> {
              String issuerName = userService.getUser(wish.getIssuerId()).getName();
              wish.setIssuer(issuerName);
            });
  }

  public boolean removeWish(UUID id, User user) {
    var wishEntry =
        wishes.entrySet().stream().filter(w -> w.getKey().getId().equals(id)).findFirst();
    if (wishEntry.isPresent()) {
      Wish wish = wishEntry.get().getKey();
      if (user.isAdmin() || wish.canBeDeletedBy(user.getSessionId())) {
        wishes.remove(wish);
        return true;
      }
    }
    return false;
  }

  public boolean upvote(UUID id, String upvoter) {
    var wishEntry =
        wishes.entrySet().stream().filter(entry -> entry.getKey().getId().equals(id)).findFirst();
    if (wishEntry.isPresent()) {
      Wish wish = wishEntry.get().getKey();
      wish.upvote(upvoter);
      wishes.put(wish, getVote(wish));
      return true;
    }
    return false;
  }

  private static Vote getVote(Wish wish) {
    return new Vote(
        (int)
            wish.getVotes().entrySet().stream()
                .filter(e -> e.getValue() == Wish.VoteType.UPVOTE)
                .count(),
        (int)
            wish.getVotes().entrySet().stream()
                .filter(e -> e.getValue() == Wish.VoteType.DOWNVOTE)
                .count());
  }

  public boolean downvote(UUID id, String downvoter) {
    var wishEntry =
        wishes.entrySet().stream().filter(w -> w.getKey().getId().equals(id)).findFirst();
    if (wishEntry.isPresent()) {
      Wish wish = wishEntry.get().getKey();
      wish.downvote(downvoter);
      wishes.put(wish, getVote(wish));
      return true;
    }
    return false;
  }
}
