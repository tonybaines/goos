package tonybaines.goos

import tonybaines.goos.AuctionEventListener.PriceSource
import tonybaines.goos.app.SniperSnapshot
import tonybaines.goos.app.SniperState

import static tonybaines.goos.AuctionEventListener.PriceSource.*

class AuctionSniper implements AuctionEventListener {
  private SniperSnapshot snapshot
  private boolean isWinning = false
  private final Auction auction
  private final String itemId
  private final SniperListener listener

  AuctionSniper(Auction auction, String itemId, SniperListener listener) {
    this.itemId = itemId
    this.auction = auction
    this.listener = listener
    this.snapshot = SniperSnapshot.joining(itemId)
  }

  @Override
  void auctionClosed() {
    snapshot = snapshot.closed();
    notifyChange()
  }

  @Override
  void currentPrice(int price, int increment, PriceSource source) {
    switch(source) {
      case FromSniper:
        snapshot = snapshot.winning(price);
        break;
      case FromOtherBidder:
        int bid = price + increment;
        auction.bid(bid);
        snapshot = snapshot.bidding(price, bid);
        break;
    }
    notifyChange();
  }

  void notifyChange() {
    listener.sniperStateChanged(snapshot)
  }
}
