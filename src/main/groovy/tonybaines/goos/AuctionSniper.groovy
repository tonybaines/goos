package tonybaines.goos

class AuctionSniper implements AuctionEventListener {
  private final SniperListener listener

  AuctionSniper(SniperListener listener) {
    this.listener = listener
  }

  def auctionClosed() {
    listener.sniperLost()
  }

  @Override
  Integer currentPrice(int price, int increment) {
    return null  //To change body of implemented methods use File | Settings | File Templates.
  }
}
