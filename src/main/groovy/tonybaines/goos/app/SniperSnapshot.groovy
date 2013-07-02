package tonybaines.goos.app

import groovy.transform.Immutable

@Immutable
class SniperSnapshot {
  String itemId
  int lastPrice, lastBid
  SniperState state
}
