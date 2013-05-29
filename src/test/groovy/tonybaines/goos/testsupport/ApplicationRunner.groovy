package tonybaines.goos.testsupport

import tonybaines.goos.app.Main

import static tonybaines.goos.testsupport.FakeAuctionServer.XMPP_HOSTNAME

class ApplicationRunner {
  static final SNIPER_ID = "sniper"
  static final SNIPER_PASSWORD = "sniper"
  AuctionSniperDriver driver

  public void startBiddingIn(final FakeAuctionServer auction) {

    Thread.startDaemon("Test Application") {
      try {
        Main.main(XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD, auction.itemId)
      } catch (Exception e) {
        e.printStackTrace()
      }
    }

    driver = new AuctionSniperDriver(2000)
    driver.showsSniperStatus(Main.MainWindow.STATUS_JOINING)
  }

  public void showsSniperHasLostAuction() {
    driver.showsSniperStatus(Main.MainWindow.STATUS_LOST)
  }

  public void stop() {
    driver?.dispose()
  }

}
