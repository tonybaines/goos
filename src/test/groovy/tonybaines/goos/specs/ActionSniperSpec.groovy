package tonybaines.goos.specs

import spock.lang.Specification
import tonybaines.goos.Auction
import tonybaines.goos.AuctionSniper
import tonybaines.goos.SniperListener

class ActionSniperSpec extends Specification{
  final SniperListener sniperListener = Mock(SniperListener);
  final Auction auction = Mock(Auction);
  final AuctionSniper sniper = new AuctionSniper(auction, sniperListener)

  def "reports lost when auction closed"() {
    when:
    sniper.auctionClosed()
    
    then:
    1 * sniperListener.sniperLost()
  }

  def "bids higher and reports bidding when new price arrives"() {
    given:
    final int price = 1001
    final int increment = 25

    when:
    sniper.currentPrice(price, increment)

    then:
    1 * auction.bid(price + increment)
    1 * sniperListener.sniperBidding()
  }
}
