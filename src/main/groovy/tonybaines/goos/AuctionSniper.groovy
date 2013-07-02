package tonybaines.goos

import tonybaines.goos.AuctionEventListener.PriceSource
import tonybaines.goos.app.SniperSnapshot
import tonybaines.goos.app.SniperState

import static tonybaines.goos.AuctionEventListener.PriceSource.*

class AuctionSniper implements AuctionEventListener {
  private boolean isWinning = false
  private final Auction auction
  private final String itemId
  private final SniperListener listener

  AuctionSniper(Auction auction, String itemId, SniperListener listener) {
    this.itemId = itemId
    this.auction = auction
    this.listener = listener
  }

  @Override
  void auctionClosed() {
    if (isWinning) {
      listener.sniperWon()
    } else {
      listener.sniperLost()
    }
  }

  @Override
  void currentPrice(int price, int increment, PriceSource source) {
    isWinning = (source == FromSniper)
    if (isWinning) {
      listener.sniperWinning()
    } else {
      def bid = price + increment
      auction.bid(bid)
      listener.sniperBidding(new SniperSnapshot(itemId, price, bid, SniperState.BIDDING))
    }
  }
}
