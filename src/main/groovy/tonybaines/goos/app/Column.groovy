package tonybaines.goos.app

public enum Column {
  ITEM_IDENTIFIER {
    @Override public Object valueIn(SniperSnapshot snapshot) {
      snapshot.itemId
    }
  },
  LAST_PRICE {
    @Override public Object valueIn(SniperSnapshot snapshot) {
      snapshot.lastPrice
    }
  },
  LAST_BID{
    @Override public Object valueIn(SniperSnapshot snapshot) {
      snapshot.lastBid
    }
  },
  SNIPER_STATE {
    @Override public Object valueIn(SniperSnapshot snapshot) {
      SnipersTableModel.textFor(snapshot.state)
    }
  };
  abstract public Object valueIn(SniperSnapshot snapshot);
  public static Column at(int offset) { return values()[offset] }
}