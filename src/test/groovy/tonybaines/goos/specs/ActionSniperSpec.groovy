package tonybaines.goos.specs

import spock.lang.Specification
import tonybaines.goos.Auction
import tonybaines.goos.AuctionSniper
import tonybaines.goos.SniperListener

import static tonybaines.goos.AuctionEventListener.PriceSource.FromOtherBidder
import static tonybaines.goos.AuctionEventListener.PriceSource.FromSniper

class ActionSniperSpec extends Specification {
  final def sniperListener = Mock(SniperListener);
  final def auction = Mock(Auction);
  final def sniper = new AuctionSniper(auction, sniperListener)

  def "reports lost if auction closes immediately"() {
    when:
    sniper.auctionClosed()

    then:
    1 * sniperListener.sniperLost()
  }

  def "reports lost if auction closes when bidding"() {
    when:
    sniper.currentPrice(123, 45, FromOtherBidder)
    then:
    1 * sniperListener.sniperBidding()

    when:
    sniper.auctionClosed()

    then:
    1 * sniperListener.sniperLost()
  }

  def "reports won if auction closes when bidding"() {
    when:
    sniper.currentPrice(123, 45, FromSniper)
    then:
    sniperListener.sniperBidding()
    sniperListener.sniperWinning()

    when:
    sniper.auctionClosed()

    then:
    1 * sniperListener.sniperWon()
  }

  def "bids higher and reports bidding when new price arrives"() {
    given:
    final int price = 1001
    final int increment = 25

    when:
    sniper.currentPrice(price, increment, FromOtherBidder)

    then:
    1 * auction.bid(price + increment)
    1 * sniperListener.sniperBidding()
  }

  def "reports is winning when current price comes from sniper"() {
    when:
    sniper.currentPrice(123, 45, FromSniper)
    then:
    1 * sniperListener.sniperWinning()
  }
}
