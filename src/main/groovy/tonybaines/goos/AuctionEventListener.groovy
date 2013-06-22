package tonybaines.goos

import tonybaines.goos.AuctionEventListener.PriceSource

interface AuctionEventListener {
  enum PriceSource {
    FromSniper,FromOtherBidder
  }
  void auctionClosed()

  void currentPrice(int price, int increment, PriceSource source)
}