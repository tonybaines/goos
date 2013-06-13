package tonybaines.goos.specs.e2e

import groovy.util.logging.Log
import spock.lang.Specification
import tonybaines.goos.testsupport.ApplicationRunner
import tonybaines.goos.testsupport.FakeAuctionServer
import tonybaines.goos.testsupport.Openfire

@Log
class AuctionSniperEndToEndSpec extends Specification {
  final FakeAuctionServer auction = new FakeAuctionServer("item-54321")
  final ApplicationRunner application = new ApplicationRunner()
  static USERS = [[name:'sniper',pass:'sniper'], [name:'auction-item-54321',pass:'auction']]
  static Openfire openfire = new Openfire(USERS)

  def "Sniper joins auction until action closes"() {
    when:
    auction.startSellingItem()
    application.startBiddingIn(auction)
    then:
    auction.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID)

    when:
    auction.announceClosed()
    then:
    application.showsSniperHasLostAuction()
  }

  def "Sniper makes a higher bid but loses"() throws Exception {
    when:
    auction.startSellingItem()
    application.startBiddingIn(auction)
    then:
    auction.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID)
    
    when:
    auction.reportPrice(1000, 98, "other bidder")
    then:
    application.hasShownSniperIsBidding()
    auction.hasReceivedBid(1098, ApplicationRunner.SNIPER_XMPP_ID)
    
    when:
    auction.announceClosed()
    then:
    application.showsSniperHasLostAuction()
  }



  def setupSpec() {
    openfire.start()
  }

  def cleanup() {
    auction.stop()
    application.stop()
    sleep 2000 // TODO: Fix this workaround - really waiting for the window-close hook to fire and the user to disconnect
  }

  def cleanupSpec() {
    openfire.stop()
  }
}
