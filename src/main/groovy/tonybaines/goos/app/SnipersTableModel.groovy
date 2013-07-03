package tonybaines.goos.app

import groovy.transform.ToString

import javax.swing.table.AbstractTableModel

@ToString(includeNames = true, includeFields = true)
class SnipersTableModel extends AbstractTableModel {

  private static final STATUS_TEXT = ["Joining", "Bidding", "Winning", "Lost", "Won"]
  private final static SniperSnapshot STARTING_UP = new SniperSnapshot("", 0, 0, SniperState.JOINING)
  private def statusText = SnipersTableModel.textFor(SniperState.JOINING)
  private SniperSnapshot snapshot = STARTING_UP

  @Override
  int getRowCount() { 1 }

  @Override
  int getColumnCount() { Column.values().size() }

  @Override
  def getValueAt(int rowIndex, int columnIndex) {
    switch (Column.at(columnIndex)) {
      case Column.ITEM_IDENTIFIER:
        return snapshot.itemId
      case Column.LAST_PRICE:
        return snapshot.lastPrice
      case Column.LAST_BID:
        return snapshot.lastBid
      case Column.SNIPER_STATUS:
        return textFor(snapshot.state)
      default:
        throw new IllegalArgumentException("No column at " + columnIndex)
    }
  }

  void sniperStatusChanged(SniperSnapshot newSnapshot) {
    this.snapshot = newSnapshot
    fireTableRowsUpdated(0, 0)
  }

  static String textFor(SniperState state) {
    STATUS_TEXT[state.ordinal()]
  }

}
