package tonybaines.goos.app

import spock.lang.Specification
import spock.lang.Unroll

class ColumnSpec extends Specification {
  def "looks up the correct property for the ITEM_IDENTIFIER column"() {
    expect:
    Column.ITEM_IDENTIFIER.valueIn(new SniperSnapshot(itemId: "123")) == "123"
  }

  def "looks up the correct property for the LAST_BID column"() {
    expect:
    Column.LAST_BID.valueIn(new SniperSnapshot(lastBid: 1234)) == 1234
  }

  def "looks up the correct property for the LAST_PRICE column"() {
    expect:
    Column.LAST_PRICE.valueIn(new SniperSnapshot(lastPrice: 4321)) == 4321
  }

  def "looks up the correct property for the SNIPER_STATE column"() {
    expect:
    Column.SNIPER_STATE.valueIn(new SniperSnapshot(state: SniperState.LOST)) == "Lost"
  }
}
