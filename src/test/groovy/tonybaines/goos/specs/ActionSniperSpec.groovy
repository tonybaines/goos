package tonybaines.goos.specs

import spock.lang.Specification
import tonybaines.goos.AuctionSniper
import tonybaines.goos.SniperListener

class ActionSniperSpec extends Specification{
  final SniperListener sniperListener = Mock(SniperListener);
  final AuctionSniper sniper = new AuctionSniper(sniperListener)

  def "reports lost when auction closed"() {
    when:
    sniper.auctionClosed()
    
    then:
    1 * sniperListener.sniperLost()
  }
}
