package tonybaines.goos.app

import tonybaines.goos.SniperListener

@SuppressWarnings("GroovyAssignabilityCheck")
class SniperStateDisplayer implements SniperListener{
  private final Object ui

  SniperStateDisplayer(ui) {
    this.ui = ui
  }

  @Override
  void sniperLost() {
    ui.invokeLater {
      showStatus(MainWindow.STATUS_LOST)
    }
  }

  @Override
  void sniperBidding() {
    ui.invokeLater {
      showStatus(MainWindow.STATUS_BIDDING)
    }
  }

  @Override
  void sniperWinning() {
    ui.invokeLater {
      showStatus(MainWindow.STATUS_WINNING)
    }
  }

  @Override
  void sniperWon() {
    ui.invokeLater {
      showStatus(MainWindow.STATUS_WON)
    }
  }
}
