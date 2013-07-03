package tonybaines.goos.app

import tonybaines.goos.SniperListener

@SuppressWarnings("GroovyAssignabilityCheck")
class SniperStateDisplayer implements SniperListener{
  private final Object ui

  SniperStateDisplayer(ui) {
    this.ui = ui
  }

  @Override
  void sniperStateChanged(SniperSnapshot snapshot) {
    ui.invokeLater {
      sniperStatusChanged(snapshot)
    }
  }
}
