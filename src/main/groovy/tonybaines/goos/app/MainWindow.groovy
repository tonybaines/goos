package tonybaines.goos.app

import groovy.swing.SwingBuilder
import groovy.util.logging.Log

import java.awt.Color
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent

@Log
class MainWindow {
  static final STATUS_JOINING = "Joining"
  static final STATUS_LOST = "Lost"
  static final STATUS_WON = "Won"
  static final STATUS_WINNING = "Winning"
  static final STATUS_BIDDING = "Bidding"
  static final MAIN_WINDOW_NAME = "Auction Sniper"
  static final SNIPER_STATUS_NAME = "Sniper Status"
  def swing = new SwingBuilder()

  public MainWindow() {
    swing.edt {
      swing.frame(title: MAIN_WINDOW_NAME, name: MAIN_WINDOW_NAME, id: MAIN_WINDOW_NAME, visible: true, pack: true) {
        label(
          id: SNIPER_STATUS_NAME,
          name: SNIPER_STATUS_NAME,
          text: STATUS_JOINING,
          border: lineBorder(color: Color.BLACK))
      }
    }
  }

  // Lookup the status label by ID, then set the new value
  public void showStatus(String status) {
    swing[SNIPER_STATUS_NAME].text = status
  }

  /**
   * Invoke the supplied closure on the Swing EDT, using 'this'
   * as its context
   */
  public void invokeLater(Closure c) {
    swing.doLater {
      c.delegate = this
      c.call()
    }
  }

  def onClose(Closure c) {
    swing[MAIN_WINDOW_NAME].addWindowListener(new WindowAdapter() {
      @Override
      void windowClosed(WindowEvent e) {
        log.info "Shutdown hook called"
        c.call()
      }
    })
  }
}
