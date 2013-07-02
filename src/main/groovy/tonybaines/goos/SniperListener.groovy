package tonybaines.goos
import tonybaines.goos.app.SniperSnapshot

public interface SniperListener {
  void sniperLost()
  void sniperBidding(SniperSnapshot state)
  void sniperWinning()
  void sniperWon()
}