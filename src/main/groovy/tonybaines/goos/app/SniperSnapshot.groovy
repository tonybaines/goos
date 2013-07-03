package tonybaines.goos.app

import groovy.transform.Immutable

@Immutable
class SniperSnapshot {
  String itemId
  int lastPrice, lastBid
  SniperState state

  public SniperSnapshot bidding(int newLastPrice, int newLastBid) {
    new SniperSnapshot(itemId, newLastPrice, newLastBid, SniperState.BIDDING)
  }
  public SniperSnapshot winning(int newLastPrice) {
    new SniperSnapshot(itemId, newLastPrice, lastBid, SniperState.WINNING)
  }
  public static SniperSnapshot joining(String itemId) {
     new SniperSnapshot(itemId, 0, 0, SniperState.JOINING)
  }
  public SniperSnapshot closed() {
    return new SniperSnapshot(itemId, lastPrice, lastBid, state.whenAuctionClosed())
  }
}
