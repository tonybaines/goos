package tonybaines.goos.app

import groovy.swing.SwingBuilder
import groovy.util.logging.Log
import org.jivesoftware.smack.Chat
import org.jivesoftware.smack.MessageListener
import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.XMPPException
import org.jivesoftware.smack.packet.Message
import tonybaines.goos.AuctionEventListener
import tonybaines.goos.AuctionMessageTranslator

import javax.swing.JFrame
import java.awt.*
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent

@Log
class Main implements AuctionEventListener {
  @SuppressWarnings("unused")
  private Chat notToBeGCd

  static final ARG_HOSTNAME = 0
  static final ARG_USERNAME = 1
  static final ARG_PASSWORD = 2
  static final ARG_ITEM_ID = 3

  static final String AUCTION_RESOURCE = "Auction"
  static final String ITEM_ID_AS_LOGIN = "auction-%s"
  static final String AUCTION_ID_FORMAT = ITEM_ID_AS_LOGIN + "@%s/" + AUCTION_RESOURCE
  static final String BID_COMMAND_FORMAT = "SOLVersion: 1.1; Command: BID; Price: %d;"
  static final String JOIN_COMMAND_FORMAT = "SOLVersion: 1.1; Command: JOIN;"


  static MainWindow ui

  public Main() throws Exception {
    ui = new MainWindow()
  }


  public static void main(String... args) throws Exception {
    Main main = new Main()
    def connection = connection(args[ARG_HOSTNAME], args[ARG_USERNAME], args[ARG_PASSWORD])
    ui.onClose {
      connection.disconnect()
    }
    main.joinAuction(connection, args[ARG_ITEM_ID])
  }

  @Override
  def auctionClosed() {
    ui.invokeLater {
      showStatus(MainWindow.STATUS_LOST)
    }
  }

  @Override
  Integer currentPrice(int price, int increment) {
    throw new UnsupportedOperationException()
  }

  private void joinAuction(XMPPConnection connection, String itemId) throws XMPPException {
    final Chat chat = connection.getChatManager().createChat(
      auctionId(itemId, connection),
      new AuctionMessageTranslator(this))
    this.notToBeGCd = chat
    chat.sendMessage(JOIN_COMMAND_FORMAT)
  }

  private MessageListener newMessageListener(Closure c) {
    new MessageListener() {
      public void processMessage(Chat aChat, Message message) {
        c.call()
      }
    }
  }

  private static XMPPConnection connection(String hostname, String username, String password) throws XMPPException {
    XMPPConnection connection = new XMPPConnection(hostname)
    connection.connect()
    connection.login(username, password, AUCTION_RESOURCE)
    return connection
  }

  private static String auctionId(String itemId, XMPPConnection connection) {
    return String.format(AUCTION_ID_FORMAT, itemId, connection.getServiceName())
  }

  @Log
  static class MainWindow {
    static final STATUS_JOINING = "Joining"
    static final STATUS_LOST = "Lost"
    static final STATUS_BIDDING = "Bidding"
    static final MAIN_WINDOW_NAME = "Auction Sniper"
    static final SNIPER_STATUS_NAME = "Sniper Status"
    def swing = new SwingBuilder()

    public MainWindow() {
      swing.edt {
        swing.frame(title: MAIN_WINDOW_NAME, name: MAIN_WINDOW_NAME, id: MAIN_WINDOW_NAME, visible: true, pack: true) {
          label(
            id: SNIPER_STATUS_NAME,
            name: SNIPER_STATUS_NAME,
            text: STATUS_JOINING,
            border: lineBorder(color: Color.BLACK))
        }
      }
    }

    // Lookup the status label by ID, then set the new value
    public void showStatus(String status) {
      swing[SNIPER_STATUS_NAME].text = status
    }

    /**
     * Invoke the supplied closure on the Swing EDT, using 'this'
     * as its context
     */
    public void invokeLater(Closure c) {
      swing.doLater {
        c.delegate = this
        c.call()
      }
    }

    def onClose(Closure c) {
      swing[MAIN_WINDOW_NAME].addWindowListener(new WindowAdapter() {
        @Override
        void windowClosed(WindowEvent e) {
          log.info "Shutdown hook called"
          c.call()
        }
      })
    }
  }
}

