package tonybaines.goos.testsupport

import com.objogate.wl.swing.AWTEventQueueProber
import com.objogate.wl.swing.driver.JFrameDriver
import com.objogate.wl.swing.driver.JTableDriver
import com.objogate.wl.swing.gesture.GesturePerformer
import tonybaines.goos.app.MainWindow
import tonybaines.goos.app.SniperSnapshot
import tonybaines.goos.app.SnipersTableModel

import static com.objogate.wl.swing.matcher.IterableComponentsMatcher.matching
import static com.objogate.wl.swing.matcher.JLabelTextMatcher.withLabelText

class AuctionSniperDriver extends JFrameDriver {
  public AuctionSniperDriver(int timeoutMillis) {
    super(new GesturePerformer(),
      topLevelFrame(
        named(MainWindow.MAIN_WINDOW_NAME),
        showingOnScreen()),
      new AWTEventQueueProber(timeoutMillis, 100))
  }


  public void showsSniperStatus(String statusText) {
    new JTableDriver(
      this, named(MainWindow.SNIPERS_TABLE_NAME)).hasCell(withLabelText(statusText))
  }

  public void showsSniperStatus(SniperSnapshot snapshot) {
    def table = new JTableDriver(this)
    table.hasRow(
      matching(withLabelText(snapshot.itemId), withLabelText(snapshot.lastPrice as String),
        withLabelText(snapshot.lastBid as String), withLabelText(SnipersTableModel.textFor(snapshot.state))))
  }
}

