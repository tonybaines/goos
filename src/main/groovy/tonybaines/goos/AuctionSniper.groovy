package tonybaines.goos

class AuctionSniper implements AuctionEventListener {
  private final Auction auction
  private final SniperListener listener

  AuctionSniper(Auction auction, SniperListener listener) {
    this.auction = auction
    this.listener = listener
  }

  @Override
  void auctionClosed() {
    listener.sniperLost()
  }

  @Override
  void currentPrice(int price, int increment) {
    auction.bid(price + increment)
    listener.sniperBidding()
  }
}
