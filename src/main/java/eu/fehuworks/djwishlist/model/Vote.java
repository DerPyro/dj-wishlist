package eu.fehuworks.djwishlist.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Vote implements Comparable<Vote> {

  private int upvotes;
  private int downvotes;

  public int getTotalVotes() {
    return upvotes - downvotes;
  }

  @Override
  public int compareTo(Vote o) {
    return Integer.compare(this.getTotalVotes(), o.getTotalVotes());
  }
}
