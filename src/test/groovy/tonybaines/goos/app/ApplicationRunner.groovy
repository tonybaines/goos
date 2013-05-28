package tonybaines.goos.app

import static tonybaines.goos.app.FakeAuctionServer.*

class ApplicationRunner {
  static final SNIPER_ID = "sniper"
  static final SNIPER_PASSWORD = "sniper"
  AuctionSniperDriver driver

  public void startBiddingIn(final FakeAuctionServer auction) {

    Thread.startDaemon("Test Application") {
      try {
        Main.main(XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD, auction.getItemId());
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    driver = new AuctionSniperDriver(1500);
    driver.showsSniperStatus(Main.MainWindow.STATUS_JOINING);
  }

  public void showsSniperHasLostAuction() {
    driver.showsSniperStatus(Main.MainWindow.STATUS_LOST);
  }

  public void stop() {
    driver?.dispose();
  }

}
