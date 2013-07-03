package tonybaines.goos.app

import groovy.transform.ToString
import tonybaines.goos.SniperListener

import javax.swing.table.AbstractTableModel

@ToString(includeNames = true, includeFields = true)
class SnipersTableModel extends AbstractTableModel implements SniperListener {

  private static final STATUS_TEXT = ["Joining", "Bidding", "Winning", "Lost", "Won"]
  private final static SniperSnapshot STARTING_UP = new SniperSnapshot("", 0, 0, SniperState.JOINING)
  private def statusText = textFor(SniperState.JOINING)
  private SniperSnapshot snapshot = STARTING_UP

  @Override
  int getRowCount() { 1 }

  @Override
  int getColumnCount() { Column.values().size() }

  @Override
  def getValueAt(int rowIndex, int columnIndex) {
    Column.at(columnIndex).valueIn(snapshot)
  }

  static String textFor(SniperState state) {
    STATUS_TEXT[state.ordinal()]
  }

  @Override
  void sniperStateChanged(SniperSnapshot newSnapshot) {
    this.snapshot = newSnapshot
    fireTableRowsUpdated(0, 0)
  }
}
