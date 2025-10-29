package eu.fehuworks.djwishlist.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Wish {

  private UUID id;
  private String songName;
  private String artist;
  private String issuer;
  private String issuerId;

  private Map<String, VoteType> votes;

  public enum VoteType {
    UPVOTE,
    DOWNVOTE
  }

  public void upvote(String upvoter) {
    if (votes == null) {
      votes = new HashMap<>();
    }
    if (votes.containsKey(upvoter) && votes.get(upvoter) == VoteType.UPVOTE) {
      votes.remove(upvoter);
    } else {
      votes.put(upvoter, VoteType.UPVOTE);
    }
  }

  public void downvote(String downvoter) {
    if (votes == null) {
      votes = new HashMap<>();
    }
    if (votes.containsKey(downvoter) && votes.get(downvoter) == VoteType.DOWNVOTE) {
      votes.remove(downvoter);
    } else {
      votes.put(downvoter, VoteType.DOWNVOTE);
    }
  }

  public boolean canBeDeletedBy(String deleter) {
    return issuerId.equals(deleter);
  }

  public boolean isUpvotedBy(String userId) {
    return votes != null && votes.containsKey(userId) && votes.get(userId) == VoteType.UPVOTE;
  }

  public boolean isDownvotedBy(String userId) {
    return votes != null && votes.containsKey(userId) && votes.get(userId) == VoteType.DOWNVOTE;
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof Wish && id.equals(((Wish) obj).getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
