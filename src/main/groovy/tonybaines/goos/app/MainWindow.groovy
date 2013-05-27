package tonybaines.goos.app

import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.border.LineBorder
import java.awt.Color

class MainWindow extends JFrame {
  static final STATUS_JOINING = "JOINING"
  static final STATUS_LOST = "LOST"
  static final MAIN_WINDOW_NAME = "Auction Sniper"
  static final SNIPER_STATUS_NAME = "Sniper Status"
  private final JLabel sniperStatus = createLabel(STATUS_JOINING)

  public MainWindow() {
    super("Auction Sniper")
    setName(MAIN_WINDOW_NAME)
    add(sniperStatus)
    pack()
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    setVisible(true)
  }

  private static JLabel createLabel(String initialText) {
    JLabel result = new JLabel(initialText)
    result.setName(SNIPER_STATUS_NAME)
    result.setBorder(new LineBorder(Color.BLACK))
    return result
  }

  public void showStatus(String status) {
    sniperStatus.setText(status)
  }

}
