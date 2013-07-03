package tonybaines.goos.app

import spock.lang.Specification

import javax.swing.event.TableModelEvent
import javax.swing.event.TableModelListener
import static tonybaines.goos.app.Column.*

class SnipersTableModelSpec extends Specification {
    private TableModelListener listener = Mock(TableModelListener)
    private final SnipersTableModel model = new SnipersTableModel()

    def setup() {
      model.addTableModelListener(listener)
    }

    def "has enough columns"() {
      expect:
      model.getColumnCount() == Column.values().size()
    }

    def "setsSniperValuesInColumns"() {
      when:
      model.sniperStateChanged(new SniperSnapshot("item id", 555, 666, SniperState.BIDDING))

      then:
      1 * listener.tableChanged {
        it.properties == new TableModelEvent(model, 0).properties
      }
      columnEquals(ITEM_IDENTIFIER, "item id")
      columnEquals(LAST_PRICE, 555)
      columnEquals(LAST_BID, 666)
      columnEquals(SNIPER_STATE, SnipersTableModel.textFor(SniperState.BIDDING))
    }

    private void columnEquals(column, expected) {
      final int rowIndex = 0
      final int columnIndex = column.ordinal()
      assert expected == model.getValueAt(rowIndex, columnIndex)
    }
}
