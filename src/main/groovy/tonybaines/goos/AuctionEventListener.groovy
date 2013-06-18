package tonybaines.goos

interface AuctionEventListener {

  void auctionClosed()

  void currentPrice(int price, int increment)
}