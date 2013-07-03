package tonybaines.goos.app

import tonybaines.goos.SniperListener

@SuppressWarnings("GroovyAssignabilityCheck")
class SwingThreadSniperListener implements SniperListener{
  private final MainWindow ui

  SwingThreadSniperListener(ui) {
    this.ui = ui
  }

  @Override
  void sniperStateChanged(SniperSnapshot snapshot) {
    ui.invokeLater {
      ui.sniperStateChanged(snapshot)
    }
  }
}
