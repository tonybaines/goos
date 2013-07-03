package tonybaines.goos.testsupport

import tonybaines.goos.app.Main
import tonybaines.goos.app.SniperSnapshot
import tonybaines.goos.app.SniperState
import tonybaines.goos.app.SnipersTableModel

import static tonybaines.goos.testsupport.FakeAuctionServer.XMPP_HOSTNAME

class ApplicationRunner {
  static final SNIPER_ID = "sniper"
  static final SNIPER_XMPP_ID = "$SNIPER_ID@127.0.0.1/Auction"
  static final SNIPER_PASSWORD = "sniper"
  AuctionSniperDriver driver
  String itemId

  public void startBiddingIn(final FakeAuctionServer auction) {
    this.itemId = auction.itemId
    Thread.startDaemon("Test Application") {
      try {
        Main.main(XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD, auction.itemId)
      } catch (Exception e) {
        e.printStackTrace()
      }
    }

    driver = new AuctionSniperDriver(2000)
    driver.showsSniperStatus(SnipersTableModel.textFor(SniperState.JOINING))
  }

  public void showsSniperHasLostAuction() {
    driver.showsSniperStatus(SnipersTableModel.textFor(SniperState.LOST))
  }

  public void showsSniperHasWonAuction(winningBid) {
    driver.showsSniperStatus(new SniperSnapshot(itemId, winningBid, winningBid, SniperState.WON))
  }

  public void hasShownSniperIsBidding(lastBid, currentBid) {
    driver.showsSniperStatus(new SniperSnapshot(itemId, lastBid, currentBid, SniperState.BIDDING))
  }

  public void hasShownSniperIsWinning(currentBid) {
    driver.showsSniperStatus(new SniperSnapshot(itemId, currentBid, currentBid, SniperState.WINNING))
  }

  public void stop() {
    driver?.dispose()
  }
}
