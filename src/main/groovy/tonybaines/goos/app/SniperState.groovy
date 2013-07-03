package tonybaines.goos.app

public enum SniperState {
  JOINING {
    @Override public SniperState whenAuctionClosed() { LOST }
  },
  BIDDING {
    @Override public SniperState whenAuctionClosed() { LOST }
  },
  WINNING {
    @Override public SniperState whenAuctionClosed() { WON }
  },
  LOST,
  WON

  SniperState whenAuctionClosed() {
    throw new Defect("Auction is already closed");
  }
}