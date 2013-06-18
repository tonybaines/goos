package tonybaines.goos.testsupport

import com.objogate.wl.swing.AWTEventQueueProber
import com.objogate.wl.swing.driver.JFrameDriver
import com.objogate.wl.swing.driver.JLabelDriver
import com.objogate.wl.swing.gesture.GesturePerformer
import tonybaines.goos.app.Main
import tonybaines.goos.app.MainWindow

import static org.hamcrest.CoreMatchers.equalTo

class AuctionSniperDriver extends JFrameDriver {
  public AuctionSniperDriver(int timeoutMillis) {
    super(new GesturePerformer(),
      topLevelFrame(
        named(MainWindow.MAIN_WINDOW_NAME),
        showingOnScreen()),
      new AWTEventQueueProber(timeoutMillis, 100))
  }

  public void showsSniperStatus(String statusText) {
    new JLabelDriver(
      this, named(MainWindow.SNIPER_STATUS_NAME)).hasText(equalTo(statusText))
  }
}

