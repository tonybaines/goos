package tonybaines.goos.testsupport

import tonybaines.goos.app.Main
import tonybaines.goos.app.MainWindow
import tonybaines.goos.app.SniperSnapshot

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
    driver.showsSniperStatus(MainWindow.STATUS_JOINING)
  }

  public void showsSniperHasLostAuction() {
    driver.showsSniperStatus(MainWindow.STATUS_LOST)
  }

  public void showsSniperHasWonAuction(winningBid) {
    driver.showsSniperStatus(new SniperSnapshot(itemId, winningBid, winningBid), MainWindow.STATUS_WON)
  }

  public void hasShownSniperIsBidding(lastBid, currentBid) {
    driver.showsSniperStatus(new SniperSnapshot(itemId, lastBid, currentBid), MainWindow.STATUS_BIDDING)
  }

  public void hasShownSniperIsWinning(currentBid) {
    driver.showsSniperStatus(new SniperSnapshot(itemId, currentBid, currentBid), MainWindow.STATUS_WINNING)
  }

  public void stop() {
    driver?.dispose()
  }
}
