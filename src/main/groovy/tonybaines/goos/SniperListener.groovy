package tonybaines.goos
import tonybaines.goos.app.SniperSnapshot

public interface SniperListener {
  void sniperStateChanged(SniperSnapshot snapshot)
}