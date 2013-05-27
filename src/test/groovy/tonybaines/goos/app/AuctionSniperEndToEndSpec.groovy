package tonybaines.goos.app

import org.jivesoftware.openfire.XMPPServer
import spock.lang.Specification

class AuctionSniperEndToEndSpec extends Specification {
  final FakeAuctionServer auction = new FakeAuctionServer("item-54321")
  final ApplicationRunner application = new ApplicationRunner()
  final Openfire openfire = new Openfire()

  def "Sniper joins auction until action closes"() {
    when:
    auction.startSellingItem()
    application.startBiddingIn(auction)
    then:
    auction.hasReceivedJoinRequestFromSniper()

    when:
    auction.announceClosed()

    then:
    application.showsSniperHasLostAuction()
  }

  def setup() {
    openfire.start()
  }

  def cleanup() {
    auction.stop()
    application.stop()
    openfire.stop()
  }
}
