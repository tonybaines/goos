package tonybaines.goos.app

import groovy.transform.ToString

import javax.swing.table.AbstractTableModel

@ToString(includeNames = true, includeFields = true)
class SnipersTableModel extends AbstractTableModel {

  private static final STATUS_TEXT = [MainWindow.STATUS_JOINING, MainWindow.STATUS_BIDDING]
  private final static SniperSnapshot STARTING_UP = new SniperSnapshot("", 0, 0, SniperState.JOINING)
  private def statusText = MainWindow.STATUS_JOINING
  private SniperSnapshot sniperState = STARTING_UP

  @Override
  int getRowCount() { 1 }

  @Override
  int getColumnCount() { Column.values().size() }

  @Override
  def getValueAt(int rowIndex, int columnIndex) {
    switch (Column.at(columnIndex)) {
      case Column.ITEM_IDENTIFIER:
        return sniperState.itemId
      case Column.LAST_PRICE:
        return sniperState.lastPrice
      case Column.LAST_BID:
        return sniperState.lastBid
      case Column.SNIPER_STATUS:
        return statusText
      default:
        throw new IllegalArgumentException("No column at " + columnIndex)
    }
  }

  def setStatusText = { statusText ->
    this.statusText = statusText
    fireTableCellUpdated(0, 0)
  }

  void sniperStatusChanged(SniperSnapshot newSniperState) {
    sniperState = newSniperState
    statusText = STATUS_TEXT[newSniperState.state.ordinal()]
    fireTableRowsUpdated(0, 0)
  }

}
