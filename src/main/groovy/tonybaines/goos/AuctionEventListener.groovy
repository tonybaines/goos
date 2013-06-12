package tonybaines.goos

interface AuctionEventListener {

  def auctionClosed()

  Integer currentPrice(int price, int increment)
}