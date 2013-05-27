package tonybaines.goos.app

import static tonybaines.goos.app.FakeAuctionServer.*

class ApplicationRunner {
  static final SNIPER_ID = "sniper"
  static final SNIPER_PASSWORD = "sniper"
  AuctionSniperDriver driver

  public void startBiddingIn(final FakeAuctionServer auction) {
    Thread thread = new Thread("Test Application") {
      @Override
      public void run() {
        try {
          Main.main(XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD, auction.getItemId());
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    };
    thread.setDaemon(true);
    thread.start();
    driver = new AuctionSniperDriver(1000);
    driver.showsSniperStatus(MainWindow.STATUS_JOINING);
  }

  public void showsSniperHasLostAuction() {
    driver.showsSniperStatus(MainWindow.STATUS_LOST);
  }

  public void stop() {
    if (driver != null) {
      driver.dispose();
    }
  }

}
