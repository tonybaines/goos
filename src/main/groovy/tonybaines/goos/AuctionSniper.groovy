package tonybaines.goos

import tonybaines.goos.AuctionEventListener.PriceSource
import static tonybaines.goos.AuctionEventListener.PriceSource.*

class AuctionSniper implements AuctionEventListener {
  private boolean isWinning = false
  private final Auction auction
  private final SniperListener listener

  AuctionSniper(Auction auction, SniperListener listener) {
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
      auction.bid(price + increment)
      listener.sniperBidding()
    }
  }
}
