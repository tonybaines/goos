package tonybaines.goos.app

import groovy.swing.SwingBuilder
import groovy.util.logging.Log

import java.awt.BorderLayout
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent

@Log
class MainWindow {
  final snipers = new SnipersTableModel()
  static final STATUS_JOINING = "Joining"
  static final STATUS_LOST = "Lost"
  static final STATUS_WON = "Won"
  static final STATUS_WINNING = "Winning"
  static final STATUS_BIDDING = "Bidding"
  static final MAIN_WINDOW_NAME = "Auction Sniper"
  static final SNIPERS_TABLE_NAME = "Sniper Status"
  def swing = new SwingBuilder()

  public MainWindow() {
    swing.edt {
      swing.frame(title: MAIN_WINDOW_NAME, name: MAIN_WINDOW_NAME, id: MAIN_WINDOW_NAME, visible: true, pack: true) {
        borderLayout()
        scrollPane(id: 'scrollPane', constraints: BorderLayout.CENTER) {
          table(id: SNIPERS_TABLE_NAME, name: SNIPERS_TABLE_NAME, model: snipers)
        }
      }
    }
  }

  public void showStatus(String status) {
    snipers.setStatusText(status)
  }

  public void sniperStatusChanged(SniperSnapshot state, String status) {
    snipers.sniperStatusChanged(state, status)
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
